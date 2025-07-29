# User Manual - Laplateformetracker

## Table of Contents

1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
   - [Installation](#21-installation)
   - [Login](#22-login)
   - [Registration](#23-registration)
   - [Welcome Screen](#24-welcome-screen)
3. [Student Management](#3-student-management)
   - [Viewing the List](#31-viewing-the-list)
   - [Searching for Students](#32-searching-for-students)
   - [Sorting Data](#33-sorting-data)
   - [Adding a Student](#34-adding-a-student)
   - [Editing a Student](#35-editing-a-student)
   - [Deleting a Student](#36-deleting-a-student)
   - [Import/Export](#37-importexport)
   - [Pagination](#38-pagination)
4. [Individual Student Management](#4-individual-student-management)
   - [Search by ID](#41-search-by-id)
   - [Adding Grades](#42-adding-grades)
   - [Adding Comments](#43-adding-comments)
   - [Viewing Results](#44-viewing-results)
   - [Editing/Deleting Grades](#45-editingdeleting-grades)
   - [Exporting Data](#46-exporting-data)
5. [Statistics](#5-statistics)
   - [Global Statistics](#51-global-statistics)
   - [Individual Statistics](#52-individual-statistics)
   - [Chart Configuration](#53-chart-configuration)
   - [Exporting Charts](#54-exporting-charts)
6. [Backups](#6-backups)
   - [Manual Creation](#61-manual-creation)
   - [Automatic Backups](#62-automatic-backups)
   - [Restoration](#63-restoration)
7. [Troubleshooting](#7-troubleshooting)
8. [Keyboard Shortcuts](#8-keyboard-shortcuts)
9. [Technical Information](#9-technical-information)

## 1. Introduction

Laplateformetracker is a comprehensive student management system developed in Java with a PostgreSQL database. The application allows you to efficiently manage student information, their grades, and analyze their performance through detailed statistics and graphical visualizations.

This manual will guide you through all the features of the application, from initial login to in-depth analysis of student data.

## 2. Getting Started

### 2.1 Installation

1. Ensure your system meets the following prerequisites:
   - Java 17 or higher
   - PostgreSQL 12 or higher
   - Maven 3.6 or higher

2. Clone the repository:
   ```bash
   git clone https://github.com/christine-chemali/StudentManagement.git
   cd StudentManagement
   ```

3. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE student_management;
   ```

4. Configure the `src/main/resources/config/application.properties` file:
   ```properties
   db.url=jdbc:postgresql://localhost:5432/student_management
   db.username=your_username
   db.password=your_password
   ```

5. Compile and run the application:
   ```bash
   mvn clean compile
   mvn javafx:run
   ```

### 2.2 Login

1. When launching the application, you will see the login screen.
2. Enter your username in the "Username" field.
3. Enter your password in the "Password" field.
4. Click the "Login" button.
5. If the information is correct, you will be redirected to the welcome screen.
6. In case of an error, a message will indicate the nature of the problem.

### 2.3 Registration

If you don't have an account yet:

1. From the login screen, click "Register".
2. On the registration screen, enter a unique username.
3. Create a secure password and confirm it.
4. Click "Register" to create your account.
5. Once registered, you will be redirected to the login screen.

**Password Note**: For security reasons, your password must contain at least 8 characters, including uppercase letters, lowercase letters, numbers, and special characters.

### 2.4 Welcome Screen

After successful login, you will see the welcome screen with:

1. A personalized welcome message with your username.
2. A tabbed interface to navigate between different features.
3. Main tabs: "Students", "Student", "Their Stats", "Student Stats".

## 3. Student Management

### 3.1 Viewing the List

In the "Students" tab, you will see a table containing all students registered in the system:

- **ID**: Unique student identifier
- **First Name**: Student's first name
- **Last Name**: Student's last name
- **Age**: Student's age
- **Class**: Student's class (e.g., "3A", "T1")
- **Average**: Student's overall grade average
- **Actions**: Buttons to edit or delete the student

### 3.2 Searching for Students

To quickly find a student:

1. Use the search field at the top of the screen.
2. Enter a search term (name, first name, ID, etc.).
3. Click "Search" or press Enter.
4. The results will appear in the table, filtered according to your search.
5. To clear the search, empty the field and click "Search" again.

### 3.3 Sorting Data

You can sort data in any column:

1. Click on the header of the column you want to sort.
2. The first click sorts the data in ascending order (A→Z, 1→9).
3. The second click reverses the order (Z→A, 9→1).
4. The third click removes the sorting.

**Important features**:
- Sorting is maintained when navigating between pages.
- The sort order remains active throughout the user session.
- Sorting works in combination with search filters.

### 3.4 Adding a Student

To add a new student:

1. At the bottom of the table, fill in the fields:
   - **Last Name**: Student's last name
   - **First Name**: Student's first name
   - **Age**: Student's age (integer)
   - **Class**: Student's class (recommended format: "3A", "T1", etc.)
2. Click "Add Student".
3. If all information is valid, the student will be added to the list.
4. In case of an error, a message will indicate the problem to correct.

### 3.5 Editing a Student

To modify an existing student's information:

1. Locate the student in the table.
2. Click the "Edit" button in the corresponding row.
3. A dialog box will open with the current information.
4. Modify the fields as needed.
5. Click "Save" to save the changes.
6. To cancel, click "Cancel" or close the window.

### 3.6 Deleting a Student

To delete a student from the system:

1. Locate the student in the table.
2. Click the "Delete" button in the corresponding row.
3. A confirmation dialog will appear.
4. Click "Confirm" to permanently delete the student.
5. Click "Cancel" to keep the student.

**Warning**: Deletion is permanent and also results in the deletion of all grades and comments associated with the student.

### 3.7 Import/Export

#### Importing Students

To import a list of students:

1. Click "Import" at the top of the table.
2. Select a properly formatted CSV file.
3. The system will check the file format.
4. Confirm the import.
5. A message will indicate the number of students successfully imported.

**Expected CSV format**:
```
lastname,firstname,age,class
Doe,John,15,3A
Smith,Jane,16,3B
```

#### Exporting Students

To export the list of students:

1. Apply desired filters (optional).
2. Click "Export" at the top of the table.
3. Choose the save location and file name.
4. Select the export format (CSV by default).
5. Click "Save".

The export will include all students currently displayed in the table, taking into account any applied filters.

### 3.8 Pagination

To navigate through a large list of students:

1. Use the pagination controls at the bottom of the table.
2. Click on page numbers to access a specific page directly.
3. Use the arrows to move to the next or previous page.
4. The total number of pages is displayed for reference.

By default, the system displays 15 students per page.

## 4. Individual Student Management

### 4.1 Search by ID

In the "Student" tab:

1. Enter the student's ID in the "Student ID" field.
2. Click "OK".
3. If the student exists, their information will be displayed.
4. The full name and class of the student will be visible at the top of the screen.
5. If the ID doesn't exist, an error message will be displayed.

### 4.2 Adding Grades

To add a grade to the selected student:

1. Select a subject from the dropdown menu.
2. Enter the grade (from 0 to 20) in the "Grade" field.
3. Enter the coefficient (numerical value) in the "Coeff" field.
4. Click "Add Grade".
5. The grade will be added and will appear in the results table.
6. The student's average will be automatically recalculated.

### 4.3 Adding Comments

To add a comment on the student's performance:

1. Select a subject from the dropdown menu (if not already done).
2. Enter your comment in the "Comment" text area.
3. Click "Add Comment".
4. The comment will be associated with the selected subject.
5. If a comment already exists for this subject, it will be updated.

### 4.4 Viewing Results

The results table displays:

- **Subjects**: The subjects for which the student has grades
- **Grades**: All grades obtained in the subject
- **Min Avg**: The minimum class average in this subject
- **Max Avg**: The maximum class average in this subject
- **Avg**: The student's average in this subject
- **Comments**: Comments associated with the subject

To filter results:
1. Use the search field above the table.
2. Enter a search term (subject name, grade value, etc.).
3. Click "Sort" to apply the filter.

### 4.5 Editing/Deleting Grades

To edit a grade:

1. In the results table, click on the grade you want to modify.
2. An edit dialog will open.
3. Modify the grade value or its coefficient.
4. Click "Save" to confirm the changes.

To delete a grade:

1. In the results table, click on the grade to delete.
2. Select the "Delete" option from the context menu.
3. Confirm the deletion in the dialog box.

### 4.6 Exporting Data

To export data for a specific student:

1. Search for and display the desired student.
2. Click "Export" at the bottom of the results table.
3. Choose the location and format of the export file.
4. The exported file will contain all information displayed in the table.

## 5. Statistics

### 5.1 Global Statistics

In the "Their Stats" tab, you can view statistics for all students in the system:

1. Select a chart type from the dropdown menu:
   - **Age Distribution**: Shows the distribution of students by age group
   - **Class Distribution**: Shows the number of students in each class
   - **Class Average Grade**: Shows the average grade for each class

2. Click "Generate" to display the chart.

3. The chart will be displayed in the main area with appropriate labels and legends.

### 5.2 Individual Statistics

In the "Student Stats" tab, you can view statistics for a specific student:

1. First, ensure you have selected a student in the "Student" tab.
2. Navigate to the "Student Stats" tab.
3. Select a chart type from the dropdown menu:
   - **Grade Evolution**: Shows the evolution of grades over time for a specific subject
   - **Yearly Grade Overview**: Shows an overview of grades for all subjects in a school year
   - **Subject Average Comparison**: Compares the student's averages with class averages

4. Configure the chart using the provided options (subject selection, year, etc.).
5. Click "Generate" to display the chart.

### 5.3 Chart Configuration

Each chart type has specific configuration options:

#### Grade Evolution
- **Subject**: Select the subject to analyze from the dropdown menu

#### Yearly Grade Overview
- **Academic Year**: Select the academic year to analyze

#### Subject Average Comparison
- No manual configuration required (automatically uses the student's class)

#### Age Distribution, Class Distribution, Class Average Grade
- No configuration required (system-wide statistics)

### 5.4 Exporting Charts

To export a chart:

1. Generate the desired chart.
2. Click "Export PDF" below the chart.
3. Choose the save location and file name.
4. The PDF will include:
   - The chart image
   - A data table with the values used to generate the chart
   - Student information (for individual charts)
   - Date and time of generation

## 6. Backups

### 6.1 Manual Creation

To manually create a backup of the system:

1. Navigate to the backup management area.
2. Click "Create Backup".
3. The system will create a complete backup of all data.
4. A confirmation message will appear when the backup is complete.
5. The backup will be stored in the configured backup location.

### 6.2 Automatic Backups

To set up automatic backups:

1. Navigate to the backup management area.
2. Click "Schedule Auto Backup".
3. Set the frequency (daily, weekly, monthly).
4. Set the time for the backup to run.
5. Click "Save" to activate automatic backups.
6. To disable automatic backups, click "Stop Auto Backup".

### 6.3 Restoration

To restore the system from a backup:

1. Navigate to the backup management area.
2. Click "Show Backup List".
3. Select the backup you want to restore from the list.
4. Click "Restore".
5. Confirm the restoration in the dialog box.
6. The system will restore all data from the selected backup.
7. You will need to restart the application after restoration.

**Warning**: Restoring from a backup will overwrite all current data. Make sure to create a new backup of your current data before performing a restoration.

## 7. Troubleshooting

### Common Issues and Solutions

#### Login Problems
- **Issue**: Unable to log in with correct credentials
- **Solution**: Check caps lock, verify username spelling, reset password if necessary

#### Database Connection Errors
- **Issue**: "Database connection failed" message
- **Solution**: Verify PostgreSQL is running, check connection settings in application.properties

#### Import Failures
- **Issue**: CSV import fails
- **Solution**: Ensure CSV format matches expected format, check for special characters or encoding issues

#### Chart Generation Issues
- **Issue**: Charts not displaying or showing "No data available"
- **Solution**: Verify that data exists for the selected parameters, try a different selection

#### Performance Problems
- **Issue**: Slow application response
- **Solution**: Reduce page size in pagination, optimize database queries, check system resources

## 8. Keyboard Shortcuts

The application supports several keyboard shortcuts to improve efficiency:

- **Enter** (in search fields): Execute search
- **Ctrl+N** (in student list): Add new student
- **Ctrl+E** (on selected student): Edit student
- **Ctrl+D** (on selected student): Delete student
- **Ctrl+S** (in edit dialogs): Save changes
- **Esc** (in dialogs): Cancel/Close dialog
- **Ctrl+P**: Print current view
- **Ctrl+F**: Focus on search field
- **Ctrl+Tab**: Navigate between tabs

## 9. Technical Information

### System Requirements
- **Operating System**: Windows 10/11, macOS 10.14+, Linux (major distributions)
- **Processor**: 2 GHz dual-core or better
- **Memory**: 4 GB RAM minimum, 8 GB recommended
- **Disk Space**: 500 MB for application, plus space for database (varies with number of students)
- **Java**: Version 17 or higher
- **Database**: PostgreSQL 12 or higher

### Data Security
- All passwords are stored using BCrypt hashing
- Database connections use prepared statements to prevent SQL injection
- Regular backups are recommended to prevent data loss

### File Formats
- **Import/Export**: CSV (comma-separated values)
- **Chart Export**: PDF
- **Backup Format**: Compressed database dump

### Network Requirements
- Local installation requires no network access
- Shared database installations require network access