# CampusFind — Lost and Found Item Tracker for Campus

A production-ready **Spring Boot REST backend** designed for university and college campuses. CampusFind provides a centralized platform where students can report lost items, security staff can log found items, and an intelligent matching engine automatically pairs matching lost and found reports by category, location, and keywords.

---

## 🌟 Key Features

1. **Role-Based Authentication & Management:**
   * Pre-configured roles: `STUDENT`, `STAFF`, and `ADMIN`.
   * Secure registration and authentication endpoints.

2. **Lost Item Reporting:**
   * Students report lost items specifying title, description, location, date, and category.
   * Status tracking (`OPEN` &rarr; `RESOLVED`).

3. **Found Item Workflow & Strict Business Rules:**
   * Security staff log found items with initial status `AVAILABLE`.
   * **Business Rule 1 (State Progression):** A found item **cannot** be marked `RETURNED` unless it is first marked `CLAIMED` (`AVAILABLE` &rarr; `CLAIMED` &rarr; `RETURNED`).
   * **Business Rule 2 (Authorization):** Only an `ADMIN` or the reporting staff member can update a found item's status. Invalid requests are rejected with `403 Forbidden`.

4. **Intelligent Matching Engine:**
   * Fuzzy keyword and category matching algorithm scoring similarities (0–100%) between open lost reports and active found items.
   * Generates human-readable match explanations.

5. **Advanced Dynamic Search & Filter (JPA Specification):**
   * Filter lost reports and found items by category, status, date range, or multi-field keyword search (title, description, location).

6. **Live Admin Dashboard:**
   * Real-time metrics tracking pending, claimed, and returned items, total users, and active reports.

7. **Global Exception Handling & Validation:**
   * Standardized RFC 7807 error responses using `@RestControllerAdvice` with field-level validation messages.

---

## 🛠️ Technology Stack

* **Framework:** Spring Boot 4.x / 3.x
* **Language:** Java 26
* **Data Access:** Spring Data JPA / Hibernate ORM
* **Database:** MySQL
* **Validation:** Jakarta Bean Validation (Hibernate Validator)
* **Testing:** Postman Collection included

---

## 📋 API Endpoints

### 1. Authentication (`/api/auth`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register student, staff, or admin |
| `POST` | `/api/auth/login` | Authenticate user |
| `GET` | `/api/auth/users` | List all users |

### 2. Categories (`/api/categories`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/categories` | List all categories |
| `POST` | `/api/categories` | Create a new category |

### 3. Lost Reports (`/api/lost-reports`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/lost-reports` | File a new lost item report |
| `GET` | `/api/lost-reports` | List & filter lost reports (supports `categoryId`, `status`, `keyword`, `startDate`, `endDate`) |
| `GET` | `/api/lost-reports/{id}` | Get report by ID |
| `GET` | `/api/lost-reports/user/{userId}` | Get reports by student |
| `PUT` | `/api/lost-reports/{id}/resolve` | Mark report as RESOLVED |

### 4. Found Items (`/api/found-items`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/found-items` | Staff logs a found item |
| `GET` | `/api/found-items` | List & filter found items (supports `categoryId`, `status`, `keyword`, `startDate`, `endDate`) |
| `GET` | `/api/found-items/{id}` | Get item by ID |
| `PUT` | `/api/found-items/{id}/status` | Update status (`AVAILABLE` &rarr; `CLAIMED` &rarr; `RETURNED`) |

### 5. Matching & Dashboard
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/matches` | List potential matches with match scores and reasons |
| `GET` | `/api/admin/dashboard` | Get system statistics and counts |
| `GET` | `/` | API status and endpoint directory |

---

## 🚀 Running the Project Locally

### Prerequisites
* JDK 17+ or JDK 26
* MySQL 8.x running locally on port `3306` with database `campusfind_db`
  *(credentials configurable in `src/main/resources/application.properties`)*

### Quick Start
1. Clone the repository:
   ```bash
   git clone https://github.com/INDIRESH-P-L/CampusFind.git
   cd CampusFind
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Open your browser:
   * **API Index:** `http://localhost:8080/`

---

## 📬 Postman Testing

A complete Postman collection is included in the root directory:
📂 **`CampusFind_Postman_Collection.json`**

Import this file directly into Postman to test all endpoints, normal flows, and business rule edge cases with one click.
