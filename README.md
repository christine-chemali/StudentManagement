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

### Advance Feature hilight: Table Sorting Functionality

#### Overview
The application provides intuitive table sorting capabilities across all data views, allowing users to organize information efficiently with persistent sorting across pagination.

#### How It Works

##### Column Header Interaction
Users can sort data by **clicking on column headers** in any table view:

- **First click**: Sorts in ascending order (A→Z, 1→9) with ↑ indicator
- **Second click**: Sorts in descending order (Z→A, 9→1) with ↓ indicator  
- **Third click**: Returns to default order (no indicator)

##### Available Sorting Options

###### Student Management Table
```
+-------------------+------------------------------------------+
| Column            | Sort Behavior                            |
|-------------------|------------------------------------------|
| **ID**            | Numerical order (student IDs)            |
| **First Name**    | Alphabetical order                       |
| **Last Name**     | Alphabetical order                       |
| **Age**           | Numerical order (youngest to oldest)     |
| **Class**         | Alphanumerical order (6A, 6B, 5A, etc.)  |
| **Average Grade** | Numerical order (performance ranking)    |
+-------------------+------------------------------------------+
```

###### Grade Management Table
```
+---------------------+------------------------------------------+
| Column              | Sort Behavior                            |
|---------------------|------------------------------------------|
| **Subject**         | Alphabetical order                       |
| **Min Average**     | Numerical order (class performance)      |
| **Max Average**     | Numerical order (class performance)      |
| **Student Average** | Numerical order (individual performance) |
+---------------------+------------------------------------------+

```

###### Persistent Sorting
- **Cross-page persistence**: Sorting order is maintained when navigating between pages
- **Session continuity**: Selected sort remains active throughout the user session
- **Search compatibility**: Sorting works seamlessly with search filters

##### Use Cases

###### Academic Performance Analysis

1. Click "Average Grade" header → View top/bottom performers
2. Navigate through pages → Ranking order preserved
3. Identify students needing support or recognition


###### Class Management

1. Click "Class" header → Group students by grade level
2. Browse pages → Class grouping maintained
3. Facilitate grade-level specific operations


##### Subject Performance Review

1. Click "Student Average" in grades view → See strengths/weaknesses
2. Navigate subjects → Performance ranking preserved
3. Quick identification of problem areas


##### Technical Implementation

##### SearchCriteria Enhancement
The sorting functionality is powered by extended `SearchCriteria` class:

```java
// Core sorting properties
private String sortField;
private String sortDirection;

// Accessor methods
public String getSortField()
public void setSortField(String sortField)
public String getSortDirection()
public void setSortDirection(String sortDirection)
```

##### Controller Integration
Controllers automatically capture and preserve sorting preferences:

```java
// Capture user sort selection
String sortField = sortColumn.getId().replace("Column", "");
String sortDirection = sortColumn.getSortType().toString();
criteria.setSortField(sortField);
criteria.setSortDirection(sortDirection);
```

##### User Benefits

- **Improved Efficiency**: No need to re-sort after each page change
- **Better Data Analysis**: Quick identification of patterns and trends
- **Intuitive Interface**: Standard sorting behavior expected in modern applications
- **Enhanced Navigation**: Seamless data exploration with maintained context

##### Visual Indicators

The interface provides clear visual feedback:
- **↑ Arrow**: Ascending sort active
- **↓ Arrow**: Descending sort active
- **No Arrow**: Default/natural order

This sorting system significantly enhances the user experience by providing efficient data organization tools that work consistently across all application views.

## Project Architecture
This application follows a classic layered architecture with the MVC (Model-View-Controller) pattern.
Here are the main layers:

### 1. **Model Layer (Data Model)**
* * *
Classes that represent business entities, and contains attributes, constructors, getters/setters, and utility methods.

- **Student.java**:

    - `Student()`-> Default constructor  
    - `Student()` -> Constructor with basic parameters  
    - `getStudentId()` -> Returns the student's ID  
    - `getFirstName()` -> Returns the student's first name  
    - `setFirstName()` -> Sets the student's first name  
    - `getLastName()` -> Returns the student's last name  
    - `setLastName()` -> Sets the student's last name  
    - `getAge()` -> Returns the student's age  
    - `setAge()` -> Sets the student's age  
     - `getAverageGrade()` -> Returns the student's average grade
    - `setAverageGrade()` -> Sets the student's average grade
    - `getFullName()` -> Returns the full name (first name - last name) 
    - `getStudentClassName()` -> Returns the class name
    - `setClassName()` -> Sets the class name (in db should be class_name VARCHAR(10) to stock "5B", "T1" ect...)
    - `toString()` -> Returns a textual representation of the student 

```
+-------------------------------------------+
|                 Student                   |
+-------------------------------------------+
| - studentId: Long                         |
| - firstName: String                       |
| - lastName: String                        |
| - age: int                                |
| - className: String                       |
+-------------------------------------------+
| - Student()                               |
| - Student(String, String, int)            |
| - getStudentId(): Long                    |
| - getFirstName(): String                  |
| - setFirstName(String): void              |
| - getLastName(): String                   |
| - setLastName(String): void               |
| - getAverageGrade(): double               |
| - setAverageGrade(double): void           |
| - getAge(): int                           |
| - setAge(int): void                       |
| - getFullName(): String                   |
| - getStudentClassName(): String           |
| - setClassName(String): void              |
| - toString(): String                      |
+-------------------------------------------+
```

- **Grade.java**: 

    - `Grade()`-> Default constructor  
    - `Grade()` -> Constructor with student ID, school subject, grade value
    - `getGradeId()` -> Returns the unique grade ID
    - `getStudentId()` -> Returns the student ID
    - `setStudentId()` -> Sets the student ID
    - `getSubject()` -> Returns the school subject of the grade
    - `setSubject()` -> Sets the school subject of the grade
    - `getValue()` -> Returns the numeric value of the grade
    - `setValue()` -> Sets the numeric value of the grade
    - `getCoefficient()` -> Returns the grade coefficient
    - `setCoefficient()` -> Sets the grade coefficient
    - `getDate()` -> Returns the date of the grade
    - `setDate()` -> Sets the date of the grade
    - `getWeightedGradeValue()` -> Calculates and returns the weighted value of the grade (value * coefficient)
    - `toString()` -> Returns a textual representation of the grade

```
+-------------------------------------------+
|                  Grade                    |
+-------------------------------------------+
| - id: Long                                |
| - studentId: Long                         |
| - subject: String                         |
| - value: double                           |
| - coefficient: double                     |
| - date: Date                              |
+-------------------------------------------+
| - Grade()                                 |
| - Grade(Long, String, double, double)     |
| - getGradeId(): Long                      |
| - getStudentId(): Long                    |
| - setStudentId(Long): void                |
| - getSubject(): String                    |
| - setSubject(String): void                |
| - getValue(): double                      |
| - setValue(double): void                  |
| - getCoefficient(): double                |
| - setCoefficient(double): void            |
| - getDate(): Date                         |
| - setDate(Date): void                     |
| - getWeightedGradeValue(): double         |
| - toString(): String                      |
+-------------------------------------------+
```

- **SubjectComment.java**:

    - `getId()` -> Returns unique comment ID
    - `getStudentId()` -> Returns student ID
    - `getSubject()` -> Returns school subject
    - `getComment()` -> Returns teacher's trimester comment
    - `setComment()` -> Sets teacher's comment

```
+-------------------------------------------+
|            SubjectComment                 |
+-------------------------------------------+
| - id: Long                                |
| - studentId: Long                         |
| - subject: String                         |
| - comment: String                         |
+-------------------------------------------+
| + SubjectComment()                        |
| + getId(): Long                           |
| + getStudentId(): Long                    |
| + getSubject(): String                    |
| + getComment(): String                    |
| + setComment(String): void                |
+-------------------------------------------+
```

- **User.java**: 

    - `User()` -> Default constructor  
    - `User()` -> Constructor with username and password
    - `getUserId()` -> Returns the unique Id of the user
    - `getUsername()` -> Returns the username
    - `setUsername()` -> Sets the username
    - `getPasswordHash()` -> Returns the password hash (for DAO access only)
    - `setPasswordHash()` -> Sets the password hash (for DAO access only)
    - `toString()` -> Returns a textual representation of the user

```
+-------------------------------------------+
|                  User                     |
+-------------------------------------------+
| - id: Long                                |
| - username: String                        |
| - passwordHash: String                    |
+-------------------------------------------+
| + User()                                  |
| + User(String, String)                    |
| + getUserId(): Long                       |
| + getUsername(): String                   |
| + setUsername(String): void               |
| + getPasswordHash(): String               |
| + setPasswordHash(String): void           |
| + toString(): String                      |
+-------------------------------------------+
```

### 2. **DAO (Data Access Object) Layer**
* * *

The DAO pattern separates data access logic from business logic.
Advantages of the DAO Pattern:
- **Separation of concerns**: Data access logic is isolated
- **Testability**: It is easy to create mock implementations for testing
- **Flexibility**: Changing the database only requires changing DAO implementation.

- **BaseDAO.java**: Abstract base class for all DAOs
    - `connection` -> Database connection object
    - `BaseDAO(Connection)` -> Constructor with connection injection
    - `mapResultSet(ResultSet)` -> Abstract method to convert ResultSet to entity
    - `count(String)` -> Generic count method for any table
    - `closeResources(ResultSet, PreparedStatement)` -> Utility to close database resources

```java
public abstract class BaseDAO<T> {
    protected Connection connection;
    
    public BaseDAO(Connection connection) {
        this.connection = connection;
    }
    
    protected abstract T mapResultSet(ResultSet rs) throws SQLException;
    protected int count(String tableName) { ... }
    protected void closeResources(ResultSet rs, PreparedStatement stmt) { ... }
}
```
```
+------------------------------------------------------+
|                 BaseDAO                              |
+------------------------------------------------------+
| - connection: Connection                             |
+------------------------------------------------------+
| + BaseDAO(Connection)                                |
| + mapResultSet(ResultSet): T                         |
| + count(String): int                                 |
| + closeResources(ResultSet, PreparedStatement): void |
+------------------------------------------------------+

```
> The `BaseDAO` class is an abstract template for all Data Access Objects (DAOs) in the project. It includes:
> 
> - **Connection**: A database connection object for performing data operations.
> - **Constructor**: Initializes the DAO with a specific database connection.
> - **mapResultSet**: An abstract method that subclasses must implement to convert a `ResultSet` into an entity of type `T`.
> - **count**: A generic method for counting records in any table.
> - **closeResources**: A utility method for safely closing database resources, preventing memory leaks.
> 
> When creating a new DAO for a specific entity (like User or Product), extend the `BaseDAO` class and implement the `mapResultSet` method to define the conversion from `ResultSet` to the desired entity. This structure ensures consistent and efficient data access across the application.

- **StudentDAO.java**: interface

    - `save()` -> Save a student to the database
    - `findStudentById()` -> Finds a student by their unique ID
    - `findAllStudent()` -> Retrieves all students from the database
    - `updateStudent()` -> Updates an existing student's information
    - `deleteStudent()` -> Deletes a student by their ID
    - `searchGeneral()` -> General research with pagination
    - `countSearchGeneral()` -> count results for general research for pagination

    NOTE: 
    - StudentDAO is an interface that defines the contract with all CRUD operations (Create, Read, Update, Delete) and specialized search methods to manipulate student data.

    ```java
    // example
     public interface StudentDAO {
    Student findById(Long id);
    // other methods...
    }
     ```
```
+------------------------------------------------+
|               StudentDAO                       |
+------------------------------------------------+
| + save(Student): void                          |
| + findStudentById(Long): Student               |
| + findAllStudents(): List<Student>             |
| + updateStudent(Student): void                 |
| + deleteStudent(Long): void                    |
| + searchGeneral(SearchCriteria): List<Student> |
| + countSearchGeneral(SearchCriteria): Long     |
+------------------------------------------------+
                    ↑
                implements
                    |
+-------------------------------------------------+
|                  StudentDAOImpl                 |
+-------------------------------------------------+
| - connection: Connection                        |
| - dbConnection: DatabaseConnection              |
+-------------------------------------------------+
| + StudentDAOImpl(Connection)                    |
| + saveStudent(Student): void                    |
| + findStudentById(Long): Student                |
| + findAllStudents(): List<Student>              |
| + updateStudent(Student): void                  |
| + deleteStudent(Long): void                     |
| + searchGeneral(SearchCriteria): List<Student>  |
| + countSearchGeneral(SearchCriteria): Long      |
| + count(): Long                                 |
| - mapResultSetToStudent(ResultSet): Student     |
+-------------------------------------------------+
|                 <<extends>>                     |
|              BaseDAO<Student>                   |
+-------------------------------------------------+
```
- **StudentDAOImpl.java**: `extends BaseDAO<Student>`

    - `StudentDAOImpl(Connection)` -> Constructor with connection injection
    - `saveStudent()` -> Implements student saving to database
    - `findStudentById()` -> Implements finding students by ID
    - `findAllStudent()` -> Implements retrieving all students
    - `updateStudent()` -> Implements student data updating
    - `deleteStudent()` -> Implements student deletion by ID
    - `searchGeneral()` -> Implement general search across all fields with pagination
    - `countSearchGeneral()` -> Implements counts the results of the general search
    - `mapResultSetTo()` -> Convert database ResultSet to Student object

SQL requests example:

