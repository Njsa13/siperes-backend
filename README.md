# Programming Project Course Assignments - SpiceSwap Backend app

## Overview

This repository contains the backend application for a website platform called SpiceSwap, which is an online platform that allows users to easily share and discover recipes. This project was developed as part of the Programming Project course and uses technologies such as Spring Boot as the main framework, PostgreSQL for the database, JWT (JSON Web Tokens) for authentication and authorization, and Cloudinary for recipe image storage. Additionally, SMTP Gmail is used to send email notifications to users, and API documentation is provided using Swagger.

## Installation

To get started with the project, follow these steps:

```bash
git clone https://github.com/Njsa13/spiceswap-backend.git
cd spiceswap-backend
mvn clean install
```
## Configuration
Configure the application by inserting the necessary information into the `env.properties` file, including configuration details for PostgreSQL, JWT, SMTP Gmail, and Cloudinary.

```bash
# Database Configuration
spring.datasource.url=jdbc:postgresql://your-postgresql-host:5432/your-database-name
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
application.security.jwt.secret-key=your-secret-key
application.security.jwt.expiration=86400000
application.security.jwt.refresh-expiration=604800000

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true

# Cloudinary Configuration
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret
```

## API Documentation

The API documentation is generated using Swagger. Access the documentation by visiting `http://localhost:8080/docs` after running the application.

```bash
# Usage

To run the application, use the following command:

mvn spring-boot:run
```

## Contributors

- Najib Sauqi Rubbayani
- Syahid Nurrohim

## Acknowledgments

We would like to express our gratitude to the following technologies and frameworks that made this project possible:

- Spring Boot
- PostgreSQL
- JWT
- SMTP Gmail
- Cloudinary
- Swagger

## License

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/).
