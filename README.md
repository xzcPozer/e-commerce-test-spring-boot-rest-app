# E-commerce-test-spring-boot-rest-app

e-commerece RESTful app with CRUD operations with delineation of roles and with the ability to add items to the cart and place orders

## Technologies

- **Spring Boot 3**
  
- **Spring Data JPA**

- **Spring Security JWT**

- **Jsonwebtoken**

- **MySQL**

## Launch

In the root of the project, you need to run `docker-compose.yml`:
```bash
docker compose up -d
```

## Test API

for test API you can authenticate how user or admin:

for auth you need to run `http://localhost:9191/api/v1/auth/login` with:

```json
{
    "email" : "admin1@email.com",
    "password" : "123456"
}
```
Afterwards you will receive a token that you will need to use in requests that require a specific role:

![изображение](https://github.com/user-attachments/assets/94c055aa-a1d6-4856-97dc-f5daa0ef5508)