```sql
-- Example of searchGeneral() request
SELECT s.*, c.class_name 
FROM students s
LEFT JOIN class c ON s.class_id = c.id
WHERE CAST(s.id AS TEXT) LIKE ? 
   OR LOWER(s.first_name) LIKE LOWER(?) 
   OR LOWER(s.last_name) LIKE LOWER(?)
   OR CAST(s.age AS TEXT) LIKE ?
ORDER BY s.id 
LIMIT ? OFFSET ?

-- Example of countSearchGeneral() request
SELECT COUNT(*) FROM students s
LEFT JOIN class c ON s.class_id = c.id
WHERE (CAST(s.id AS TEXT) LIKE ? 
    OR LOWER(s.first_name) LIKE LOWER(?) 
    OR LOWER(s.last_name) LIKE LOWER(?) 
    OR CAST(s.age AS TEXT) LIKE ?)
```

Snipset example in StudentDAOImpl:

```java
    // Example findStudentById
    public Student findStudentById(Long id) {
        try {
            String sql = "SELECT * FROM students WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            return null;
            
        } catch (SQLException e) {
            return null;
        }
    }
```
- **GradeDAO.java**: interface

    - `saveGrade()` -> Saves a grade to the database
    - `updateGrade()` -> Updates an existing grade by its ID
    - `deleteGrade()` -> Delete an existing gade by its ID
    - `saveCoefficient()` -> Saves a grade coefficient
    - `updateCoefficient()` -> Updates an existing coefficient
    - `searchBySubject()` -> Complete research by subject with pagination
    - `countBySubject()` -> Count for pagination
    - `count()` -> Returns the total number of grades

 ```   
+---------------------------------------------------------------+
|                    GradeDAO                                   |
+---------------------------------------------------------------+
| + saveGrade(Grade): void                                      |
| + updateGrade(Grade): void                                    |
| + deleteGrade(Long): void                                     |
| + saveCoefficient(double): void                               |
| + updateCoefficient(Long, double): void                       |
| + getMinAverageBySubject(String): double                      |
| + getMaximumAverageBySubject(String): double                  |
| + searchBySubject(Long, SearchCriteria): List<SubjectResult>  |
| + countBySubject(Long, String): Long                          |
| + calculateWeightedAverageGrade(Long): double                 |
| + count(): Long                                               |
+---------------------------------------------------------------+
|                  <<interface>>                                |
|                    GradeDAO                                   |
+---------------------------------------------------------------+
                        ↑
                    implements
                        |
+---------------------------------------------------------------+
|                  GradeDAOImpl                                 |
+---------------------------------------------------------------+
| - connection: Connection                                      |
| - dbConnection: DatabaseConnection                            |
+---------------------------------------------------------------+
| + GradeDAOImpl(Connection)                                    |
| + saveGrade(Grade): void                                      |
| + updateGrade(Grade): void                                    |
| + deleteGrade(Long): void                                     |
| + saveCoefficient(double): void                               |
| + updateCoefficient(Long, double): void                       |
| + getMinAverageBySubject(String): double                      |
| + getMaximumAverageBySubject(String): double                  |
| + searchBySubject(Long, SearchCriteria): List<SubjectResult>  |
| + countBySubject(Long, String): Long                          |
| + calculateWeightedAverageGrade(Long): double                 |
| - mapResultSetToGrade(ResultSet): Grade                       |
+---------------------------------------------------------------+
|                  <<extends>>                                  |
|                    BaseDAO<Grade>                             |
+---------------------------------------------------------------+
```

- **GradeDAOImpl.java**: `extends BaseDAO<Grade>`

    - `GradeDAOImpl(Connection)` -> Constructor with connection injection
    - `saveGrade()` -> Implements grade saving to database
    - `updateGrade()` -> Implements grade updating by ID
    - `deleteGrade()` -> Implements grade deletion by ID
    - `saveCoefficient()` -> Implements coefficient saving
    - `updateCoefficient()` -> Implements coefficient updating
    - `searchBySubject()` -> Complex request with GROUP BY to returns all the info in one request (subject - grades - min average grade, max average grade, student average, comment)
    - `countBySubject()` ->  Implements subject count
    - `mapResultSet()` -> Convert database ResultSet to Grade

```sql
-- Example of countBySubject() request
SELECT COUNT(DISTINCT subject_id) FROM grades 
WHERE student_id = ? AND subject_id IN (SELECT id FROM subject WHERE LOWER(name) LIKE LOWER(?))

-- Example of searchBySubject() request 
SELECT 
    s.name AS subject,
    GROUP_CONCAT(g.grade, ', ') as grades,
    (SELECT calculateWeightedAverageGrade(g.student_id, g.subject_id)) as student_average,
    (SELECT MIN(calculateWeightedAverageGrade(student_id, g.subject_id)) 
     FROM (SELECT DISTINCT student_id FROM grades WHERE subject_id = g.subject_id) s) as class_min_average,
    (SELECT MAX(calculateWeightedAverageGrade(student_id, g.subject_id)) 
     FROM (SELECT DISTINCT student_id FROM grades WHERE subject_id = g.subject_id) s) as class_max_average,
    sc.comment as teacher_comment
FROM grades g
LEFT JOIN subject s ON g.subject_id = s.id
LEFT JOIN subject_comments sc ON sc.student_id = g.student_id AND sc.subject_id = g.subject_id
WHERE g.student_id = ? AND LOWER(s.name) LIKE LOWER(?)
GROUP BY s.name, sc.comment
ORDER BY s.name
LIMIT ? OFFSET ?
```

- **SubjectCommentDAO.java**:  interface 

    - `saveComment()` -> Saves a comment for a subject
    - `updateComment()` -> Updates an existing comment
    - `deleteComment()` -> Deletes a comment
    - `findCommentsByStudentAndSubject()` -> Finds comments for student/subject

 ```       
+-----------------------------------------------------------------------+
|                SubjectCommentDAO                                      |
+-----------------------------------------------------------------------+
| + saveComment(SubjectComment): void                                   |
| + updateComment(SubjectComment): void                                 |
| + deleteComment(Long): void                                           |
| + findCommentsByStudentAndSubject(Long, String): List<SubjectComment> |
+-----------------------------------------------------------------------+
|                  <<interface>>                                        |
|                SubjectCommentDAO                                      |
+-----------------------------------------------------------------------+
                        ↑
                    implements
                        |
+-----------------------------------------------------------------------+
|              SubjectCommentDAOImpl                                    |
+-----------------------------------------------------------------------+
| - connection: Connection                                              |
| - dbConnection: DatabaseConnection                                    |
+-----------------------------------------------------------------------+
| + SubjectCommentDAOImpl(Connection)                                   |
| + saveComment(SubjectComment): void                                   |
| + updateComment(SubjectComment): void                                 |
| + deleteComment(Long): void                                           |
| + findCommentsByStudentAndSubject(Long, String): List<SubjectComment> |
| - mapResultSetToComment(ResultSet): SubjectComment                    |
+-----------------------------------------------------------------------+
|                  <<extends>>                                          |
|                BaseDAO<SubjectComment>                                |
+-----------------------------------------------------------------------+
 ```   

- **SubjectCommentDAOImpl.java**: `extends BaseDAO<SubjectComment>`

    - `SubjectCommentDAOImpl(Connection)` -> Constructor with connection injection
    - `saveComment()` -> Implements comment saving to database
    - `updateComment()` -> Implements comment updating
    - `deleteComment()` -> Implements comment deletion
    - `findCommentsByStudentAndSubject()` -> Implements finding comments by student and subject
    - `mapResultSet()` -> Convert database ResultSet to SubjectComment

- **UserDAO.java**: interface

    - `saveUser()` -> Saves a user to the database 
    - `findUserByUsername ()` -> Finds a user by username
    - `authenticateUser()` -> Checks if username/password combination is valid
```
+-------------------------------------------------+
|                    UserDAO                      |
+-------------------------------------------------+
| + saveUser(User): void                          |
| + findUserByUsername(String): User              |
| + authenticateUser(String, String): boolean     |
+-------------------------------------------------+
|                  <<interface>>                  |
|                    UserDAO                      |
+-------------------------------------------------+
                        ↑
                    implements
                        |
+-------------------------------------------------+
|                  UserDAOImpl                    |
+-------------------------------------------------+
| - connection: Connection                        |
| - dbConnection: DatabaseConnection              |
+-------------------------------------------------+
| + UserDAOImpl(Connection)                       |
| + saveUser(User): void                          |
| + findUserByUsername(String): User              |
| + authenticateUser(String, String): boolean     |
| - mapResultSetToUser(ResultSet): User           |
+-------------------------------------------------+
|                  <<extends>>                    |
|                    BaseDAO<User>                |
+-------------------------------------------------+
```
- **UserDAOImpl.java**:  `extends BaseDAO<User>`

    - `UserDAOImpl(Connection)` -> Constructor with connection injection
    - `saveUser()` -> Implements user saving to database
    - `findUserByUsername` -> Implements findinf user by username
    - `authenticateUser()` -> Implements login authentication
    - `mapResultSet()` -> Convert database ResultSet to User

### 3. **Database**
* * *

 - **DatabaseConnection.java**:

    - `url` -> Database UrL string
    - `username` -> Database username
    - `password` -> Database password
    - `connection` -> Active database connection object
    - `DatabaseConnection()` -> Default constructor
    - `connect()` -> Establishes database connection
    - `disconnect()` -> Closes database connection
    - `getConnection()` -> Returns active connection
    - `isConnected()` -> Checks if connection is active
    - `executeQuery()` -> Executes SELECT query
    - `executeUpdate()` -> Executes INSERT/UPDATE/DELETE query
    - `executeSafeQuery()` -> Executes SQL query with parameters for security
    - `closeResources()`
```
+-------------------------------------------------+
|                DatabaseConnection               |
+-------------------------------------------------+
| - url: String                                   |
| - username: String                              |
| - password: String                              |
| - connection: Connection                        |
+-------------------------------------------------+
| + DatabaseConnection()                          |
| + connect(): void                               |
| + disconnect(): void                            |
| + getConnection(): Connection                   |
| + isConnected(): boolean                        |
| + executeQuery(String): ResultSet               |
| + executeUpdate(String): void                   |
| + executeSafeQuery(String, List<Object>): void  |
| + closeResources(): void                        |
+-------------------------------------------------+

```
- **DatabaseConfig.java**:
    - `properties` -> Properties object for configuration
    - `configFile` -> Configuration file path
    - `DatabaseConfig()` -> Default constructor, loads configuration
    - `loadConfig()` -> Loads configuration from properties file
    - `getDatabaseUrl()` -> Returns database URL
    - `getDatabaseUsername()` -> Returns database username
    - `getDatabasePassword()` -> Returns database password

```
+-------------------------------------------------+
|                  DatabaseConfig                 |
+-------------------------------------------------+
| - properties: Properties                        |
| - configFile: String                            |
+-------------------------------------------------+
| + DatabaseConfig()                              |
| + loadConfig(): void                            |
| + getDatabaseUrl(): String                      |
| + getDatabaseUsername(): String                 |
| + getDatabasePassword(): String                 |
+-------------------------------------------------+
```
### 4. **Service Layer (Business Logic)**
* * *
The Service layer contains business logic and orchestrates calls to the DAOs

- **StudentService.java**:

    - `studentDAO` -> StudentDAO instance
    - `validator` -> InputValidator instance
    - `StudentService()` -> Constructor with DAO injection
    - `createStudent()` -> Creates new student with validation
    - `getStudentByID()` -> Retrieves student by ID
    - `getAllStudents()` -> Retrieves all students
    - `updateStudent()` -> Updates existing student
    - `deleteStudent()` -> Deletes student by ID
    - `searchStudents()`  -> Returns paginated student list - use StudentDAO.searchGeneral() with SearchCriteria and pagination

```
+-------------------------------------------------+
|                  StudentService                 |
+-------------------------------------------------+
| - studentDAO: StudentDAO                        |
| - validator: InputValidator                     |
+-------------------------------------------------+
| + StudentService(StudentDAO, InputValidator)    |
| + createStudent(Student): void                  |
| + getStudentByID(Long): Student                 |
| + getAllStudents(): List<Student>               |
| + updateStudent(Student): void                  |
| + deleteStudent(Long): void                     |
| + searchStudents(SearchCriteria): List<Student> |
+-------------------------------------------------+
```
- **GradeService.java**:

    - `gradeDAO` -> GradeDAO instance
    - `GradeService()` -> Constructeur
    - `searchBySubject()` -> Use GradeDAO.searchBySubject() with SearchCriteria and pagination

```
+--------------------------------------------------------------+
|                  GradeService                                |
+--------------------------------------------------------------+
| - gradeDAO: GradeDAO                                         |
+--------------------------------------------------------------+
| + GradeService(GradeDAO)                                     |
| + searchBySubject(Long, SearchCriteria): List<SubjectResult> |
+--------------------------------------------------------------+
```

- **StatisticsService.java**:

    - `studentDAO` -> StudentDAO instance
    - `gradeDAO` -> GradeDAO instance
    - `StatisticsService()` -> Constructor with DAO injection
    - `calculateClassAverageBySubject()` -> Calculates class average by subject
    - `getStudentCountByAgeGroup()` -> Returns student count by age group
    - `getGradeDistributionBySubject()` -> Returns grade distribution ranges by subject
    - `getMinAverageBySubject()` -> Finds minumum average by subject in the student class list 
    - `getMaximumAverageBySubject()` -> Finds maximum average by subject in the student class list 
    - `calculateWeightedAverageGrade` -> Calculates and returns the pondereate (coeff) average of the grades by school subjects
