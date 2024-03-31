# inventory-management
- Java 17
- Java Spring Boot
- Spring Security Basic Auth
- Spring Data JPA
- Postgresql
- Spring Web
- Lombok
- Docker
- OpenAPI
- JUnit
- H2
## Setup for a PostgreSQL
Set up PostgreSQL with 
```
username=postgres
password=123
datasource.url=jdbc:postgresql://localhost:5432/postgres
```
## Setup Project

```bash
git clone https://github.com/azizyelbay/inventory-management.git
```

Run `mvn clean install` to build the project.

Run `mvn spring-boot:run` to start the application.

## HTTP basic Auth
```
username=user
password=user@123
```
## Swagger UI Doc

http://localhost:8080/swagger-ui/index.html

![image](https://github.com/azizyelbay/inventory-management/assets/34959497/a7378dd1-ffd5-420d-ba63-5f56dcf43596)
