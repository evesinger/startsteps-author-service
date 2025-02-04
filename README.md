# Author Service

## Description

Stores information about authors in a database.
We use PostgreSQL as the database and Spring Data JPA to interact with the database.

## How to run locally

### Install PostgreSQL

You can install PostgreSQL using https://postgresapp.com/ or Docker.
For now we assume you have PostgreSQL running on localhost:5432.

### Create a database

Create a database named `newsmanagement_db` in PostgreSQL.

### Run the application

Run the application using the following command:

```./mvnw spring-boot:run```

The application will be available on `http://localhost:8080`.