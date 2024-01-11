### Secure and Scalable RESTful API for Notes Management

 - This project is built with Spring Boot, MongoDB, and JWT to create a secure and scalable RESTful API for managing notes.

 - Users can perform CRUD operations on notes, share them with others,secure authentication and search based on keywords. 


### Technical Stack

- **Framework**: Spring Boot

- **Database**: MongoDB

- **Authentication**: JSON Web Tokens (JWT)

- **Testing Framework**: JUnit and Spring Test

- **Search Functionality**: MongoDB text indexing

### Installation

Use JDK 21 and Mongodb Database
Postman to test the endpoints


### Build the project using Maven:

```mvn clean install```

### Run the Spring Boot application:

```mvn spring-boot:run```

### Configuration

```The application can be configured through application.properties. You may configure the database connection, and other settings as needed.```

### API Endpoints

### Authentication Endpoints
``` POST /api/auth/signup``` : Create a new user account.

```POST /api/auth/login```: Log in to an existing user account and receive an access token.

### Note Endpoints
```GET /api/notes```: Get a list of all notes for the authenticated user.

```GET /api/notes/{id}```: Get a note by ID for the authenticated user.

```POST /api/notes```: Create a new note for the authenticated user.

```PUT /api/notes/{id}```: Update an existing note by ID for the authenticated user.

```DELETE /api/notes/{id}```: Delete a note by ID for the authenticated user.

```POST /api/notes/{id}/share```: Share a note with another user for the authenticated user.

### Evaluation Criteria



- **Correctness**: Ensure the code meets the requirements and works as expected.

- **Performance**: Implement rate limiting and request throttling to handle high traffic.

- **Security**: Use secure authentication and authorization mechanisms with JWT.

- **Quality**: Organize code in a maintainable and easy-to-understand manner.

- **Completeness**: Include unit, integration, and end-to-end tests for all endpoints.

- **Search Functionality**: Implement text indexing and search functionality for efficient note searches.
