# Patient management microservices

A practice project I built along a well-known Spring Boot microservices course, to try Kafka, gRPC
and an API gateway in one small system. It is not a production system: the billing and analytics
services are placeholders.

## Services

| Service | What it does |
|---|---|
| api-gateway | Spring Cloud Gateway, routes `/api/v1/auth/**` and `/api/v1/patients/**`, and checks the JWT with the auth service before patient routes |
| auth-service | login with e-mail and password, issues and validates JWTs, own PostgreSQL database |
| patient-service | patient CRUD over REST with validation, own PostgreSQL database; creating a patient calls billing over gRPC and publishes a Protobuf event to Kafka |
| billing-service | gRPC server; placeholder that returns a fixed billing account |
| analytics-service | Kafka consumer; placeholder that only logs the patient events |

Every service has its own Dockerfile. `docker-compose.yml` starts them together with Kafka (KRaft
mode), two PostgreSQL databases and Adminer, and reads database credentials and ports from a `.env`
file.

Example requests are in `api-requests/` and `grpc-requests/` (IntelliJ HTTP client).

## Status

Not runnable from a fresh clone yet: `auth-service/pom.xml`, the `application.properties` of the
auth and patient services and a `.env.example` for docker-compose are not in the repository.
