# RecallRadar backend

Spring Boot 3 API for RecallRadar India. It uses PostgreSQL in Docker and production, Flyway for schema migrations, and H2 only for automated tests.

## Required environment variables

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `JWT_SECRET` — use a long random secret in production.
- `SETUP_CODE` — private code used only to create the first organization administrator.
- `CORS_ORIGIN` — the exact Vercel frontend URL.

## Verify

Run `mvn test` for the integration and inventory/production planning tests. After deployment, check `/actuator/health` and use `/swagger-ui/index.html` for API verification.