```
+--------------------------------------------------------------------+
|                StatisticsService                                   |
+--------------------------------------------------------------------+
| - studentDAO: StudentDAO                                           |
| - gradeDAO: GradeDAO                                               |
+--------------------------------------------------------------------+
| + StatisticsService(StudentDAO, GradeDAO)                          |
| + calculateClassAverageBySubject(String): double                   |
| + getStudentCountByAgeGroup(): Map<String, Long>                   |
| + getGradeDistributionBySubject(String): Map<String, List<Double>> |
| + getTopPerformers(int): List<Student>                             |
| + getStudentStatistics(): Map<String, Object>                      |
| + getMinAverageBySubject(String): double                           |
| + getMaximumAverageBySubject(String): double                       |
| + calculateWeightedAverageGrade(): double                          |
+--------------------------------------------------------------------+
```
- **AuthenticationService.java**:

    - `userDAO` -> UserDAO instance
    - `currentUser` -> Current logged-in user
    - `AuthenticationService()` -> Constructor with UserDAO injection
    - `authenticate()` -> Authenticates user with login/password
    - `register(User)` -> Registers new user
    - `isAuthenticated()` -> Checks if user is logged in

```
+-------------------------------------------------+
|              AuthenticationService              |
+-------------------------------------------------+
| - userDAO: UserDAO                              |
| - currentUser: User                             |
+-------------------------------------------------+
| + AuthenticationService(UserDAO)                |
| + authenticate(String, String): boolean         |
| + register(User): void                          |
| + isAuthenticated(): boolean                    |
+-------------------------------------------------+
```

- **ImportExportService.java**:

    - `csvHandler` -> CSV file handler
    - `pdfExporter` -> PDF file exporter
    - `ImportExportService()` -> Default constructor
    - `exportToCSV()` -> Exports research result to CSV file
    - `exportToPDF()` -> Exports graphs to PDF file
```
+-------------------------------------------------+
|               ImportExportService               |
+-------------------------------------------------+
| - csvHandler: CSVHandler                        |
| - pdfExporter: PDFExporter                      |
+-------------------------------------------------+
| + ImportExportService()                         |
| + exportToCSV(List<Student>): void              |
| + exportToPDF(): void                           |
+-------------------------------------------------+
```
- **BackupService.java**:

    - `databaseConnection` -> Database connection instance
    - `BackupService(DatabaseConnection)` -> Constructor with connection injection
    - `createBackup()` -> Creates complete system backup
    - `restoreBackup()` -> Restores system from backup
    - `scheduleAutoBackup()` -> Schedules periodic automatic backup
    - `listBackups()` -> Lists all available backups
    - `deleteBackup()` -> Deletes specific backup

```
+-------------------------------------------------+
|                BackupService                    |
+-------------------------------------------------+
| - databaseConnection: DatabaseConnection        |
| - studentDAO: StudentDAO                        |
+-------------------------------------------------+
| + BackupService(DatabaseConnection, StudentDAO) |
| + createBackup(): void                          |
| + restoreBackup(): void                         |
| + scheduleAutoBackup(): void                    |
| + stopAutoBackup(): void                        |
| + listBackups(): List<String>                   |
| + deleteBackup(String): void                    |
| - compressBackup(String): void                  |
| - extractBackup(String): void                   |
+-------------------------------------------------+
```

### 5. **Controller Layer** 
* * *

- **BaseTableController.java**:

    - `searchField` -> TextField instance for search input
    - `searchButton` -> Button for executing search
    - `exportButton` -> Button for exporting data
    - `importButton` -> Button for importing data
    - `dataTable` -> TableView instance for displaying data
    - `pagination` -> Pagination instance for navigating through data pages
    - `importExportService` -> ImportExportService instance
    - `ROWS_PER_PAGE` -> Constant for the number of rows per page
    - `initialize()` -> Common initialization for all table controllers
    - `initializeServices()` -> Initializes required services
    - `setupTableResizing()` -> Sets up table auto-resizing
    - `refreshTableRowHeights()` -> Forces height recalculation for table rows
    - `autoResizeTable()` -> Automatically resizes the table
    - `setupColumnSizing()` -> Configures column sizing (abstract)
    - `setupPagination()` -> Sets up pagination functionality
    - `setupSearchAndExport()` -> Sets up search and export button actions
    - `setupImport()` -> Sets up import button action
    - `setupColumnSorting()` -> Configures column sorting
    - `loadInitialData()` -> Loads initial data into the table
    - `handleSearch()` -> Handles search event and loads data
    - `handleExport()` -> Handles CSV export functionality
    - `loadDataPage()` -> Loads the specified page of data into the table
    - `calculatePageCount()` -> Calculates the total number of pages
    - `createSearchCriteria()` -> Creates search criteria based on user input
    - `extractSortField()` -> Extracts sort field name from a column
    - `refreshTable()` -> Refreshes the current table
    - `refreshTableFromStart()` -> Refreshes and returns to the first page
    - `setupTableColumns()` -> Abstract method to set up table columns (to be implemented in subclasses)
    - `searchData()` -> Abstract method to search data based on criteria (to be implemented in subclasses)
    - `exportToCSV()` -> Abstract method to export data to CSV (to be implemented in subclasses)
    - `getTotalCount()` -> Abstract method to get total count of items based on criteria (to be implemented in subclasses)
    - `handleImport()` -> Abstract method to handle import functionality (to be implemented in subclasses)
    - `ImportExportService()` -> Sets the ImportExportService for the controller

```
+----------------------------------------------------------------------+
|              BaseTableController<T>                                  |
+----------------------------------------------------------------------+
| - searchField: TextField                                             |
| - searchButton: Button                                               |
| - exportButton: Button                                               |
| - importButton: Button                                               |
| - dataTable: TableView<T>                                            |
| - pagination: Pagination                                             |
| - importExportService: ImportExportService                           |
| - ROWS_PER_PAGE: int                                                 |
+----------------------------------------------------------------------+
| - initialize(): void                                                 |
| - initializeServices(): void                                         |
| - setupTableResizing(): void                                         |
| - refreshTableRowHeights(): void                                     |
| - autoResizeTable(): void                                            |
| - setupColumnSizing(): void                                          |
| - setupPagination(): void                                            |
| - setupSearchAndExport(): void                                       |
| - setupImport(): void                                                |
| - setupColumnSorting(): void                                         |
| - loadInitialData(): void                                            |
| - handleSearch(): void                                               |
| - handleExport(): void                                               |
| - loadDataPage(int pageIndex): void                                  |
| - calculatePageCount(): int                                          |
| - createSearchCriteria(): SearchCriteria                             |
| - extractSortField(TableColumn<T, ?> column): String                 |
| - refreshTable(): void                                               |
| - refreshTableFromStart(): void                                      |
| - setupTableColumns(): void (abstract)                               |
| - searchData(SearchCriteria criteria): List<T>                       |
| - exportToCSV(List<T> data, File file): void                         |
| - getTotalCount(SearchCriteria criteria): int                        |
| - handleImport(): void (abstract)                                    |
| - ImportExportService(ImportExportService importExportService): void |
+----------------------------------------------------------------------+
```

- **TabController.java**:

    - `initialize()` -> Initializes the tab controller
    - `handleButtonAction()` -> Handles button click action

```
+-------------------------------------------------+
|                TabController                    |
+-------------------------------------------------+
| + initialize(URL, ResourceBundle): void         |
| + handleButtonAction(): void                    |
+-------------------------------------------------+
```

- **LoginController.java**:

    - `authService` -> AuthenticationService instance
    - `textFieldUsername` -> TextField instance
    - `textFieldPassword` -> TextField instance
    - `buttonLogin` -> Button instance
    - `buttonRegister` -> Button instance
    - `initialize()` -> Initializes the authentication controller
    - `handleLogin()` -> Handles login event
    - `handleRegister()` -> Handles registration event
    - `showRegisterView()` -> Displays registration view
    - `showMainView()` -> Displays main view after successful login
    - `clearForm()` -> Clears input fields in the login form
```
+-------------------------------------------------+
|              LoginController                    |
+-------------------------------------------------+
| - authService: AuthenticationService            |
| - textFieldUsername: TextField                  |
| - textFieldPassword: TextField                  |
| - buttonLogin: Button                           |
| - buttonRegister: Button                        |
+-------------------------------------------------+
| + initialize(): void                            |
| + handleLogin(): void                           |
| + handleRegister(): void                        |
| + showRegisterView(): void                      |
| + showMainView(): void                          |
| + clearForm(): void                             |
+-------------------------------------------------+
```

- **RegisterController.java**:

    - `authService` -> AuthenticationService instance
    - `textFieldUsername` -> TextField instance
    - `textFieldPassword` -> TextField instance
    - `textFieldConfirmPassword` -> TextField instance
    - `buttonRegister` -> Button instance
    - `buttonBackToLogin` -> Button instance
    - `initialize()` -> Initializes the registration controller
    - `handleRegister()` -> Handles registration event
    - `handleBackToLogin()` -> Handles event to return to login page
    - `showLogin()` -> Displays login view after successful registration
    - `clearForm()` -> Clears input fields in the registration form
    - `setAuthService()` -> Sets the AuthenticationService instance for the controller 

```
+-------------------------------------------------+
|                RegisterController               |
+-------------------------------------------------+
| - authService: AuthenticationService            |
| - textFieldUsername: TextField                  |
| - textFieldPassword: TextField                  |
| - textFieldConfirmPassword: TextField           |
| - buttonRegister: Button                        |
| - buttonBackToLogin: Button                     |
+-------------------------------------------------+
| + initialize(): void                            |
| + handleRegister(): void                        |
| + handleBackToLogin(): void                     |
| + showLogin(): void                             |
| + clearForm(): void                             |
| + setAuthService(AuthenticationService): void   |
+-------------------------------------------------+
```
- **WelcomeController.java**:

    - `usernameLabel` -> Label for displaying the username
    - `currentUser` -> User object representing the current user
    - `initialize()` -> Initializes the controller
    - `setCurrentUser()` -> Sets the current user and updates the UI
    - `updateUI()` -> Updates UI components based on the current user's information

```
+------------------------------------------------------------+
|                   WelcomeController                        |
+------------------------------------------------------------+
| - usernameLabel: Label                                     |
| - currentUser: User                                        |
| - initialize(URL location, ResourceBundle resources): void |
| - setCurrentUser(User user): void                          |
| - updateUI(): void                                         |
+------------------------------------------------------------+

```

- **StudentsController.java**:

    - `studentService` -> StudentService instance
    - `importExportService` -> ImportExportService instance
    - `searchField` -> TextField instance for search
    - `studentTable` -> TableView instance for students list
    - `pagination` -> Pagination instance
    - `firstNameField`, `lastNameField`, `ageField`, `classNameField` -> TextField instances for adding students
    - `initialize()` -> Initializes the students controller and table
    - `handleSearch()` -> Handles search event and filters students
    - `handleImport()` -> Handles importing students from CSV file
    - `handleExport()` -> Handles exporting students to CSV file
    - `handleAddStudent()` -> Handles adding a new student
    - `loadStudentsPage()` -> Loads a specific page of students with search filter
    - `calculatePageCount()` -> Calculates total number of pages
    - `showEditDialog()` -> Displays a dialog to edit student information
    - `showDeleteConfirmation()` -> Displays confirmation before deleting a student
    - `setupEditColumn()` -> Configures the edit column with buttons
    - `setupDeleteColumn()` -> Configures the delete column with buttons
    - `setupColumnSorting()` -> Sets up column sorting functionality

```
+-------------------------------------------------------+
|              StudentsController                       |
+-------------------------------------------------------+
| - studentService: StudentService                      |
| - importExportService: ImportExportService            |
| - searchField: TextField                              |
| - studentTable: TableView<Student>                    |
| - pagination: Pagination                              |
| - firstNameField: TextField                           |
| - lastNameField: TextField                            |
| - ageField: TextField                                 |
| - classNameField: TextField                           |
| - ROWS_PER_PAGE: int                                  |
+-------------------------------------------------------+
| + initialize(): void                                  |
| + handleSearch(): void                                |
| + handleImport(): void                                |
| + handleExport(): void                                |
| + handleAddStudent(): void                            |
| - loadStudentsPage(int): void                         |
| - calculatePageCount(): int                           |
| - showEditDialog(Student): void                       |
| - showDeleteConfirmation(Student): void               |
| - setupEditColumn(): void                             |
| - setupDeleteColumn(): void                           |
| - setupColumnSorting(): void                          |
| - setupTableColumns(): void                           |
| - clearAddForm(): void                                |
| + setStudentService(StudentService): void             |
| + setImportExportService(ImportExportService): void   |
+-------------------------------------------------------+
```

- **StudentsStatsController.java**:

    - `ageChart` -> PieChart for displaying age distribution
    - `noDataLabel` -> Label for displaying no data message
    - `refreshButton` -> Button for refreshing the statistics
    - `exportButton` -> Button for exporting the statistics
    - `statisticsService` -> StatisticsService instance for handling statistics
    - `initialize()` -> Initializes the controller
    - `loadStatistics()` -> Loads statistical data and updates the chart
    - `updateAgeDistributionChart()` -> Updates the pie chart with age distribution
    - `handleRefresh()` -> Handles the click event for the refresh button
    - `handleExport()` -> Handles the click event for the export button
    - `setStatisticsService()` -> Sets the StatisticsService for the controller and loads statistics

