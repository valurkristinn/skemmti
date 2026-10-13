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


Frodo was here

Ýmir líka sjælir

Kristófer alltaf síðastur ;)

Valur líka
