# Finished API endpoints

- `POST /login`
- `POST /signup`
- `POST /signup/org`
- `GET /user`
- `GET /user/{userId}`
- `GET /user/favourite`
- `POST /user/favourite/{organizerId}`
- `POST /user/unfavourite/{organizerId}`
- `GET /user/going`
- `GET /event`
- `POST /event/publish`
- `PUT /event/{eventId}`
- `DELETE /event/delete/{eventId}`
- `POST /event/{eventId}/going`
- `POST /event/{eventId}/notgoing`
- `GET /event/{eventId}/going`
- `GET /event/{eventId}/going/count`
- `GET /requests/org`
- `POST /requests/org/approve/{requestId}`
- `POST /requests/org/reject/{requestId}`

---

## Request bodies examples

login/signup body (`POST /login`, `POST /signup`)

```json
{
    "username": "admin",
    "password": "passw"
}
```

Event body (`POST /event/publish`, `PUT /event/{eventId}`):

```json
{
  "name": "Októberfest dagur 1",
  "date": "2026-09-10",
  "startTime": "17:00:00",
  "location": "Háskóli Íslands",
  "description": "Októberfest!",
  "tag": "Októberfest",
  "image": "data:image/png;base64,..."
}
```