```
+-------------------------------------------------------------------+
|                  StudentsStatsController                          |
+-------------------------------------------------------------------+
| - ageChart: PieChart                                              |
| - noDataLabel: Label                                              |
| - refreshButton: Button                                           |
| - exportButton: Button                                            |
| - statisticsService: StatisticsService                            |
+-------------------------------------------------------------------+
| - initialize(URL location, ResourceBundle resources): void        |
| - loadStatistics(): void                                          |
| - updateAgeDistributionChart(): void                              |
| - handleRefresh(): void                                           |
| - handleExport(): void                                            |
| - setStatisticsService(StatisticsService statisticsService): void |
+-------------------------------------------------------------------+

```

- **StudentController.java**:

    - `studentIdField` -> TextField for the student ID
    - `studentNameLabel` -> Label displaying the student's name
    - `studentClassLabel` -> Label displaying the student's class
    - `subjectComboBox` -> ComboBox for selecting the subject
    - `gradeField` -> TextField for entering the grade
    - `coefficientField` -> TextField for entering the coefficient
    - `addGradeButton` -> Button to add a grade
    - `commentArea` -> TextArea for entering comments
    - `addCommentButton` -> Button to add a comment
    - `okButton` -> Button for confirmation actions
    - `searchButton` -> Button to search for grades
    - `gradesTable` -> TableView displaying the grades
    - `subjectColumn` -> Column for subject names
    - `gradesColumn` -> Column for grades
    - `minAverageColumn` -> Column for minimum class average
    - `maxAverageColumn` -> Column for maximum class average
    - `studentAverageColumn` -> Column for student's average
    - `commentsColumn` -> Column for comments
    - `studentService` -> Service for student data
    - `gradeService` -> Service for grade data
    - `commentService` -> Service for comment data
    - `currentStudent` -> The currently loaded student
    - `subjects` -> List of subjects available for selection
    - `StudentController()` -> Constructor
    - `initialize()` -> Initializes the controller
    - `setupColumnSizing()` -> Sets up specific sizing for the table columns
    - `setupTableColumns()` -> Configures the table columns
    - `handleLoadStudent()` -> Loads a student based on the ID entered
    - `handleAddGrade()` -> Adds a grade for the current student
    - `handleAddComment()` -> Adds a comment for the current student
    - `handleSort()` -> Handles sorting action
    - `showGradeEditDialog()` -> Displays a dialog to edit a grade
    - `showGradeDeleteConfirmation()` -> Displays a confirmation dialog for grade deletion
    - `showCommentEditDialog()` -> Displays a dialog to edit a comment
    - `showCommentDeleteConfirmation()` -> Displays a confirmation dialog for comment deletion
    - `disableGradeControls()` -> Enables or disables grade input controls
    - `searchData()` -> Searches data based on criteria
    - `getTotalCount()` -> Gets the total count of records based on criteria
    - `exportToCSV()` -> Exports data to CSV format
    - `handleImport()` -> Handles import action (not implemented)
    - `setStudentService()` -> Sets the StudentService instance
    - `setGradeService()` -> Sets the GradeService instance
    - `setCommentService()` -> Sets the SubjectCommentService instance

```
+-----------------------------------------------------------+
|              StudentController                            |
+-----------------------------------------------------------+
| - studentIdField: TextField                               |
| - studentNameLabel: Label                                 |
| - studentClassLabel: Label                                |
| - subjectComboBox: ComboBox<String>                       |
| - gradeField: TextField                                   |
| - coefficientField: TextField                             |
| - addGradeButton: Button                                  |
| - commentArea: TextArea                                   |
| - addCommentButton: Button                                |
| - okButton: Button                                        |
| - searchButton: Button                                    |
| - gradesTable: TableView<SubjectResult>                   |
| - subjectColumn: TableColumn<SubjectResult, String>       |
| - gradesColumn: TableColumn<SubjectResult, String>        |
| - minAverageColumn: TableColumn<SubjectResult, Number>    |
| - maxAverageColumn: TableColumn<SubjectResult, Number>    |
| - studentAverageColumn: TableColumn<SubjectResult, Number>|
| - commentsColumn: TableColumn<SubjectResult, String>      |
| - studentService: StudentService                          |
| - gradeService: GradeService                              |
| - commentService: SubjectCommentService                   |
| - currentStudent: Student                                 |
| - subjects: List<String>                                  |
+-----------------------------------------------------------+
| + initialize(): void                                      |
| - setupColumnSizing(): void                               |
| - setupTableColumns(): void                               |
| + handleLoadStudent(): void                               |
| + handleAddGrade(): void                                  |
| + handleAddComment(): void                                |
| + handleSort(): void                                      |
| - showGradeEditDialog(String, String): void               |
| - showGradeDeleteConfirmation(String, String): void       |
| - showCommentEditDialog(String, String): void             |
| - showCommentDeleteConfirmation(String): void             |
| - disableGradeControls(boolean): void                     |
| - searchData(SearchCriteria): List<SubjectResult>         |
| - getTotalCount(SearchCriteria): int                      |
| - exportToCSV(List<SubjectResult>, File): void            |
| + handleImport(): void                                    |
| + setStudentService(StudentService): void                 |
| + setGradeService(GradeService): void                     |
| + setCommentService(SubjectCommentService): void          |
+-----------------------------------------------------------+
```

- **StudentStatsController.java**:

    - `studentNameLabel` -> Label for displaying the student's name
    - `subjectComboBox` -> ComboBox for selecting subjects
    - `gradeChart` -> LineChart for displaying grades over time
    - `dateAxis` -> CategoryAxis for dates on the x-axis
    - `gradeAxis` -> NumberAxis for grades on the y-axis
    - `noDataLabel` -> Label for displaying no data message
    - `refreshButton` -> Button for refreshing the statistics
    - `exportButton` -> Button for exporting the statistics
    - `subjects` -> List of subjects available for selection
    - `statisticsService` -> StatisticsService instance for handling statistics
    - `currentStudent` -> Student object representing the current student
    - `initialize()` -> Initializes the controller and sets up the UI
    - `setCurrentStudent()` -> Sets the current student and updates the UI
    - `updateGradeChart()` -> Updates the grade chart for the selected subject
    - `handleRefresh()` -> Handles the click event for the refresh button
    - `handleExport()` -> Handles the click event for the export button
    - `setStatisticsService()` -> Sets the StatisticsService for the controller

```
+-------------------------------------------------------------------+
|                   StudentStatsController                          |
+-------------------------------------------------------------------+
| - studentNameLabel: Label                                         |
| - subjectComboBox: ComboBox<String>                               |
| - gradeChart: LineChart<String, Number>                           |
| - dateAxis: CategoryAxis                                          |
| - gradeAxis: NumberAxis                                           |
| - noDataLabel: Label                                              |
| - refreshButton: Button                                           |
| - exportButton: Button                                            |
| - subjects: List<String>                                          |
| - statisticsService: StatisticsService                            |
| - currentStudent: Student                                         |
+-------------------------------------------------------------------+
| - initialize(URL location, ResourceBundle resources): void        |
| - setCurrentStudent(Student student): void                        |
| - updateGradeChart(String subject): void                          |
| - handleRefresh(): void                                           |
| - handleExport(): void                                            |
| - setStatisticsService(StatisticsService statisticsService): void |
+-------------------------------------------------------------------+
```

- **BackupController.java**:

    - `backupService` -> BackupService instance
    - `handleCreateBackup()` -> Handles backup creation
    - `handleRestoreBackup()` -> Handles backup restoration
    - `handleAutoBackupSchedule()` -> Manages auto-backup settings
    - `showBackupList()` -> Displays available backups
    - `handleDeleteBackup()` -> Handles backup deletion
```
+-------------------------------------------------+
|                BackupController                 |
+-------------------------------------------------+
| - backupService: BackupService                  |
+-------------------------------------------------+
| + handleCreateBackup(): void                    |
| + handleRestoreBackup(): void                   |
| + handleAutoBackupSchedule(): void              |
| + showBackupList(): void                        |
| + handleDeleteBackup(): void                    |
+-------------------------------------------------+
```

### 6. **Utils Layer**
* * *

- **SearchCriteria.java**:

    - `searchValue` -> Value typed in the search bar
    - `pageNumber` -> Page number (default: 1)
    - `pageSize` -> Page size (default: 15)
    - `sortField` -> Field used for sorting
    - `sortDirection` -> Direction of sorting (ascending or descending)
    - `SearchCriteria()` -> Default constructor
    - `SearchCriteria()` -> Constructor with searchValue
    - `getSearchValue()` -> Returns the search value
    - `setSearchValue()` -> Sets the search value
    - `getPageNumber()` -> Returns the page number
    - `setPageNumber()` -> Sets the page number
    - `getPageSize()` -> Returns the page size
    - `setPageSize()` -> Sets the page size
    - `getSortField()` -> Returns the sort field
    - `setSortField()` -> Sets the sort field
    - `getSortDirection()` -> Returns the sort direction
    - `setSortDirection()` -> Sets the sort direction
    - `getOffset()` -> Calculates the offset
    - `toString()` -> String representation
    - `equals()` -> Checks for equality with another object
    - `hashCode()` -> Generates a hash code for the SearchCriteria
```
+-------------------------------------------------+
|                SearchCriteria                   |
+-------------------------------------------------+
| - searchValue: String                           |
| - pageNumber: int                               |
| - pageSize: int                                 |
| - sortField: String                             |
| - sortDirection: String                         |
+-------------------------------------------------+
| + SearchCriteria()                              |
| + SearchCriteria(String): void                  |
| + getSearchValue(): String                      |
| + setSearchValue(String): void                  |
| + getPageNumber(): int                          |
| + setPageNumber(int): void                      |
| + getPageSize(): int                            |
| + setPageSize(int): void                        |
| + getSortField(): String                        |
| + setSortField(String): void                    |
| + getSortDirection(): String                    |
| + setSortDirection(String): void                |
| + getOffset(): int                              |
| + toString(): String                            |
| + equals(Object): boolean                       |
| + hashCode(): int                               |
+-------------------------------------------------+
```

- **SubjectResult.java**:

    - `subject` -> Subject
    - `grades` -> List of grades (String format "grade1, grade2, grade3...")
    - `studentAverage` -> Student's average
    - `classMinAverage` -> Minimum class average
    - `classMaxAverage` -> Maximum class average
    - `teacherComment` -> Teacher's comment
    - `SubjectResult()` -> Default constructor
    - `SubjectResult()` -> Constructor with all parameters
    - `getSubject()` -> Returns the subject
    - `setSubject()` -> Sets the subject
    - `getGrades()` -> Returns the formatted grades
    - `setGrades()` -> Sets the formatted grades
    - `getStudentAverage()` -> Returns the student's average
    - `setStudentAverage()` -> Sets the student's average
    - `getClassMinAverage()` -> Returns the minimum class average
    - `setClassMinAverage()` -> Sets the minimum class average
    - `getClassMaxAverage()` -> Returns the maximum class average
    - `setClassMaxAverage()` -> Sets the maximum class average
    - `getTeacherComment()` -> Returns the teacher's comment
    - `setTeacherComment()` -> Sets the teacher's comment
    - `toString()` -> String representation
```
+-------------------------------------------------+
|                SubjectResult                    |
+-------------------------------------------------+
| - subject: String                               |
| - grades: String                                |
| - studentAverage: double                        |
| - classMinAverage: double                       |
| - classMaxAverage: double                       |
| - teacherComment: String                        |
+-------------------------------------------------+
| + SubjectResult()                               |
| + SubjectResult(String, String, double,         |
|                   double, double, String): void |
| + getSubject(): String                          |
| + setSubject(String): void                      |
| + getGrades(): String                           |
| + setGrades(String): void                       |
| + getStudentAverage(): double                   |
| + setStudentAverage(double): void               |
| + getClassMinAverage(): double                  |
| + setClassMinAverage(double): void              |
| + getClassMaxAverage(): double                  |
| + setClassMaxAverage(double): void              |
| + getTeacherComment(): String                   |
| + setTeacherComment(String): void               |
| + toString(): String                            |
+-------------------------------------------------+
```
-  **AlertUtils.java**:

    - `CSS_FILE` -> Path to the CSS file for custom styling
    - `applyCustomStyle()` -> Applies custom styling to the specified alert dialog
    - `showAlert()` -> Displays an alert dialog with the specified title and message
    - `showError()` -> Displays an error alert dialog with the specified title and message
    - `showWarning()` -> Displays a warning alert dialog with the specified title and message
    - `showInformation()` -> Displays an information alert dialog with the specified title and message

```
+--------------------------------------------------+
|                  AlertUtils                      |
+--------------------------------------------------+
| - CSS_FILE: String                               |
+--------------------------------------------------+
| + applyCustomStyle(Alert): void                  |
| + showAlert(String, String): void                |
| + showError(String, String): void                |
| + showWarning(String, String): void              |
| + showInformation(String, String): void          |
+--------------------------------------------------+

```

