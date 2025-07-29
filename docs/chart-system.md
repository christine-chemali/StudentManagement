# Chart System Documentation

## Overview

The chart system is a modular component of the Student Management System that provides statistical visualization capabilities. It allows users to generate, view, and export various types of charts related to student performance and demographic data.

## Architecture

The chart system follows a layered architecture with clear separation of concerns:

1. **Chart Selection Layer**: Manages the user interface for selecting and configuring charts
2. **Chart Generation Layer**: Handles the creation of charts based on selected parameters
3. **Data Access Layer**: Retrieves the necessary data from the database through the StatisticsService
4. **Export Layer**: Provides functionality to export charts and their underlying data

## Key Components

### ChartSelector

The `ChartSelector` class serves as the main UI component for the chart system. It provides:

- A dropdown for selecting chart types
- Dynamic configuration panels based on the selected chart type
- Chart display area
- Export functionality

### ChartGenerator Interface

The `ChartGenerator` interface defines the contract for all chart generators:

```java
public interface ChartGenerator {
    Chart generateChart(Map<String, Object> parameters);
    void setStatisticsService(StatisticsService statisticsService);
    String getChartTitle();
    String getChartDescription();
    boolean validateParameters(Map<String, Object> parameters);
    TableView<?> getDataTable();
}
```

### AbstractChartGenerator

The `AbstractChartGenerator` provides common functionality for all chart generators:

- Parameter validation
- Service availability checking
- Basic getters and setters
- Data table storage

### ChartFactory

The `ChartFactory` creates the appropriate chart generator based on the selected chart type:

```java
public ChartGenerator createChartGenerator(ChartType chartType) {
    // Returns the appropriate generator for the given chart type
}
```

## Available Chart Types

### Student-Specific Charts

1. **Student Grade Evolution**
   - Shows the evolution of a student's grades over time for a specific subject
   - Configuration: Subject selection
   - Implementation: `StudentGradeEvolutionGenerator`

2. **Yearly Grade Overview**
   - Presents an overview of a student's grades for all subjects in a school year
   - Configuration: Academic year selection
   - Implementation: `YearlyGradeOverviewGenerator`

3. **Subject Average Comparison**
   - Compares a student's averages with class averages for each subject
   - Configuration: None (uses current student and class)
   - Implementation: `SubjectAverageComparisonGenerator`

### System-Wide Charts

4. **Students Age Distribution**
   - Displays the distribution of students by age group
   - Configuration: None
   - Implementation: `StudentsAgeDistributionGenerator`

5. **Students Class Distribution**
   - Shows the number of students in each class
   - Configuration: None
   - Implementation: `StudentsClassDistributionGenerator`

6. **Class Average Grade**
   - Visualizes the average grade for each class
   - Configuration: None
   - Implementation: `ClassAverageGradeGenerator`

## Data Export Functionality

Each chart generator now implements a `getDataTable()` method that returns a TableView containing the raw data used to generate the chart. This enables:

1. **PDF Export**: Users can export both the visual chart and the underlying data to PDF
2. **Data Inspection**: Administrators can review the exact data used in charts
3. **Further Analysis**: The data can be exported for use in other tools

The data tables are created using custom model classes specific to each chart type, with appropriate columns and formatting.

# User Experience

## Student Statistics View

When a user accesses a student's statistics tab, they are presented with an intuitive interface designed to provide meaningful insights into student performance.

### Interface Components

The main interface consists of:
- A chart type selector (dropdown menu)
- A dynamic configuration area that adapts to the selected chart type
- A "Generate" button to create the chart
- A chart display area
- An "Export PDF" button for exporting the chart and its underlying data

### Available Student Charts

#### 1. Grade Evolution

This chart shows how a student's grades in a specific subject have changed over time.

**Configuration:**
- The user must select a subject from a dropdown menu
- The system automatically uses the ID of the currently viewed student

**Data Displayed:**
- A line chart with grades plotted against dates
- Clear labeling of assessment dates
- Color-coded trend lines

**User Interaction:**
1. Select "Grade Evolution" from the chart type dropdown
2. Choose a subject (e.g., "Mathematics") from the subject dropdown
3. Click "Generate"
4. View the resulting line chart showing grade progression
5. Hover over points to see exact grades and dates

**Example Flow:**
A teacher checking a student's progress in French would select "Grade Evolution," choose "French" from the subject dropdown, and click "Generate." The resulting chart would show all the student's French grades throughout the year, making it easy to identify trends and patterns.

#### 2. Yearly Overview

This chart displays average grades across all subjects for a selected academic year.

**Configuration:**
- The user must select an academic year (e.g., 2023-2024)
- The system automatically uses the ID of the currently viewed student

**Data Displayed:**
- A bar chart with subjects on the x-axis and average grades on the y-axis
- Color-coded bars for visual clarity
- Clear labeling of subject names and grade values

**User Interaction:**
1. Select "Yearly Overview" from the chart type dropdown
2. Choose an academic year from the year dropdown
3. Click "Generate"
4. View the resulting bar chart showing average grades by subject
5. Hover over bars to see exact average values

**Example Flow:**
A parent reviewing their child's performance would select "Yearly Overview," choose the current academic year, and click "Generate." The chart would display the student's average in each subject, making it easy to identify strengths and areas needing improvement.

#### 3. Subject Comparison

This chart compares the student's averages with class averages across different subjects.

**Configuration:**
- No manual configuration required
- The system automatically uses the student's ID and current class

