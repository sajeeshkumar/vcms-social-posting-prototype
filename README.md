# VCMS Social Posting Prototype

Welcome to the **VCMS Social Posting Prototype**! This is a simple platform for scheduling and posting messages to multiple social media platforms like Twitter (X), Instagram, and Bluesky.

---

## Tech Stack

### Backend (Java)
- **Spring Boot 3**: Core backend framework.
- **Spring Scheduling**: To handle scheduled posts.
- **Spring Data JPA**: Database interaction (currently using in-memory H2 for prototype).
- **Contract-First API Development**:
  - OpenAPI (Swagger) specification used to define APIs first.
  - Controllers are generated from API contracts.

### Frontend (React)
- **React 18**: Frontend UI library.
- **Vite**: Frontend build tool for fast builds.
- **TypeScript**: Type-safe JavaScript.
- **Axios**: For API calls.

---

## How to Build and Run Locally

### Prerequisites
- Java 17+
- Node.js 18+
- Docker (optional, for database if needed)

### Backend Setup

```bash
# Navigate to backend folder
cd backend

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend server will start on: `http://localhost:8080`

### Frontend Setup

```bash
# Navigate to frontend folder
cd frontend

# Install dependencies
npm install

# Run the development server
npm run dev
```

The frontend will be available at: `http://localhost:5173`

---

## Extending to Support a New Platform

Adding a new platform is **super simple**!

1. **Create a new class** implementing `PlatformPoster`:

```java
@Service
public class LinkedInPoster implements PlatformPoster {

    @Override
    public String getPlatformName() {
        return "LinkedIn";
    }

    @Override
    public void post(String message, List<String> mediaUrls) {
        // Add LinkedIn API integration logic here
    }
}
```

2. **Spring Boot** will automatically detect the new bean and inject it into the platform poster list.

3. **No changes needed in `PostService`!** It's fully extendible by design.

4. **Frontend** will automatically list "LinkedIn" if the backend reports it in the `/platforms` API.

---

## Contract-First API Development

- We define all APIs **first** using an OpenAPI 3.0 YAML/JSON file.
- Backend controllers are generated using code generation tools (e.g., OpenAPI Generator).
- This approach ensures:
  - Clear API contract between frontend and backend.
  - Easier validation, documentation, and testing.

You can view the API documentation by visiting `http://localhost:8080/swagger-ui.html` after starting the backend.

---

## Useful Commands

### Backend
| Task                  | Command |
| --------------------- | ------- |
| Build backend         | `./mvnw clean install` |
| Run backend           | `./mvnw spring-boot:run` |
| Run tests             | `./mvnw test` |

### Frontend
| Task                  | Command |
| --------------------- | ------- |
| Install dependencies  | `npm install` |
| Run frontend          | `npm run dev` |
| Build frontend        | `npm run build` |
| Run frontend tests    | `npm run test` |

### Run Docker Compose

- Make sure Docker is installed
- Build backend mvn clean install
- From home folder: docker compose build
- From home folder: docker compose up
- Front end available at http://localhost:5173

---

## Continuous Integration (CI)

GitHub Actions is used for CI/CD.

CI Workflow: .github/workflows/ci.yml

### Key steps:

- Checkout code

- Set up Java and Node.js

- Build backend using Maven

- Build frontend using npm

- Run unit and integration tests

- Build Docker images

## Future Improvements
- Add OAuth2 authentication to social media APIs.
- Use production-ready databases like PostgreSQL.
- CD pipelines for automated deployments.

---



> Made with ❤️ by Koding Krafters Inc.