- **DialogUtils.java**:

    - `DialogField` -> Represents an input field in a dialog window
    - `label` -> Label for the dialog field
    - `textField` -> TextField for user input
    - `initialValue` -> Initial value for the text field
    - `DialogField()` -> Constructor to create a dialog field
    - `getLabel()` -> Returns the label of the dialog field
    - `getTextField()` -> Returns the text field
    - `getValue()` -> Returns the trimmed text from the text field
    - `setValue()` -> Sets the value of the text field
    - `focus()` -> Focuses on the text field and selects all its text
    - `TextAreaField` -> Represents a text area in a dialog window
    - `label` -> Label for the text area field
    - `textArea` -> TextArea for user input
    - `initialValue` -> Initial value for the text area
    - `TextAreaField()` -> Constructor to create a text area field
    - `getLabel()` -> Returns the label of the text area field
    - `getTextArea()` -> Returns the text area
    - `getValue()` -> Returns the trimmed text from the text area
    - `setValue()` -> Sets the value of the text area
    - `focus()` -> Focuses on the text area and selects all its text
    - `showEditDialog()` -> Creates a generic edit dialog window with custom validation
    - `showTextAreaDialog()` -> Shows a dialog window containing a TextArea for user input
    - `showDeleteConfirmation()` -> Shows a delete confirmation window
    - `createActionColumn()` -> Creates a table column that contains action buttons for each row
    - `setupColumnSorting()` -> Sets up sorting for the columns of the TableView

```
+-------------------------------------------------------------------------+
|                  DialogUtils                                            |
+-------------------------------------------------------------------------+
| - DialogField                                                           |
|   - label: String                                                       |
|   - textField: TextField                                                |
|   - initialValue: String                                                |
| + DialogField(String, String): void                                     |
| + getLabel(): String                                                    |
| + getTextField(): TextField                                             |
| + getValue(): String                                                    |
| + setValue(String): void                                                |
| + focus(): void                                                         |
| - TextAreaField                                                         |
|   - label: String                                                       |
|   - textArea: TextArea                                                  |
|   - initialValue: String                                                |
| + TextAreaField(String, String): void                                   |
| + getLabel(): String                                                    |
| + getTextArea(): TextArea                                               |
| + getValue(): String                                                    |
| + setValue(String): void                                                |
| + focus(): void                                                         |
| + showEditDialog(String, List<DialogField>,                             |
|        Consumer<List<DialogField>>,                                     |
|        java.util.function.Function<List<DialogField>, String>): void    |
| + showTextAreaDialog(String, TextAreaField, Button...): void            |
| + showDeleteConfirmation(String, String, Runnable): void                |
| + createActionColumn(String, String, Consumer<T>): TableColumn<T, Void> |
| + setupColumnSorting(TableView<?>, Runnable): void                      |
+-------------------------------------------------------------------------+
```
- **GradeValidator.java**:

    - `ValidationResult` -> Represents the result of a validation check
    - `valid` -> Indicates if the validation passed
    - `errorMessage` -> The error message if validation failed
    - `focusField` -> The field to focus on if validation failed
    - `ValidationResult()` -> Constructor for creating a validation result
    - `isValid()` -> Checks if the validation passed
    - `getErrorMessage()` -> Gets the error message
    - `getFocusField()` -> Gets the field that should receive focus
    - `validateGradeInput()` -> Validates the input for grade and coefficient fields
    - `checkForChanges()` -> Checks if the grade or coefficient values have changed
    - `createGradeFromFields()` -> Creates a Grade object from the validated fields
    - `createCommentFromField()` -> Creates a SubjectComment object from the validated field

```
+---------------------------------------------------------------------------+
|                    GradeValidator                                         |
+---------------------------------------------------------------------------+
| - ValidationResult                                                        |
|   - valid: boolean                                                        |
|   - errorMessage: String                                                  |
|   - focusField: Object                                                    |
| + ValidationResult(boolean, String, Object): void                         |
| + isValid(): boolean                                                      |
| + getErrorMessage(): String                                               |
| + getFocusField(): Object                                                 |
| + validateGradeInput(TextField, TextField): ValidationResult              |
| + checkForChanges(double, double, TextField, TextField): ValidationResult |
| + createGradeFromFields(Long, String, TextField, TextField): Grade        |
| + createCommentFromField(Long, String, TextArea): SubjectComment          |
+---------------------------------------------------------------------------+

```
- **PasswordUtils.java**:

    - `generateSalt()` -> Generates random salt
    - `hashPassword()` -> Hashes password with salt
    - `hashPasswordWithSalt()` -> Hashes password with auto-generated salt
    - `verifyPassword()` -> Verifies if password matches hash
    - `validatePasswordStrength()` -> Validates password strength
    - `getPasswordCriteria()` -> Returns validation criteria
```
+-------------------------------------------------+
|                PasswordUtils                    |
+-------------------------------------------------+
| + generateSalt(): String                        |
| + hashPassword(String, String): String          |
| + hashPasswordWithSalt(String): String          |
| + verifyPassword(String, String): boolean       |
| + validatePasswordStrength(String): boolean     |
| + getPasswordCriteria(): String                 |
+-------------------------------------------------+
```
- **CSVHandler.java**:

    - `delimiter` -> CSV delimiter character (default: comma)
    - `CSVHandler()` -> Default constructor with standard delimiters
    - `CSVHandler()` -> Constructor with custom delimiters
    - `exportGeneralSearchResults()` -> Exports general search results list to CSV file
    - `exportStudentSearchResults()` -> Exports grade list to CSV file
    - `validateCSVFormat(S)` -> Validates CSV file format
    - `parseStudentFromCSV()` -> Parses CSV line to Student object
    - `formatStudentToCSV()` -> Formats Student object to CSV line
    - `parseGeneralFromCSV()` -> Parses CSV line to general object
    - `formatGeneralToCSV()` -> Formats general object to CSV line
```
+-------------------------------------------------+
|                CSVHandler                       |
+-------------------------------------------------+
| - delimiter: char                               |
+-------------------------------------------------+
| + CSVHandler()                                  |
| + CSVHandler(char): void                        |
| + exportGeneralSearchResults(List): void        |
| + exportStudentSearchResults(List): void        |
| + validateCSVFormat(String): boolean            |
| + parseStudentFromCSV(String): Student          |
| + formatStudentToCSV(Student): String           |
| + parseGeneralFromCSV(String): General          |
| + formatGeneralToCSV(General): String           |
+-------------------------------------------------+
```
- **PDFExporter.java**:

    - `document` -> Document instance
    - `writer` -> PdfWriter instance
    - `PDFExporter()` -> Default constructor, initializes PDF components
    - `exportStatisticsGeneralGraph()` -> Exports general statistics graph to PDF file
    - `exportStatisticsStudentGraph()` -> Exports student statistics graph to PDF file
    - `formatStudentData()` -> Formats student data for PDF
    - `addHeader()` -> Adds header to PDF document
    - `addFooter()` -> Adds footer to PDF document

```
+-------------------------------------------------+
|                PDFExporter                      |
+-------------------------------------------------+
| - document: Document                            |
| - writer: PdfWriter                             |
+-------------------------------------------------+
| + PDFExporter()                                 |
| + exportStatisticsGeneralGraph(): void          |
| + exportStatisticsStudentGraph(): void          |
| + formatStudentData(Student): void              |
| + addHeader(): void                             |
| + addFooter(): void                             |
+-------------------------------------------------+
```

- **SceneUtils.java**:

    - `changeScene()` -> Changes the current scene of the given stage to a new FXML view
    - `<T>` -> The type of the controller associated with the FXML
    - `stage` -> The stage to change the scene for
    - `fxmlPath` -> The path to the FXML file
    - `title` -> The title to set for the stage
    - `return` -> The controller associated with the loaded FXML
    - `throws Exception` -> If loading the FXML fails

```
+--------------------------------------------------+
|                    SceneUtils                    |
+--------------------------------------------------+
| + changeScene(Stage, String, String): T          |
+--------------------------------------------------+

```

- **StudentValidator.java**:

    - `ValidationResult` -> Represents the result of a validation check
    - `valid` -> Indicates if the validation passed
    - `errorMessage` -> The error message if validation failed
    - `focusField` -> The TextField that should receive focus if validation fails
    - `ValidationResult()` -> Constructor for creating a validation result
    - `isValid()` -> Checks if the validation passed
    - `getErrorMessage()` -> Gets the error message
    - `getFocusField()` -> Gets the TextField that should receive focus
    - `validateForCreation()` -> Validates the field for creating a new student
    - `validateAndApplyChanges()` -> Validates and applies changes to the student object based on the provided fields
    - `validateFirstName()` -> Validates the first name input
    - `validateLastName()` -> Validates the last name input
    - `validateAge()` -> Validates the age input
    - `validateClassName()` -> Validates the class name input
    - `formatName()` -> Formats the name by capitalizing the first letter and correcting spacing around hyphens
    - `formatClassName()` -> Formats the class name by correcting spacing and capitalization
    - `createStudentFromFields()` -> Creates a Student object from the provided TextFields

```
+----------------------------------------------------------+
|                StudentValidator                          |
+----------------------------------------------------------+
| - ValidationResult                                       |
|   - valid: boolean                                       |
|   - errorMessage: String                                 |
|   - focusField: TextField                                |
| + ValidationResult(boolean, String, TextField): void     |
| + isValid(): boolean                                     |
| + getErrorMessage(): String                              |
| + getFocusField(): TextField                             |
+----------------------------------------------------------+
| + validateForCreation                                    |
|       (TextField, TextField, TextField, TextField):      | 
|                                         ValidationResult |
| + validateAndApplyChanges                                |
|       (Student, TextField, TextField, TextField,         |
|                             TextField): ValidationResult |
| + validateFirstName(String, TextField): ValidationResult |
| + validateLastName(String, TextField): ValidationResult  |
| + validateAge(String, TextField): ValidationResult       |
| + validateClassName(String, TextField): ValidationResult |
| + formatName(String): String                             |
| + formatClassName(String): String                        |
| + createStudentFromFields(TextField, TextField,          |
|                           TextField, TextField): Student |
+----------------------------------------------------------+

```
### **Main**

- **Main.java**:

    - `start()` -> Entry point for the JavaFX application
    - Loads the initial FXML view ()
    - Sets the application icon
    - Configures the main scene with CSS
    - Sets the title of the main window and shows the stage
    - `main()` -> The main method to launch the JavaFX application

```
+--------------------------------------------------+
|                     Main                         |
+--------------------------------------------------+
| + start(Stage): void                             |
| + main(String[]): void                           |
+--------------------------------------------------+

```
### UML Class Diagram 

