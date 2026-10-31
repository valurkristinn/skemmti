# Skemmti

Description coming soon...

---

clone and run the program with

``` 
git clone https://github.com/valurkristinn/skemmti.git
cd skemmti
cp .env.example .env
mvn spring-boot:run 
```

The program runs an in-memory db during development which is seeded with the following users 

| role | username | userId |
| --- | ---| ---|
| admin | "admin" | 1 |
| organizer | "Oktoberfest"| 2 |
|member | "Jon Jonsson"| 3|

all have the same password: `passw`

users created after seeding will have userIds counting from 100

> events an relationships are also seeded but less relevant and can be fetched with `GET /event`

login example:

```
POST /login

{
    "username": "admin",
    "password": "passw",
}
```

see [dev.md](docs/dev.md) for development info

see [api.md](docs/api.md) for info on available endpoints

---

## Project plan

Expected hours
P.O.
Sprint
Consultation



| Week | Planned work | P.O. | Sprint |
| --- | --- | --- | --- |
| 1 | Project skeleton, database structure, GitHub setup and project planning | V | 1 |
| 2 | Class and sequence diagram setup. Decide how layers, classes and APIs work, issue creation, finish UC12 | V | 1 |
| 3 | Finish class and sequence diagrams and start work on UC4, UC5 and UC6 (signup/login) | F | 2 |
| 4 | UC8, UC9 and UC11; events, images and event management | F | 2 |
| 5 | UC3 and UC17; attendance, attendance count and favourites | F | 2 |
| 6 | UC12 paging, UC14, UC13, UC15; view and search events and see their organizer | Ý | 3 |
| 7 | UC16, UC7 and UC19; account management and integration work | Ý | 3 |
| 8 | UC10, UC18 and UC20; admin permissions and admin functionality | Ý | 3 |
| 9 | System integration, completion of unfinished work and testing | K | 4 |
| 10 | Bug fixes, usability improvements, documentation and final release | K | 4 |

