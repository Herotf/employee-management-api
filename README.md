# 🏢 Employee Management REST API

> A production-quality RESTful API built with **Java 17 · Spring Boot 3.2 · PostgreSQL** — featuring JWT authentication, paginated search, role-based access control, and a live interactive dashboard.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=flat-square)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow?style=flat-square)

---

## 🔗 Live Links

| | Link |
|---|---|
| 🌐 Live Demo UI | `https://your-project.vercel.app` |
| ⚙️ Backend API | `https://your-api.onrender.com` |
| 📖 Swagger Docs | `https://your-api.onrender.com/swagger-ui.html` |

---

## ✨ Features

- **CRUD operations** — Create, read, update, delete employees
- **JWT Authentication** — Stateless bearer token auth with BCrypt password hashing
- **Role-based access control** — `ROLE_USER` (read-only) and `ROLE_ADMIN` (full access)
- **Pagination & sorting** — Page, size, sortBy, sortDir query parameters
- **Full-text search** — Search across name, email, department, job title
- **Multi-field filtering** — Filter by department, employment status, salary range
- **Department analytics** — Headcount, avg/min/max salary per department
- **PostgreSQL indexing** — Indexes on email, department, last name for fast queries
- **Global exception handling** — Consistent error responses across all endpoints
- **Swagger UI** — Interactive API documentation at `/swagger-ui.html`
- **Data seeding** — Auto-seeds 12 employees + 2 users on first run

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (JJWT 0.12) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 15 |
| Validation | Jakarta Bean Validation |
| Docs | SpringDoc OpenAPI / Swagger UI |
| Build | Maven |
| Utilities | Lombok |

---

## 📁 Project Structure

```
src/main/java/com/example/employeeapi/
├── controller/
│   ├── AuthController.java        # POST /api/auth/login, /register
│   └── EmployeeController.java    # CRUD + stats endpoints
├── service/
│   ├── AuthService.java           # Login, register business logic
│   └── EmployeeService.java       # Employee CRUD, search, analytics
├── repository/
│   ├── EmployeeRepository.java    # JPA queries + custom JPQL
│   └── UserRepository.java
├── model/
│   ├── Employee.java              # Entity with DB indexes
│   └── User.java                  # Auth user entity
├── dto/
│   ├── EmployeeDTO.java           # Request / Response / Summary
│   ├── AuthDTO.java               # Login / Register / AuthResponse
│   └── ApiResponse.java           # Unified response wrapper
├── security/
│   ├── JwtUtils.java              # Token generation & validation
│   ├── JwtAuthFilter.java         # Per-request token filter
│   └── UserDetailsServiceImpl.java
├── config/
│   ├── SecurityConfig.java        # Security chain + CORS
│   ├── OpenApiConfig.java         # Swagger setup
│   └── DataSeeder.java            # Initial data on startup
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    └── DuplicateResourceException.java
```

---

## 🚀 API Endpoints

### Authentication
```
POST   /api/auth/login              Public — returns JWT
POST   /api/auth/register           Public — creates user
```

### Employees
```
GET    /api/employees               USER — paginated, searchable list
GET    /api/employees/{id}          USER — get by ID
POST   /api/employees               ADMIN — create employee
PUT    /api/employees/{id}          ADMIN — update employee
DELETE /api/employees/{id}          ADMIN — delete employee
GET    /api/employees/stats/dashboard  USER — analytics
GET    /api/employees/departments   USER — department list
```

### Query Parameters (GET /api/employees)
```
page        Page number (default: 0)
size        Page size (default: 10)
search      Full-text search
department  Filter by department
status      Filter by status (ACTIVE, ON_LEAVE, INACTIVE, TERMINATED)
sortBy      Field to sort (default: firstName)
sortDir     asc or desc
```

---

## 🔐 Authentication

The API uses **stateless JWT bearer tokens**.

**1. Login:**
```bash
curl -X POST https://your-api.onrender.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**2. Use the token:**
```bash
curl https://your-api.onrender.com/api/employees \
  -H "Authorization: Bearer <your-token>"
```

**Demo credentials:**
| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN (full access) |
| `user` | `user123` | USER (read-only) |

---

## 🗄️ Database Schema

### employees
| Column | Type | Index |
|---|---|---|
| id | BIGSERIAL PK | — |
| first_name | VARCHAR(50) | — |
| last_name | VARCHAR(50) | idx_employee_last_name |
| email | VARCHAR(100) | idx_employee_email (UNIQUE) |
| department | VARCHAR(100) | idx_employee_department |
| job_title | VARCHAR(100) | — |
| salary | DECIMAL(12,2) | — |
| hire_date | DATE | — |
| status | VARCHAR(20) | — |
| phone | VARCHAR(20) | — |
| created_at | TIMESTAMP | — |
| updated_at | TIMESTAMP | — |

---

## 📦 Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Clone & configure
```bash
git clone https://github.com/yourusername/employee-api.git
cd employee-api
```

Create a database:
```sql
CREATE DATABASE employeedb;
```

### 2. Set environment variables
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/employeedb
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

Or edit `src/main/resources/application.properties` directly.

### 3. Run
```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## ☁️ Deploy to Render (Free)

### Backend
1. Push this repo to GitHub
2. Go to [render.com](https://render.com) → **New Web Service**
3. Connect your GitHub repo
4. Settings:
   - **Environment:** Java
   - **Build command:** `mvn clean package -DskipTests`
   - **Start command:** `java -jar target/employee-api-1.0.0.jar`
5. Add **Environment Variables:**
   ```
   DATABASE_URL      → from Render PostgreSQL (see below)
   DB_USERNAME       → from Render PostgreSQL
   DB_PASSWORD       → from Render PostgreSQL
   JWT_SECRET        → any 64-char hex string
   CORS_ORIGINS      → https://your-frontend.vercel.app
   ```
6. Add a **PostgreSQL** database from Render dashboard
7. Click **Deploy**

### Frontend
1. Go to [vercel.com](https://vercel.com) → **New Project**
2. Upload or link the `frontend/` folder
3. Set environment variable:
   ```
   REACT_APP_API_URL → https://your-api.onrender.com
   ```
4. Deploy

---

## 🧠 Design Decisions

**Why PostgreSQL indexes?**
Employees are frequently searched by email (unique lookups), filtered by department, and sorted by last name. Indexes on these columns cut query time from O(n) full scans to O(log n) B-tree lookups.

**Why stateless JWT?**
No server-side session storage needed. Scales horizontally without sticky sessions. Each token contains the username and roles — the filter validates and sets the SecurityContext on every request.

**Why DTOs instead of exposing entities?**
Decouples the API contract from the DB schema. Passwords, internal IDs, and audit fields are never accidentally leaked. The `Response` DTO adds a computed `fullName` field the entity doesn't have.

**Why `@Transactional(readOnly = true)` on the service?**
Read-only transactions tell Hibernate to skip dirty-checking on flush, reducing overhead for GET-heavy workloads.