```mermaid
classDiagram
    class Student {
        +Long studentId
        +String firstName
        +String lastName
        +int age
        +String className
        +double averageGrade
        +Student()
        +Student(String, String, int)
        +Long getStudentId()
        +String getFirstName()
        +void setFirstName(String)
        +String getLastName()
        +void setLastName(String)
        +double getAverageGrade()
        +void setAverageGrade(double)
        +int getAge()
        +void setAge(int)
        +String getFullName()
        +String getStudentClassName()
        +void setClassName(String)
        +String toString()
    }

    class Grade {
        +Long id
        +Long studentId
        +String subject
        +double value
        +double coefficient
        +Date date
        +Grade()
        +Grade(Long, String, double, double)
        +Long getGradeId()
        +Long getStudentId()
        +void setStudentId(Long)
        +String getSubject()
        +void setSubject(String)
        +double getValue()
        +void setValue(double)
        +double getCoefficient()
        +void setCoefficient(double)
        +Date getDate()
        +void setDate(Date)
        +double getWeightedGradeValue()
        +String toString()
    }

    class SubjectComment {
        +Long id
        +Long studentId
        +String subject
        +String comment
        +SubjectComment()
        +Long getId()
        +Long getStudentId()
        +String getSubject()
        +String getComment()
        +void setComment(String)
        +String toString()
    }

    class User {
        +Long id
        +String username
        +String passwordHash
        +User()
        +User(String, String)
        +Long getUserId()
        +String getUsername()
        +void setUsername(String)
        +String getPasswordHash()
        +void setPasswordHash(String)
        +String toString()
    }

    class SearchCriteria {
        +String searchValue
        +int pageNumber
        +int pageSize
        +String sortField
        +String sortDirection
        +SearchCriteria()
        +SearchCriteria(String)
        +String getSearchValue()
        +void setSearchValue(String)
        +int getPageNumber()
        +void setPageNumber(int)
        +int getPageSize()
        +void setPageSize(int)
        +String getSortField()
        +void setSortField(String)
        +String getSortDirection()
        +void setSortDirection(String)
        +int getOffset()
        +String toString()
        +boolean equals(Object)
        +int hashCode()
    }

    class SubjectResult {
        +String subject
        +String grades
        +double studentAverage
        +double classMinAverage
        +double classMaxAverage
        +String teacherComment
        +SubjectResult()
        +SubjectResult(String, String, double, double, double, String)
        +String getSubject()
        +void setSubject(String)
        +String getGrades()
        +void setGrades(String)
        +double getStudentAverage()
        +void setStudentAverage(double)
        +double getClassMinAverage()
        +void setClassMinAverage(double)
        +double getClassMaxAverage()
        +void setClassMaxAverage(double)
        +String getTeacherComment()
        +void setTeacherComment(String)
        +String toString()
    }

    class ValidationResult {
        +boolean valid
        +String errorMessage
        +Object focusField
        +ValidationResult(boolean, String, Object)
        +boolean isValid()
        +String getErrorMessage()
        +Object getFocusField()
    }

    class BaseDAO~T~ {
        +Connection connection
        +BaseDAO(Connection)
        +T mapResultSet(ResultSet)
        +int count(String)
        +void closeResources(ResultSet, PreparedStatement)
    }

    class StudentDAO {
        <<interface>>
        +void save(Student)
        +Student findStudentById(Long)
        +List~Student~ findAllStudents()
        +void updateStudent(Student)
        +void deleteStudent(Long)
        +List~Student~ searchGeneral(SearchCriteria)
        +long countSearchGeneral(SearchCriteria)
    }

    class StudentDAOImpl {
        +Connection connection
        +DatabaseConnection dbConnection
        +StudentDAOImpl(Connection)
        +void saveStudent(Student)
        +Student findStudentById(Long)
        +List~Student~ findAllStudents()
        +void updateStudent(Student)
        +void deleteStudent(Long)
        +List~Student~ searchGeneral(SearchCriteria)
        +long countSearchGeneral(SearchCriteria)
        +long count()
        -Student mapResultSetToStudent(ResultSet)
    }

    class GradeDAO {
        <<interface>>
        +void saveGrade(Grade)
        +void updateGrade(Grade)
        +void deleteGrade(Long)
        +void saveCoefficient(double)
        +void updateCoefficient(Long, double)
        +double getMinAverageBySubject(String)
        +double getMaximumAverageBySubject(String)
        +List~SubjectResult~ searchBySubject(Long, SearchCriteria)
        +long countBySubject(Long, String)
        +double calculateWeightedAverageGrade(Long)
        +long count()
    }

    class GradeDAOImpl {
        +Connection connection
        +DatabaseConnection dbConnection
        +GradeDAOImpl(Connection)
        +void saveGrade(Grade)
        +void updateGrade(Grade)
        +void deleteGrade(Long)
        +void saveCoefficient(double)
        +void updateCoefficient(Long, double)
        +double getMinAverageBySubject(String)
        +double getMaximumAverageBySubject(String)
        +List~SubjectResult~ searchBySubject(Long, SearchCriteria)
        +long countBySubject(Long, String)
        +double calculateWeightedAverageGrade(Long)
        -Grade mapResultSetToGrade(ResultSet)
    }

    class SubjectCommentDAO {
        <<interface>>
        +void saveComment(SubjectComment)
        +void updateComment(SubjectComment)
        +void deleteComment(Long)
        +List~SubjectComment~ findCommentsByStudentAndSubject(Long, String)
    }

    class SubjectCommentDAOImpl {
        +Connection connection
        +DatabaseConnection dbConnection
        +SubjectCommentDAOImpl(Connection)
        +void saveComment(SubjectComment)
        +void updateComment(SubjectComment)
        +void deleteComment(Long)
        +List~SubjectComment~ findCommentsByStudentAndSubject(Long, String)
        -SubjectComment mapResultSetToComment(ResultSet)
    }

    class UserDAO {
        <<interface>>
        +void saveUser(User)
        +User findUserByUsername(String)
        +boolean authenticateUser(String, String)
    }

    class UserDAOImpl {
        +Connection connection
        +DatabaseConnection dbConnection
        +UserDAOImpl(Connection)
        +void saveUser(User)
        +User findUserByUsername(String)
        +boolean authenticateUser(String, String)
        -User mapResultSetToUser(ResultSet)
    }

    class DatabaseConnection {
        +String url
        +String username
        +String password
        +Connection connection
        +DatabaseConnection()
        +void connect()
        +void disconnect()
        +Connection getConnection()
        +boolean isConnected()
        +ResultSet executeQuery(String)
        +void executeUpdate(String)
        +void executeSafeQuery(String, List~Object~)
        +void closeResources()
    }

    class DatabaseConfig {
        +Properties properties
        +String configFile
        +DatabaseConfig()
        +void loadConfig()
        +String getDatabaseUrl()
        +String getDatabaseUsername()
        +String getDatabasePassword()
    }

    class StudentService {
        +StudentDAO studentDAO
        +InputValidator validator
        +StudentService(StudentDAO, InputValidator)
        +void createStudent(Student)
        +Student getStudentByID(Long)
        +List~Student~ getAllStudents()
        +void updateStudent(Student)
        +void deleteStudent(Long)
        +List~Student~ searchStudents(SearchCriteria)
    }

    class GradeService {
        +GradeDAO gradeDAO
        +GradeService(GradeDAO)
        +List~SubjectResult~ searchBySubject(Long, SearchCriteria)
    }

    class StatisticsService {
        +StudentDAO studentDAO
        +GradeDAO gradeDAO
        +StatisticsService(StudentDAO, GradeDAO)
        +double calculateClassAverageBySubject(String)
        +Map~String, Long~ getStudentCountByAgeGroup()
        +Map~String, List~Double~~ getGradeDistributionBySubject(String)
        +List~Student~ getTopPerformers(int)
        +Map~String, Object~ getStudentStatistics()
        +double getMinAverageBySubject(String)
        +double getMaximumAverageBySubject(String)
        +double calculateWeightedAverageGrade()
    }

    class AuthenticationService {
        +UserDAO userDAO
        +User currentUser
        +AuthenticationService(UserDAO)
        +boolean authenticate(String, String)
        +void register(User)
        +boolean isAuthenticated()
        +User getCurrentUser()
        +void setCurrentUser(User)
    }

    class SubjectCommentService {
        +SubjectCommentDAO commentDAO
        +SubjectCommentService(SubjectCommentDAO)
        +void saveComment(SubjectComment)
        +void updateComment(SubjectComment)
        +void deleteComment(Long)
        +List~SubjectComment~ findCommentsByStudentAndSubject(Long, String)
    }

    class ImportExportService {
        +CSVHandler csvHandler
        +PDFExporter pdfExporter
        +ImportExportService()
        +void exportToCSV(List~Student~)
        +void exportToPDF()
    }

    class BackupService {
        +DatabaseConnection databaseConnection
        +StudentDAO studentDAO
        +BackupService(DatabaseConnection, StudentDAO)
        +void createBackup()
        +void restoreBackup()
        +void scheduleAutoBackup()
        +void stopAutoBackup()
        +List~String~ listBackups()
        +void deleteBackup(String)
        -void compressBackup(String)
        -void extractBackup(String)
    }

    class AlertUtils {
        +String CSS_FILE
        +void applyCustomStyle(Alert)
        +void showAlert(String, String)
        +void showError(String, String)
        +void showWarning(String, String)
        +void showInformation(String, String)
    }

    class DialogUtils {
        +DialogField createDialogField(String, String)
        +void showEditDialog(String, List~DialogField~, Consumer~List~DialogField~~, Function~List~DialogField~, String~)
        +void showTextAreaDialog(String, TextAreaField, Button)
        +void showDeleteConfirmation(String, String, Runnable)
        +TableColumn createActionColumn(String, String, Consumer)
        +void setupColumnSorting(TableView, Runnable)
    }

    class GradeValidator {
        +ValidationResult validateGradeInput(TextField, TextField)
        +ValidationResult checkForChanges(double, double, TextField, TextField)
        +Grade createGradeFromFields(Long, String, TextField, TextField)
        +SubjectComment createCommentFromField(Long, String, TextArea)
    }

    class PasswordUtils {
        +String generateSalt()
        +String hashPassword(String, String)
        +String hashPasswordWithSalt(String)
        +boolean verifyPassword(String, String)
        +boolean validatePasswordStrength(String)
        +String getPasswordCriteria()
    }

    class CSVHandler {
        +char delimiter
        +CSVHandler()
        +CSVHandler(char)
        +void exportGeneralSearchResults(List)
        +void exportStudentSearchResults(List)
        +boolean validateCSVFormat(String)
        +Student parseStudentFromCSV(String)
        +String formatStudentToCSV(Student)
    }

    class PDFExporter {
        +Document document
        +PdfWriter writer
        +PDFExporter()
        +void exportStatisticsGeneralGraph()
        +void exportStatisticsStudentGraph()
        +void formatStudentData(Student)
        +void addHeader()
        +void addFooter()
    }

    class SceneUtils {
        +T changeScene(Stage, String, String)
    }

    class StudentValidator {
        +ValidationResult validateForCreation(TextField, TextField, TextField, TextField)
        +ValidationResult validateAndApplyChanges(Student, TextField, TextField, TextField, TextField)
        +ValidationResult validateFirstName(String, TextField)
        +ValidationResult validateLastName(String, TextField)
        +ValidationResult validateAge(String, TextField)
        +ValidationResult validateClassName(String, TextField)
        +String formatName(String)
        +String formatClassName(String)
        +Student createStudentFromFields(TextField, TextField, TextField, TextField)
    }

    class InputValidator {
        +ValidationResult validateStudentInput(TextField, TextField, TextField, TextField)
    }

    class BaseTableController~T~ {
        +TextField searchField
        +Button searchButton
        +Button exportButton
        +Button importButton
        +TableView~T~ dataTable
        +Pagination pagination
        +ImportExportService importExportService
        +int ROWS_PER_PAGE
        +void initialize()
        +void initializeServices()
        +void setupTableResizing()
        +void refreshTableRowHeights()
        +void autoResizeTable()
        +void setupColumnSizing()
        +void setupPagination()
        +void setupSearchAndExport()
        +void setupImport()
        +void setupColumnSorting()
        +void loadInitialData()
        +void handleSearch()
        +void handleExport()
        +void loadDataPage(int)
        +int calculatePageCount()
        +SearchCriteria createSearchCriteria()
        +String extractSortField(TableColumn)
        +void refreshTable()
        +void refreshTableFromStart()
        +void setupTableColumns()*
        +List~T~ searchData(SearchCriteria)*
        +void exportToCSV(List~T~, File)*
        +int getTotalCount(SearchCriteria)*
        +void handleImport()*
        +void setImportExportService(ImportExportService)
    }

    class LoginController {
        +AuthenticationService authService
        +TextField textFieldUsername
        +TextField textFieldPassword
        +Button buttonLogin
        +Button buttonRegister
        +void initialize()
        +void handleLogin()
        +void handleRegister()
        +void showRegisterView()
        +void showMainView()
        +void clearForm()
    }

    class RegisterController {
        +AuthenticationService authService
        +TextField textFieldUsername
        +TextField textFieldPassword
        +TextField textFieldConfirmPassword
        +Button buttonRegister
        +Button buttonBackToLogin
        +void initialize()
        +void handleRegister()
        +void handleBackToLogin()
        +void showLogin()
        +void clearForm()
        +void setAuthService(AuthenticationService)
    }

    class StudentController {
        +TextField studentIdField
        +Label studentNameLabel
        +Label studentClassLabel
        +ComboBox~String~ subjectComboBox
        +TextField gradeField
        +TextField coefficientField
        +Button addGradeButton
        +TextArea commentArea
        +Button addCommentButton
        +Button okButton
        +Button searchButton
        +TableView~SubjectResult~ gradesTable
        +TableColumn~SubjectResult, String~ subjectColumn
        +TableColumn~SubjectResult, String~ gradesColumn
        +TableColumn~SubjectResult, Number~ minAverageColumn
        +TableColumn~SubjectResult, Number~ maxAverageColumn
        +TableColumn~SubjectResult, Number~ studentAverageColumn
        +TableColumn~SubjectResult, String~ commentsColumn
        +StudentService studentService
        +GradeService gradeService
        +SubjectCommentService commentService
        +Student currentStudent
        +List~String~ subjects
        +void initialize()
        +void setupColumnSizing()
        +void setupTableColumns()
        +void handleLoadStudent()
        +void handleAddGrade()
        +void handleAddComment()
        +void handleSort()
        +void showGradeEditDialog(String, String)
        +void showGradeDeleteConfirmation(String, String)
        +void showCommentEditDialog(String, String)
        +void showCommentDeleteConfirmation(String)
        +void disableGradeControls(boolean)
        +List~SubjectResult~ searchData(SearchCriteria)
        +int getTotalCount(SearchCriteria)
        +void exportToCSV(List~SubjectResult~, File)
        +void handleImport()
        +void setStudentService(StudentService)
        +void setGradeService(GradeService)
        +void setCommentService(SubjectCommentService)
    }

    class StudentsController {
        +StudentService studentService
        +ImportExportService importExportService
        +TextField searchField
        +TableView~Student~ studentTable
        +Pagination pagination
        +TextField firstNameField
        +TextField lastNameField
        +TextField ageField
        +TextField classNameField
        +int ROWS_PER_PAGE
        +void initialize()
        +void handleSearch()
        +void handleImport()
        +void handleExport()
        +void handleAddStudent()
        +void loadStudentsPage(int)
        +int calculatePageCount()
        +void showEditDialog(Student)
        +void showDeleteConfirmation(Student)
        +void setupEditColumn()
        +void setupDeleteColumn()
        +void setupColumnSorting()
        +void setupTableColumns()
        +void clearAddForm()
        +void setStudentService(StudentService)
        +void setImportExportService(ImportExportService)
    }

    class StudentStatsController {
        +Label studentNameLabel
        +ComboBox~String~ subjectComboBox
        +LineChart~String, Number~ gradeChart
        +CategoryAxis dateAxis
        +NumberAxis gradeAxis
        +Label noDataLabel
        +Button refreshButton
        +Button exportButton
        +List~String~ subjects
        +StatisticsService statisticsService
        +Student currentStudent
        +void initialize()
        +void setCurrentStudent(Student)
        +void updateGradeChart(String)
        +void handleRefresh()
        +void handleExport()
        +void setStatisticsService(StatisticsService)
    }

    class StudentsStatsController {
        +PieChart ageChart
        +Label noDataLabel
        +Button refreshButton
        +Button exportButton
        +StatisticsService statisticsService
        +void initialize()
        +void loadStatistics()
        +void updateAgeDistributionChart()
        +void handleRefresh()
        +void handleExport()
        +void setStatisticsService(StatisticsService)
    }

    class TabController {
        +void initialize()
        +void handleButtonAction()
    }

    class WelcomeController {
        +Label usernameLabel
        +User currentUser
        +void initialize()
        +void setCurrentUser(User)
        +void updateUI()
    }

    class BackupController {
        +BackupService backupService
        +void handleCreateBackup()
        +void handleRestoreBackup()
        +void handleAutoBackupSchedule()
        +void showBackupList()
        +void handleDeleteBackup()
    }

    class Main {
        +void start(Stage)
        +void main(String[])
    }

    %% Relations entre entités de domaine
    Student "1" --> "0..*" Grade : has
    Student "1" --> "0..*" SubjectComment : has
    User "1" --> "0..*" SubjectComment : creates

    %% Relations DAO-Interface
    StudentDAOImpl ..|> StudentDAO : implements
    GradeDAOImpl ..|> GradeDAO : implements
    SubjectCommentDAOImpl ..|> SubjectCommentDAO : implements
    UserDAOImpl ..|> UserDAO : implements

    %% Héritage DAO
    StudentDAOImpl --|> BaseDAO : extends
    GradeDAOImpl --|> BaseDAO : extends
    SubjectCommentDAOImpl --|> BaseDAO : extends
    UserDAOImpl --|> BaseDAO : extends

    %% Relations Service vers DAO
    StudentService --> StudentDAO : uses
    StudentService --> InputValidator : uses
    GradeService --> GradeDAO : uses
    StatisticsService --> StudentDAO : uses
    StatisticsService --> GradeDAO : uses
    AuthenticationService --> UserDAO : uses
    SubjectCommentService --> SubjectCommentDAO : uses
    BackupService --> DatabaseConnection : uses
    BackupService --> StudentDAO : uses
    ImportExportService --> CSVHandler : uses
    ImportExportService --> PDFExporter : uses

    %% Relations Controller vers Service
    LoginController --> AuthenticationService : uses
    RegisterController --> AuthenticationService : uses
    StudentController --> StudentService : uses
    StudentController --> GradeService : uses
    StudentController --> SubjectCommentService : uses
    StudentsController --> StudentService : uses
    StudentsController --> ImportExportService : uses
    StudentStatsController --> StatisticsService : uses
    StudentsStatsController --> StatisticsService : uses
    BackupController --> BackupService : uses
    WelcomeController --> User : displays
    TabController --> SceneUtils : uses
    Main --> LoginController : launches

    %% Héritage Controller
    StudentsController --|> BaseTableController : extends
    StudentController --|> BaseTableController : extends

    %% Relations des utilitaires
    DialogUtils --> GradeValidator : uses
    DialogUtils --> AlertUtils : uses
    GradeValidator --> ValidationResult : creates
    StudentValidator --> ValidationResult : creates
    InputValidator --> ValidationResult : creates
    InputValidator --> StudentValidator : delegates
    PDFExporter --> Student : formats
    CSVHandler --> Student : formats
    SceneUtils --> BaseTableController : manages
    RegisterController --> PasswordUtils : uses
    AuthenticationService --> PasswordUtils : uses

    %% Relations database
    DatabaseConnection --> DatabaseConfig : uses
    StudentDAOImpl --> DatabaseConnection : uses
    GradeDAOImpl --> DatabaseConnection : uses
    SubjectCommentDAOImpl --> DatabaseConnection : uses
    UserDAOImpl --> DatabaseConnection : uses

    %% Relations avec SearchCriteria
    BaseTableController --> SearchCriteria : creates
    StudentService --> SearchCriteria : uses
    GradeService --> SearchCriteria : uses
    StudentDAO --> SearchCriteria : uses
    GradeDAO --> SearchCriteria : uses

    %% Relations avec SubjectResult
    GradeDAO --> SubjectResult : creates
    GradeService --> SubjectResult : creates
    StudentController --> SubjectResult : displays

```
### Directory Structure
* * *

