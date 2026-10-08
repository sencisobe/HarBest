# HarBest

A habit tracker where every habit is a sprout in your garden. You "water" it with the time you dedicate each day, and consistency matters more than total hours: keep your streak and the sprout grows into a tree.

## Features

- Create habits with a name (unique) and a daily objective in minutes
- Water a habit by logging the minutes you spent on it
- XP-based growth: `SPROUT` → `HALF_TREE` → `TREE`, with a streak bonus for consecutive days
- Daily progress bar and feedback when you reach (or are close to) today's objective
- Last 7 days view per habit
- Validation with clear error messages (missing name, duplicate name, non-positive minutes)

## Tech stack

| Layer    | Technology                          |
|----------|-------------------------------------|
| Backend  | Java, Spring Boot, REST API         |
| Database | PostgreSQL                          |
| Frontend | OpenUI5 (XML views, MVC, JSONModel) |

## Architecture

The backend follows Clean / Hexagonal Architecture: the domain (`Habit`, `Water`, `GrowthStage`) has no dependency on Spring or persistence, and the web layer lives in `infrastructure`.

Domain errors are translated to HTTP responses by a global exception handler (`ProblemDetail`):

| Status | Cause                                   |
|--------|-----------------------------------------|
| 400    | Invalid or missing data                 |
| 404    | Habit not found                         |
| 409    | Duplicate habit name                    |

## Project structure

```
HarBest/
├── backend/    # Spring Boot API (port 8081)
└── frontend/   # OpenUI5 app (port 8080)
```

## API

| Method | Endpoint                         | Description                          |
|--------|----------------------------------|--------------------------------------|
| GET    | `/habits`                        | List habits                          |
| POST   | `/habits`                        | Create a habit                       |
| GET    | `/habits/{id}`                   | Habit detail and progress            |
| DELETE | `/habits/{id}`                   | Delete a habit                       |
| POST   | `/habits/{id}/water`             | Log minutes for today                |
| GET    | `/habits/{id}/weekly-progress`   | Minutes per day, last 7 days         |

Example bodies:

```json
// POST /habits
{ "name": "Read", "dailyObjectiveTime": 30 }

// POST /habits/{id}/water
{ "duration": 25 }
```

`POST /habits/{id}/water` returns the updated habit plus today's status:

```json
{
  "habit": { "id": 4, "name": "Read", "growthStage": "SPROUT", "streak": 1, "totalExperience": 190.38 },
  "objectiveMetToday": false,
  "remainingMinutesToday": 29
}
```

## Getting started

### Prerequisites

- JDK and Maven
- PostgreSQL
- Node.js LTS

### Database

Create a database named `harbest`.

### Backend

Create `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/harbest
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update

server.port=8081
```

Then run:

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm start
```

Open http://localhost:8080. The backend allows CORS from that origin.

## Roadmap

- [x] MVP: create, list, detail, water, weekly progress, delete
- [ ] Multi-user support with login (authentication and per-user habits)
- [ ] Visual redesign of the garden (illustrations per growth stage)