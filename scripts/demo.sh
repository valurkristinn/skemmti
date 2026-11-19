#!/bin/bash

URL="http://localhost:8080"

step() {
  echo
  echo "=== $1 ==="
  read -p "(Enter to continue)"
}

call() {
  local method=$1 path=$2 token=$3 body=$4
  local args=(-s -X "$method" "$URL$path" -w "\n%{http_code}")
  [ -n "$token" ] && args+=(-H "Authorization: Bearer $token")
  [ -n "$body" ]  && args+=(-H "Content-Type: application/json" -d "$body")

  local res=$(curl "${args[@]}")
  local status=$(echo "$res" | tail -n 1)
  local json=$(echo "$res" | sed '$d')

  [ -n "$json" ] && (echo "$json" | jq . 2>/dev/null || echo "$json")
  echo "Status: $status"
}

login() {
  curl -s -X POST "$URL/login" -H "Content-Type: application/json" \
    -d "{\"username\":\"$1\",\"password\":\"$2\"}"
}

json_id() {
  sed -n "s/.*\"$1\":\([0-9]*\).*/\1/p"
}


step "1. Anyone can see published events, no login needed"
call GET "/event"

step "2. Anyone can search events"
call GET "/event/search?searchTerm=okt"

step "3. Viewing your own account without logging in -> 401"
call GET "/user"


step "4. Sign up a new member (logged in right away)"
MEMBER=$(curl -s -X POST "$URL/signup" -H "Content-Type: application/json" \
  -d '{"username":"demo_user","password":"demopassw"}')
echo "Token: ${MEMBER:0:30}..."
MEMBER_ID=$(curl -s "$URL/user" -H "Authorization: Bearer $MEMBER" | json_id userId)

step "5. Member adds a description and a profile picture"
IMG="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
call PATCH "/user" "$MEMBER" "{\"description\":\"I love concerts\",\"image\":\"$IMG\"}"

step "6. Member favourites the organizer Oktoberfest"
call POST "/user/favourite/2" "$MEMBER"
echo "-> Favourites now:"
call GET "/user/favourite" "$MEMBER"

step "7. Member removes the favourite again"
call POST "/user/unfavourite/2" "$MEMBER"
echo "-> Favourites now:"
call GET "/user/favourite" "$MEMBER"

step "8. Member marks themselves as going to event 1"
call POST "/event/1/going" "$MEMBER"
echo "-> Events the member is going to now:"
call GET "/user/going" "$MEMBER"
echo "-> Number of people going to event 1:"
call GET "/event/1/going/count"

step "9. Member is not going after all"
call POST "/event/1/notgoing" "$MEMBER"
echo "-> Events the member is going to now:"
call GET "/user/going" "$MEMBER"
echo "-> Number of people going to event 1:"
call GET "/event/1/going/count"

step "10. Member tries to create an event -> 403 (only organizers can)"
call POST "/event" "$MEMBER" \
  '{"name":"Party","date":"2026-12-24","startTime":"20:00:00","location":"Reykjavik","description":"Not allowed"}'


step "11. Sign up as an organizer -> a request is sent to the admin"
RES=$(curl -s -X POST "$URL/signup/org" -H "Content-Type: application/json" \
  -d '{"username":"Jolatonleikar","password":"demopassw"}')
echo "$RES" | jq .
REQ_ID=$(echo "$RES" | json_id requestId)

step "12. The organizer tries to log in before approval -> 401"
call POST "/login" "" '{"username":"Jolatonleikar","password":"demopassw"}'


step "13. Admin logs in and sees pending organizer requests"
ADMIN=$(login "admin" "passw")
call GET "/requests/org" "$ADMIN"

step "14. Admin approves the request"
RES=$(curl -s -X POST "$URL/requests/org/approve/$REQ_ID" -H "Authorization: Bearer $ADMIN")
echo "$RES" | jq .
ORG_ID=$(echo "$RES" | json_id userId)

step "15. Admin edits another user's account"
call PATCH "/user/$MEMBER_ID" "$ADMIN" '{"description":"Edited by admin"}'

step "16. Admin deletes that user"
call DELETE "/user/$MEMBER_ID" "$ADMIN"
echo "-> Looking up the deleted user:"
call GET "/user/$MEMBER_ID" "$ADMIN"


step "17. The approved organizer can now log in"
ORG=$(login "Jolatonleikar" "demopassw")
echo "Token: ${ORG:0:30}..."

step "18. Organizer creates an event"
RES=$(curl -s -X POST "$URL/event" -H "Authorization: Bearer $ORG" -H "Content-Type: application/json" \
  -d '{"name":"Jolatonleikar","date":"2026-12-10","startTime":"20:00:00","location":"Harpa","description":"Christmas concert"}')
echo "$RES" | jq .
EVENT_ID=$(echo "$RES" | json_id eventId)

step "19. Anyone can see the organizer's upcoming events"
call GET "/org/$ORG_ID/event?time=upcoming"

step "20. Organizer edits the event"
call PUT "/event/$EVENT_ID" "$ORG" \
  '{"name":"Jolatonleikar","date":"2026-12-10","startTime":"21:00:00","location":"Harpa","description":"Now starts at 21:00"}'

step "21. Another organizer (Oktoberfest) tries to delete it -> 403"
OKT=$(login "Oktoberfest" "passw")
call DELETE "/event/$EVENT_ID" "$OKT"

step "22. The owner deletes the event"
call DELETE "/event/$EVENT_ID" "$ORG"
echo "-> The organizer's events now:"
call GET "/org/$ORG_ID/event"

echo
echo "=== Demo done ==="