```
StudentManagementSystem/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── studentmanagement/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Student.java
│   │   │   │   │   │   ├── Grade.java
│   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   ├── SubjectComment.java
│   │   │   │   │   │   └── SubjectResult.java
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── StudentDAO.java
│   │   │   │   │   │   ├── StudentDAOImpl.java
│   │   │   │   │   │   ├── UserDAO.java
│   │   │   │   │   │   ├── GradeDAO.java
│   │   │   │   │   │   ├── GradeDAOImpl.java
│   │   │   │   │   │   ├── SubjectCommentDAO.java
│   │   │   │   │   │   └── SubjectCommentDAOImpl.java
│   │   │   │   │   ├── database/
│   │   │   │   │   │   ├── DatabaseConnection.java
│   │   │   │   │   │   └── DatabaseConfig.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── StudentService.java
│   │   │   │   │   │   ├── AuthenticationService.java
│   │   │   │   │   │   ├── StatisticsService.java
│   │   │   │   │   │   ├── ImportExportService.java
│   │   │   │   │   │   └── BackupService.java
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── BaseTableController.java
│   │   │   │   │   │   ├── LoginController.java
│   │   │   │   │   │   ├── RegisterController.java
│   │   │   │   │   │   ├── StudentController.java
│   │   │   │   │   │   ├── StudentsController.java
│   │   │   │   │   │   ├── StudentStatsController.java
│   │   │   │   │   │   ├── StudentsStatsController.java
│   │   │   │   │   │   ├── TabController.java
│   │   │   │   │   │   └── WelcomeController.java
│   │   │   │   │   ├── utils/
│   │   │   │   │   │   ├── AlertUtils.java
│   │   │   │   │   │   ├── CSVHandler.java
│   │   │   │   │   │   ├── DialogUtils.java
│   │   │   │   │   │   ├── GradeValidator.java
│   │   │   │   │   │   ├── PasswordUtils.java
│   │   │   │   │   │   ├── PDFExporter.java
│   │   │   │   │   │   ├── SceneUtils.java
│   │   │   │   │   │   ├── SearchCriteria.java
│   │   │   │   │   │   ├── StudentValidator.java
│   │   │   │   │   │   └── SubjectResult.java
│   │   │   │   │   └── Main.java
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── fxml/
│   │   │       │   ├── login.fxml
│   │   │       │   ├── register.fxml
│   │   │       │   ├── studentStats.fxml
│   │   │       │   ├── studentsStats.fxml
│   │   │       │   ├── student.fxml
│   │   │       │   ├── students.fxml
│   │   │       │   ├── tab.fxml
│   │   │       │   └── welcome.fxml
│   │   │       ├── css/
│   │   │       │   └── styles.css
│   │   │       ├── images/
│   │   │       │   └── icon.png
│   │   │       ├── database/
│   │   │       │   └── schema.sql
│   │   │       └── config/
│   │   │           └── application.properties
│   │
│   └── test/
│       ├── java/
│       │   ├── com/
│       │   │   ├── studentmanagement/
│       │   │   │   ├── controller/
│       │   │   │   │   ├── BaseTableControllerUnitTest.java
│       │   │   │   │   ├── LoginControllerUITest.java
│       │   │   │   │   ├── LoginControllerUnitTest.java
│       │   │   │   │   ├── RegisterControllerUITest.java
│       │   │   │   │   ├── RegisterControllerUnitTest.java
│       │   │   │   │   ├── StudentControllerUITest.java
│       │   │   │   │   ├── StudentControllerUnitTest.java
│       │   │   │   │   ├── StudentStatsControllerUITest.java
│       │   │   │   │   ├── StudentStatsControllerUnitTest.java
│       │   │   │   │   ├── StudentsStatsControllerUITest.java
│       │   │   │   │   ├── StudentsStatsControllerUnitTest.java
│       │   │   │   │   ├── StudentsControllerUITest.java
│       │   │   │   │   └── StudentsControllerUnitTest.java
│       │   │   │   ├── dao/
│       │   │   │   │   └── StudentDAOTest.java
│       │   │   │   ├── service/
│       │   │   │   │   ├── StudentServiceTest.java
│       │   │   │   │   └── AuthenticationServiceTest.java
│       │   │   │   └── utils/
│       │   │   │       └── InputValidatorTest.java
│       │
│       └── resources/
│           └── test-data/
│               └── sample-students.csv
│
├── lib/
│   ├── postgresql-xx.x.jar
│   └── other-dependencies.jar
│
├── docs/
│   ├── database-schema.md
│   └── user-manual.md
│
├── scripts/
│   ├── setup-database.sql
│   └── sample-data.sql
│
├── README.md
├── .gitignore
└── pom.xml
```
## Database 

### Diagram tables
* * *
### User Table
The `User` table is designed to manage user accounts within the system.
It contains essential information about each user, including their credentials, role, and account status.

```
+--------------------------------------------------+
|                      User                        |
+--------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                      |
| - username: VARCHAR(255) NOT NULL UNIQUE         |
| - password_hash: TEXT NOT NULL                   |
| - role: VARCHAR(50) DEFAULT 'USER'               |  -- Default role value
| - is_active: BOOLEAN DEFAULT TRUE                |
+--------------------------------------------------+
```

### Class Table
The `Class` table stores information about academic classes.
Each class has a unique identifier, a name.
```
+---------------------------------------------------+
|                      Class                        |
+---------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                       |
| - class_name: VARCHAR(5) NOT NULL UNIQUE          | -- example : T1, 5B, 3A ..
+---------------------------------------------------+
```

### Student Table
The `Student` table contains information about individual students. Each student has a unique identifier and various personnal details,
including their name, age... The table also tracks student's active status and timestamps for creation and updates.

```
+----------------------------------------------------------------------------------+
|                     Student                                                      |
+----------------------------------------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                                                      |
| - first_name: VARCHAR(50) NOT NULL                                               |
| - last_name: VARCHAR(50) NOT NULL                                                |
| - age: INTEGER CHECK (age > 0 AND age < 150)                                     |  -- Constraint on age
| - class_id: BIGINT REFERENCES Class(id) ON DELETE SET NULL                       |  -- Foreign key with action
| - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                |
| - updated_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                |
| - is_active: BOOLEAN DEFAULT TRUE                                                |
+----------------------------------------------------------------------------------+
```

### Subject Table
The `Subject` table holds data about various subjects offered in the school. Each subject has a unique identifier, a name and a coefficient value indicating its weight in grading.

```
+---------------------------------------------------+
|                     Subject                       |
+---------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                       |
| - name: VARCHAR(50) NOT NULL UNIQUE               |
| - coefficient: DECIMAL(3,2) DEFAULT 1.0           |  -- Default coefficient value 1
| - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP |
+---------------------------------------------------+
```
### Grade Table
The `Grade` table records the grades received by students for specific subjects.
Each record is linked to a student and a subject, includes the grade value, the recording date. Timestamps for creation and updates are also included.

```
+-----------------------------------------------------------------------+
|                              Grade                                    |
+-----------------------------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                                           |
| - student_id: BIGINT REFERENCES Student(id) ON DELETE CASCADE         |  -- Foreign key with action
| - subject_id: BIGINT REFERENCES Subject(id) ON DELETE CASCADE         |  -- Foreign key with action
| - grade: DECIMAL(4,2) CHECK (grade >= 0 AND grade <= 20)              |  -- Constraint on grade
| - date_recorded: DATE DEFAULT CURRENT_DATE                            |  -- Default date recorded
| - grade_coefficient: DECIMAL(3,2) DEFAULT 1.0 CHECK (coefficient > 0) |  -- Coefficient for the specific grade
| - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP                     |
| - updated_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP                     |
+-----------------------------------------------------------------------+
```