**Data Displayed:**
- A grouped bar chart comparing student vs. class averages by subject
- Dual-colored bars for easy comparison
- Clear labeling of subjects and values

**User Interaction:**
1. Select "Subject Comparison" from the chart type dropdown
2. Click "Generate" (no additional configuration needed)
3. View the resulting grouped bar chart
4. Hover over bars to see exact values and differences

**Example Flow:**
An advisor evaluating a student's performance relative to peers would select "Subject Comparison" and click "Generate." The chart would show how the student's averages compare to class averages across all subjects, highlighting areas where the student excels or struggles compared to peers.

### Data Visualization

Once a chart is generated, the user sees:
- The visual chart (line, bar, etc.) with clear legends and explanatory titles
- Interactive elements allowing hover-over for detailed information
- Source data is not directly visible in the main interface but can be exported

### Export Functionality

Users can export charts as PDF documents containing:
- The chart image
- A table of the underlying data used to generate the chart
- Student information (name, class)
- Generation date and time
- School identification

### Navigation and Usability

- Charts feature interactive elements for exploring data points
- Configurations are streamlined with sensible defaults
- The interface dynamically adapts to show only relevant options
- Error states (like missing data) are clearly communicated
- Export options are prominently displayed

## System Statistics View

Administrators can access system-wide statistics through dedicated views that provide insights into overall student demographics and performance.

### Available System Charts

#### 1. Age Distribution

This chart shows the distribution of students by age group.

**Configuration:**
- No configuration required
- Displays data for the entire student body

**Data Displayed:**
- A pie chart with age groups and percentages
- Color-coded segments for different age ranges
- Clear labels showing both count and percentage

**User Interaction:**
1. Select "Age Distribution" from the system statistics menu
2. View the resulting pie chart
3. Hover over segments to see detailed counts and percentages

**Example Flow:**
An administrator planning age-appropriate activities would access the "Age Distribution" chart to see the breakdown of students by age group. The pie chart would show the proportion of students in each age range, helping inform program planning.

#### 2. Class Distribution

This chart displays the number of students in each class.

**Configuration:**
- No configuration required
- Displays data for all classes

**Data Displayed:**
- A bar chart with classes and student counts
- Sorted bars for easy comparison
- Clear labeling of class names and student counts

**User Interaction:**
1. Select "Class Distribution" from the system statistics menu
2. View the resulting bar chart
3. Hover over bars to see exact student counts

**Example Flow:**
A school administrator evaluating resource allocation would access the "Class Distribution" chart to see enrollment across different classes. This would help identify overcrowded or underutilized classrooms.

#### 3. Class Averages

This chart shows the average grade for each class.

**Configuration:**
- No configuration required
- Displays data for all classes

**Data Displayed:**
- A bar chart with classes and their average grades
- Color-coded bars based on performance
- Clear labeling of class names and average values

**User Interaction:**
1. Select "Class Averages" from the system statistics menu
2. View the resulting bar chart
3. Hover over bars to see exact average values

**Example Flow:**
A curriculum coordinator assessing program effectiveness would access the "Class Averages" chart to compare performance across different classes. This would help identify which teaching methods or class compositions might be most effective.

### Data Export and Analysis

Administrators can export all system statistics charts as PDF reports containing:
- The visual representation
- Detailed data tables
- Timestamp and generation information
- Institutional details

These reports can be used for:
- Board meetings
- Departmental reviews
- Accreditation documentation
- Year-over-year comparisons

### User Experience Considerations

The statistics system is designed with these principles in mind:
- Simplicity: Charts present complex data in an easily digestible format
- Relevance: Each chart answers specific questions about student performance or demographics
- Accessibility: Information is available to authorized users with minimal navigation
- Actionability: Data is presented in a way that facilitates decision-making

This approach allows users to access meaningful visualizations of student and system performance without being overwhelmed by excessive options or raw data.

## Implementation Details

### Data Table Generation

Each chart generator now creates a corresponding data table:

```java
private void createDataTable(Map<String, Double> data) {
    // Create a TableView with appropriate columns
    // Populate with data from the chart
    // Store in the dataTable field for later access
}
```

### PDF Export Process

When a user clicks the "Export PDF" button:

1. The current chart is captured as an image
2. The data table is converted to a formatted table in the PDF
3. Additional information (student details, date, etc.) is added
4. The PDF is generated and offered for download

## Usage Example

```java
// Create a chart selector for a specific student
ChartSelector selector = new ChartSelector(statisticsService, 
    ChartType.STUDENT_GRADE_EVOLUTION,
    ChartType.YEARLY_GRADE_OVERVIEW,
    ChartType.SUBJECT_AVERAGE_COMPARISON);

// Set the current student
selector.setStudent(currentStudent);

// Add it to the UI
studentStatsTab.setContent(selector);

// Set up export functionality
selector.setOnChartGenerated(chart -> {
    exportButton.setDisable(false);
});

exportButton.setOnAction(e -> {
    Chart chart = selector.getCurrentChart();
    TableView<?> dataTable = selector.getDataTable();
    if (chart != null && dataTable != null) {
        PDFExporter.exportChartAndData(chart, dataTable, currentStudent);
    }
});
```

## Future Enhancements

Potential improvements to the chart system:

1. **Interactive Charts**: Add zoom, pan, and filtering capabilities
2. **Custom Chart Creation**: Allow users to create custom charts by selecting variables
3. **Trend Analysis**: Add statistical analysis of trends and predictions
4. **Export to More Formats**: Support for Excel, CSV, and other formats
5. **Scheduled Reports**: Automatic generation of periodic statistical reports