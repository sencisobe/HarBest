# HarBest

A habit tracker where every habit is a sprout in your garden. You "water" it with the time you dedicate each day, and consistency matters more than total hours: keep your streak and the sprout grows into a tree.

[Project Board](https://github.com/users/sencisobe/projects/1) | [Sprint Documentation](https://github.com/sencisobe/HarBest/milestones)

## Features

- **Multi-user support**: Registration and login using email or username. Each user manages their own private garden.
- **Habit management**: Create habits with a unique name and a daily objective in minutes.
- **Watering system**: Log the minutes spent on a habit to increase its experience.
- **XP-based growth**: `SPROUT` → `HALF_TREE` → `TREE`, with a streak bonus for consecutive days.
- **Progress tracking**: Daily progress bar with feedback and a historical 7-day view per habit.
- **Security & Audit**: Password recovery system and security event logging (successful/failed logins, registrations, password resets) to monitor account activity.

## Screenshots

<p align="center">
  <img src="docs/images/watering-habit.gif" width="48%" alt="Watering a habit">
  <img src="docs/images/weekly-progress.png" width="48%" alt="Weekly progress view">
</p>
<p align="center">
  <em>(Sustituye las rutas por tus imágenes reales. Un GIF mostrando cómo sube la barra al "regar" el hábito queda genial aquí, junto con una vista de la gráfica semanal o la pantalla de login).</em>
</p>

## Tech stack

| Layer    | Technology                                |
|----------|-------------------------------------------|
| Backend  | Java, Spring Boot, REST API               |
| Database | PostgreSQL                                |
| Frontend | OpenUI5 (XML views, MVC, JSONModel)       |

## Architecture

The backend follows Clean / Hexagonal Architecture: the domain (`Habit`, `Water`, `GrowthStage`, `User`) has no dependency on Spring or persistence, and the web layer lives in `infrastructure`.

Domain errors are translated to HTTP responses by a global exception handler (`ProblemDetail`):

| Status | Cause                                                                 |
|--------|-----------------------------------------------------------------------|
| 400    | Invalid or missing data                                               |
| 401    | Unauthorized (Missing or invalid token, invalid login credentials)    |
| 404    | Habit or User not found                                               |
| 409    | Duplicate habit name, or email/username already registered            |

## Project structure

```text
HarBest/
├── backend/    # Spring Boot API (port 8081)
├── frontend/   # OpenUI5 app (port 8080)
└── docs/       # Sprint documentation and images
```

## API

**Authentication:** All `/habits` endpoints require an `Authorization` header with a valid Bearer token (`Authorization: Bearer <your_jwt_token>`).

| Method | Endpoint                         | Description                                  |
|--------|----------------------------------|----------------------------------------------|
| POST   | `/auth/register`                 | Register a new user                          |
| POST   | `/auth/login`                    | Authenticate user and receive JWT            |
| POST   | `/auth/forgot-password`          | Request a password reset token               |
| POST   | `/auth/reset-password`           | Reset password using the provided token      |
| GET    | `/habits`                        | List authenticated user's habits             |
| POST   | `/habits`                        | Create a new habit                           |
| GET    | `/habits/{id}`                   | Habit detail and progress                    |
| DELETE | `/habits/{id}`                   | Delete a habit                               |
| POST   | `/habits/{id}/water`             | Log minutes for today                        |
| GET    | `/habits/{id}/weekly-progress`   | Minutes per day, last 7 days                 |

Example bodies:

```json
// POST /auth/login
{ "username": "user1", "password": "securepassword" }

// POST /habits
{ "name": "Read", "dailyObjectiveTime": 30 }

// POST /habits/{id}/water
{ "duration": 25 }
```

## Languages

The interface is available in English and Spanish (i18n).

## Getting started

### Prerequisites

- JDK and Maven
- PostgreSQL
- Node.js LTS

### Database

Create a database named `harbest`.

### Backend

Copy the example properties file or create `backend/src/main/resources/application.properties` directly:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/harbest
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8081

# JWT Auth (Change in production)
jwt.secret=YOUR_SUPER_SECRET_KEY_AT_LEAST_256_BITS
jwt.expiration=86400000
```
*(Tip: It is recommended to keep an `application.properties.example` in the repository for reference).*

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

## Known Development Limitations

* **Password Reset:** Currently, when requesting a password reset via `/auth/forgot-password`, the reset token is printed to the backend console instead of being emailed to the user. This is a known limitation for the development environment. Email integration is planned for a future release.

## Roadmap

- [x] MVP: create, list, detail, water, weekly progress, delete
- [x] Multi-user support with login (authentication and per-user habits) (v0.2.0)
- [ ] Visual redesign of the garden (illustrations per growth stage) - *Upcoming UI/UX Sprint*