### SubjectComment Table
The `SubjectComment' table is designed to store comments made by teachers for specific students in their respective subjects. Each comment is linked to a student and as subject, allowing for personnalised feedback.

```
+---------------------------------------------------------------+
|                    SubjectComment                             |
+---------------------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                                   |
| - student_id: BIGINT REFERENCES Student(id) ON DELETE CASCADE |  -- Foreign key with action
| - subject_id: BIGINT REFERENCES Subject(id) ON DELETE CASCADE |  -- Foreign key with action
| - comment: TEXT NOT NULL                                      |  -- Comment from the teacher
| - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP             |  -- Timestamp for record keeping
+---------------------------------------------------------------+
```

### BackUp Table
The `Backup` table maintains a record of backup operations, including the type of backup , file path, size, status, and any error messages encountered during backup process. A timestamp for the creation of each log entry is also included

```
+----------------------------------------------------------------------+
|                  Backup                                              |
+----------------------------------------------------------------------+
| - id: BIGSERIAL PRIMARY KEY                                          |
| - backup_type: VARCHAR(50) CHECK (backup_type IN ('AUTO', 'MANUAL')) |  -- Backup type constraint
| - file_path: VARCHAR(255) NOT NULL                                   |
| - file_size: BIGINT                                                  |
| - status: VARCHAR(50) DEFAULT 'SUCCESS' CHECK                        |
|                     (status IN ('SUCCESS', 'FAILED', 'IN_PROGRESS')) |  -- Status constraint
| - error_message: TEXT                                                |
| - created_at: TIMESTAMP DEFAULT CURRENT_TIMESTAMP                    |
+----------------------------------------------------------------------+
```

## ER Diagram 

```mermaid
erDiagram
    User {
        BIGSERIAL id PK
        VARCHAR(255) username UK "NOT NULL UNIQUE"
        TEXT password_hash "NOT NULL"
        VARCHAR(50) role "DEFAULT 'USER'"
        BOOLEAN is_active "DEFAULT TRUE"
    }
    
    Classe {
        BIGSERIAL id PK
        VARCHAR(5) class_name UK "NOT NULL UNIQUE"
    }
    
    Student {
        BIGSERIAL id PK
        VARCHAR(50) first_name "NOT NULL"
        VARCHAR(50) last_name "NOT NULL"
        INTEGER age "CHECK (age > 0 AND age < 150)"
        BIGINT class_id FK "REFERENCES Class(id)"
        TIMESTAMP created_at "DEFAULT CURRENT_TIMESTAMP"
        TIMESTAMP updated_at "DEFAULT CURRENT_TIMESTAMP"
        BOOLEAN is_active "DEFAULT TRUE"
    }
    
    Subject {
        BIGSERIAL id PK
        VARCHAR(50) name UK "NOT NULL UNIQUE"
        DECIMAL coefficient "DEFAULT 1.0"
        TIMESTAMP created_at "DEFAULT CURRENT_TIMESTAMP"
    }
    
    Grade {
        BIGSERIAL id PK
        BIGINT student_id FK "REFERENCES Student(id)"
        BIGINT subject_id FK "REFERENCES Subject(id)"
        DECIMAL grade "CHECK (grade >= 0 AND grade <= 20)"
        DATE date_recorded "DEFAULT CURRENT_DATE"
        DECIMAL grade_coefficient "DEFAULT 1.0 CHECK (coefficient > 0)"
        TIMESTAMP created_at "DEFAULT CURRENT_TIMESTAMP"
        TIMESTAMP updated_at "DEFAULT CURRENT_TIMESTAMP"
    }
    
    SubjectComment {
        BIGSERIAL id PK
        BIGINT student_id FK "REFERENCES Student(id)"
        BIGINT subject_id FK "REFERENCES Subject(id)"
        TEXT comment "NOT NULL"
        TIMESTAMP created_at "DEFAULT CURRENT_TIMESTAMP"
    }
    
    Backup {
        BIGSERIAL id PK
        VARCHAR(50) backup_type "CHECK IN ('AUTO', 'MANUAL')"
        VARCHAR(255) file_path "NOT NULL"
        BIGINT file_size
        VARCHAR(50) status "DEFAULT 'SUCCESS' CHECK IN ('SUCCESS', 'FAILED', 'IN_PROGRESS')"
        TEXT error_message
        TIMESTAMP created_at "DEFAULT CURRENT_TIMESTAMP"
    }
    
    %% Relations
    Classe ||--o{ Student : "has"
    Student ||--o{ Grade : "receives"
    Subject ||--o{ Grade : "graded_in"
    Student ||--o{ SubjectComment : "receives_comment"
    Subject ||--o{ SubjectComment : "commented_on"
```

## User Stories - ÉDUSYS

### Authentication & Access

#### US-001: User Login
**As a** teacher/administrator,  
**I want** to log in with my username and password,  
**So that** I can access the student management system securely.

**Acceptance Criteria:**
- User can enter username and password
- System validates credentials
- Upon successful login, user is redirected to welcome screen
- Invalid credentials show appropriate error message

#### US-002: User Registration
**As a** new user,  
**I want** to create an account with username and password,  
**So that** I can access the system.

**Acceptance Criteria:**
- User can enter username, password, and password confirmation
- System validates password match
- User account is created successfully
- User can navigate back to login screen

#### US-003: Welcome Screen
**As a** logged-in user,  
**I want** to see a personalized welcome message,  
**So that** I know I'm successfully logged in and feel welcomed.

**Acceptance Criteria:**
- Display personalized greeting with username
- Show welcoming message in French interface
- Provide clear navigation to main features

### Student Management

#### US-004: View Students List
**As a** teacher,  
**I want** to view a complete list of all students,  
**So that** I can see their basic information at a glance.

**Acceptance Criteria:**
- Display table with ID, first name, last name, age, class, and average
- Show all students with pagination for large datasets
- Display student information clearly and readably

#### US-005: Search Students
**As a** teacher,  
**I want** to search for specific students,  
**So that** I can quickly find a student in a large list.

**Acceptance Criteria:**
- Provide search input field
- Search functionality works on student names
- Results update dynamically or on search button click
- Clear search results when needed

#### US-006: Add New Student
**As a** teacher,  
**I want** to add a new student to the system,  
**So that** I can maintain an up-to-date student roster.

**Acceptance Criteria:**
- Form includes fields for name, first name, age, and class
- All required fields must be filled
- Student is added to the main list upon successful submission
- Form validation prevents invalid data entry

#### US-007: Edit Student Information
**As a** teacher,  
**I want** to modify existing student information,  
**So that** I can keep student records accurate and current.

**Acceptance Criteria:**
- Each student row has an edit button
- Edit functionality allows modification of student details
- Changes are saved and reflected in the main list
- Validation prevents invalid data updates

#### US-008: Delete Student
**As a** teacher,  
**I want** to remove a student from the system,  
**So that** I can maintain a clean and current student list.

**Acceptance Criteria:**
- Each student row has a delete button
- System prompts for confirmation before deletion
- Student is permanently removed from the database
- List updates immediately after deletion

#### US-009: Import/Export Students
**As a** teacher,  
**I want** to import and export student data,  
**So that** I can integrate with other systems and backup data.

**Acceptance Criteria:**
- Import button allows uploading student data files
- Export button generates downloadable student list
- Supports common file formats (CSV, Excel)
- Data integrity maintained during import/export

### Individual Student Management

#### US-010: Select Student by ID
**As a** teacher,  
**I want** to search for a student by their ID,  
**So that** I can quickly access their detailed information.

**Acceptance Criteria:**
- ID search field with confirmation button
- System displays student name and class when found
- Clear error message if student ID not found
- Quick access to student's detailed view

#### US-011: Add Student Grades
**As a** teacher,  
**I want** to add grades for specific subjects,  
**So that** I can track student academic performance.

**Acceptance Criteria:**
- Form includes subject, grade, and coefficient fields
- Grades are validated for appropriate ranges
- Multiple grades can be added for the same subject
- Grades are immediately reflected in student's record

#### US-012: Add Comments
**As a** teacher,  
**I want** to add comments about student performance,  
**So that** I can provide qualitative feedback and notes.

**Acceptance Criteria:**
- Comment text field with sufficient character limit
- Comments are associated with specific students
- Comments are saved and displayed in student view
- Edit and delete functionality for existing comments

#### US-013: View Grade History
**As a** teacher,  
**I want** to view a student's complete grade history,  
**So that** I can track their academic progress over time.

**Acceptance Criteria:**
- Table displays subjects, grades, min/max averages, and comments
- Sorting functionality by subject or grade
- Search within student's grades
- Clear display of calculated averages

#### US-014: Export Student Data
**As a** teacher,  
**I want** to export individual student's academic data,  
**So that** I can share reports or create backups.

**Acceptance Criteria:**
- Export button generates downloadable file
- Includes all grades, averages, and comments
- Professional formatting suitable for reports
- Multiple export formats available

### Analytics & Statistics

#### US-015: View Age Distribution
**As a** teacher,  
**I want** to see the age distribution of all students,  
**So that** I can understand the demographic composition of my classes.

**Acceptance Criteria:**
- Pie chart visualization of age groups
- Clear labels and percentages
- Responsive chart that updates with data changes
- Export functionality for the chart

#### US-016: View Individual Grade Evolution
**As a** teacher,  
**I want** to see a student's grade evolution over time,  
**So that** I can identify trends and improvement areas.

**Acceptance Criteria:**
- Line graph showing grade progression
- Filter by specific subject
- Time-based x-axis with grades on y-axis
- Export chart functionality

#### US-017: Refresh Statistical Data
**As a** teacher,  
**I want** to refresh statistical displays,  
**So that** I can ensure I'm viewing the most current data.

**Acceptance Criteria:**
- Refresh button updates all displayed statistics
- Loading indicators during data refresh
- Error handling for refresh failures
- Automatic refresh option available

#### US-018: Handle No Data Scenarios
**As a** teacher,  
**I want** to see appropriate messages when no data is available,  
**So that** I understand the system status clearly.

**Acceptance Criteria:**
- Clear "No data available" messages in French
- Helpful suggestions for next steps
- Consistent messaging across all views
- Professional appearance even with empty data

### System Features

### US-019: Pagination
**As a** teacher,  
**I want** to navigate through large datasets with pagination,  
**So that** the system performs well with many students.

**Acceptance Criteria:**
- Page navigation controls at bottom of tables
- Configurable items per page
- Current page indication
- Smooth navigation between pages

#### US-020: Data Validation
**As a** teacher,  
**I want** the system to validate my input data,  
**So that** I can maintain data quality and avoid errors.

**Acceptance Criteria:**
- Real-time validation feedback
- Clear error messages in appropriate language
- Prevention of invalid data submission
- Consistent validation across all forms

## UI

### Login interface layout

```
+----------------------------------------------------------------------------------+
|                                                                                  |
|                                      Logo                                        |
|                                                                                  |
|                                     ÉDUSYS                                       |
|                                                                                  |
|                           Nom d'utilisateur: [__________]                        |
|                                                                                  |
|                           Mot de passe:      [__________]                        |
|                                                                                  |
|                                 [   Connecte moi   ]                             |
|                                                                                  |
|                                          ou                                      |
|                                [   Enregistre moi   ]                            |
|                                                                                  |
|                                                                                  |
+----------------------------------------------------------------------------------+

```
### Registration interface layout

```
+----------------------------------------------------------------------------------+
|                                                                                  |
|                                   Logo                                           |
|                                                                                  |
|                                  ÉDUSYS                                          |
|                                                                                  |
|                         Nom d'utilisateur: [__________]                          |
|                                                                                  |
|                         Mot de passe:      [__________]                          |
|                                                                                  |
|                         Confirme ton mp:   [__________]                          |
|                                                                                  |
|                                                                                  |
|                                [   Enregistre moi   ]                            |
|                                                                                  |
|                                                                                  |
|                                  [   ⬅ Retour   ]                               |
|                                                                                  |
+----------------------------------------------------------------------------------+

```
### Welcome screen layout

```
+--------------+
|   Bonjour !  |
+--------------+-------------------------------------------------------------------+
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                     BIENVENUE                                    |
|                                                                                  |
|                                 [Nom d'utilisateur]                              |
|                                                                                  |
|                        Je te souhaite une splendide journée !                    |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
+----------------------------------------------------------------------------------+

```

### Students management interface layout

```
               +---------------+
               | Les Etudiants |
+--------------+---------------+---------------------------------------------------+
|                                                                                  |
| Rechercher: [_____________________] [Je recherche]  [ J'importe ] [ J'exporte ]  |
|                                                                                  |
+--------+----------+----------+-----+--------+---------+------------+-------------+
|   ID   | Prénom   | Nom      | Age | Classe | Moyenne | Je modifie | Je supprime |
+--------+----------+----------+-----+--------+---------+------------+-------------+
|   [ ]  |  [____]  |  [____]  | [ ] |  [__]  |   [__]  |    [__]    |     [__]    |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
|        |          |          |     |        |         |            |             |
+--------+----------+----------+-----+--------+---------+------------+-------------+
|                                 [ Pagination ]                                   |
|                                                                                  |
| Nom:            Prénom:        Age:    Classe:                                   |
| [___________]  [____________]  [____]  [____]              [J'ajoute l'étudiant] |
+----------------------------------------------------------------------------------+

```

### Students age distribution interface layout

```
                               +----------------+
                               | Et Leurs Stats |
+------------------------------+----------------+----------------------------------+
|                      Répartition des étudiants par tranche d'âge                 |
|                                                                                  |
|                                                                                  |
|                          Répartition par âge (Graphique)                         |
|                                                                                  |
|                                                                                  |
|                            [ Graphique en Camembert ]                            |
|                                                                                  |
|                               Aucune donnée disponible                           |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                             [ Rafraîchir ]   [ Exporter ]                        |
+----------------------------------------------------------------------------------+

```

### Student management interface layout

```
                                                +--------------+
                                                | Un  Etudiant |
+-----------------------------------------------+--------------+-------------------+
| ID étudiant : [_____]  [ OK ]  Nom de l'étudiant : [______]                      |
| Classe : [_]                                                                     |
|                                                                                  |
| Matières                   Note      Coeff     [J'ajoute une note]               |
|                                                                                  |
| [___________]            | [__]    | [__]    |                                   |
|                                                                                  |
| Commentaire: [____________________________] [J'ajoute un commentaire]            |
|                                                                                  |
| Rechercher: [______________________________________________________]  [Je trie]  |
+----------------+------------+------------+-----------+--------+------------------+
| Matières       | Notes      | Moy min    | Moy max   | Moy    |    Commentaires  |
+----------------+------------+------------+-----------+--------+------------------+
| [_________]    | [________] | [_____]    | [____]    | [_____]| [_______________]|
|                |            |            |           |        |                  |
|                |            |            |           |        |                  |
|                |            |            |           |        |                  |
+----------------+------------+------------+-----------+--------+------------------+
|                               [ Pagination ]                      [J'exporte]    |
+----------------------------------------------------------------------------------+

```

### Student grades evolution interface layout

````
                                                               +-------------------+
                                                               | Et Ses Stats      |
+--------------------------------------------------------------+-------------------+
|                      Évolution des notes de [Nom de l'étudiant]                  |
|                                                                                  |
|                       Matière: [ Sélectionner une matière ]                      |
|                                                                                  |
|                       Évolution des notes                                        |
|                                                                                  |
|                               [ Graphique des Notes ]                            |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                                                                                  |
|                               Aucune donnée disponible                           |
|                                                                                  |
|                                                                                  |
|                            [ Rafraîchir ]   [ Exporter ]                         |
+----------------------------------------------------------------------------------+

```