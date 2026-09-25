# Skemmti

Description coming soon...

---

run the program with

``` 
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

---


Frodo was here

Ýmir líka sjælir

Kristófer alltaf síðastur ;)

Valur líka
