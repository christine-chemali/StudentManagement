# Laplateformetracker - Student Management System

## Description

This project is a student management system developed in Java with a PostgreSQL database. It allows managing student information (name, surname, age, grades ...) with advanced features for search, sorting, statistics, and security.

## Features

### Basic Features
- **Add a student**: Input and save information
- **Edit a student**: Search by ID and modify data
- **Delete a student**: Remove by ID
- **Display all students**: Complete list of students
- **Search for a student**: Lookup by ID

### Advanced Features
The application includes several advanced features to enhance user experience and productivity:
- **Sort students**: By name, surname, age ect...
- **Advanced search**: Muliple criteria (age, average grade, ect...)
- **Statistics**: Class average, age distribution ...
- **Import/Export**: Formats CSV
- **Pagination**: Display in batches
- **Authentification**: Secured by login/password
- **Registration**: to add new users
- **Export Results**: CSV, PDF
- **Automatic Backup**: Protection against data loss
- **Undo operations**: Undo/redo functionality for CRUD actions
- **Table Sorting**: Comprehensive sorting capabilities across all data tables with persistent ordering

For detailed documentation on the table sorting functionality, please refer to the [table sorting feature documentation](docs/table-sorting-feature.md).

## Architecture

This project follows a classic MVC (Model-View-Controller) architecture with a layered organization:

- **Model**: Classes representing business entities (Student, Grade, etc.)
- **DAO**: Data Access layer using the DAO pattern
- **Service**: Business layer orchestrating calls to DAOs
- **Controller**: Management of user interactions and display

For detailed architecture documentation, please consult the [architecture documentation](docs/architecture.md).

## Database

This application uses a relational database to store information about students, classes, subjects, and grades.

- **Main tables**: User, Class, Student, Subject, Grade, SubjectComment, and Backup
- **Relationships**: Associations between students, classes, subjects, and grades
- **Constraints**: Integrity rules to ensure data consistency

For detailed database schema documentation, please consult the [database schema documentation](docs/database-schema.md).

## User Stories

This application has been developed based on a comprehensive set of user stories that define the core functionality and user experience requirements.

- **Authentication & Access**: User login, registration, and welcome screen
- **Student Management**: View, search, add, edit, and delete student records
- **Grade Management**: Track academic performance with grades and comments
- **Analytics**: Statistical views of student performance and demographics

For the complete list of user stories that guided the development process, please refer to the [user stories documentation](docs/user-stories.md).

## User Interface

The application features a clean, intuitive user interface designed for optimal user experience and productivity.

- **Authentication screens**: Login and registration interfaces
- **Student management**: Comprehensive views for student data management
- **Statistics visualizations**: Various charts and graphs for data analysis
- **Grade management**: Interfaces for tracking academic performance

For detailed mockups and interface layouts, please refer to the [UI layouts documentation](docs/ui-layouts.md).

Voici la section finale à ajouter à votre README, basée sur les informations que vous avez fournies :

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher
- JavaFX 16 (included in the dependencies)

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/christine-chemali/StudentManagement.git
cd StudentManagement
```

### 2. Database Configuration

Create a PostgreSQL database for the application:

```sql
CREATE DATABASE student_management;
```

The application will automatically create the necessary tables when it first runs, based on the schema defined in the application.

### 3. Configure application properties

Create or modify the file `src/main/resources/config/application.properties`:

```properties
# Database Configuration
db.url=jdbc:postgresql://localhost:5432/student_management
db.username=your_username
db.password=your_password
```

### 4. Build and Run

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Run the application with JavaFX
mvn javafx:run
```

## Running Tests

```bash
# Run all tests with detailed output
mvn test

# Run with code coverage report (JaCoCo)
mvn test jacoco:report
```

The test report will be available in `target/site/jacoco/index.html`.

## Building for Distribution

```bash
# Create an executable JAR
mvn package

# Run the packaged application
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media -jar target/student-management-system-1.0-SNAPSHOT.jar
```

## Technologies Used

- **Java 17**: Core programming language
- **JavaFX 16**: UI framework
- **PostgreSQL**: Database
- **Maven**: Build and dependency management
- **JUnit 5 & Mockito**: Testing frameworks
- **TestFX**: JavaFX UI testing
- **BCrypt**: Password hashing
- **iText & PDFBox**: PDF generation
- **OpenCSV**: CSV file processing
- **JaCoCo**: Code coverage analysis

## Author & Support

For questions or issues, please contact:

- **Email**: christine.chemali@laplateforme.io
- **GitHub**: [christine-chemali](https://github.com/christine-chemali)
- **Project Repository**: [StudentManagement](https://github.com/christine-chemali/StudentManagement)

## License

This project is currently unlicensed. All rights reserved.