# Table Sorting Feature

The application provides intuitive table sorting capabilities across all data views, allowing users to organize information efficiently with persistent sorting across pagination.

## How It Works

### Column Header Interaction
Users can sort data by **clicking on column headers** in any table view:

- **First click**: Sorts in ascending order (A→Z, 1→9) with ↑ indicator
- **Second click**: Sorts in descending order (Z→A, 9→1) with ↓ indicator  
- **Third click**: Returns to default order (no indicator)

### Available Sorting Options

#### Student Management Table
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

#### Grade Management Table
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

#### Persistent Sorting
- **Cross-page persistence**: Sorting order is maintained when navigating between pages
- **Session continuity**: Selected sort remains active throughout the user session
- **Search compatibility**: Sorting works seamlessly with search filters

## Use Cases

### Academic Performance Analysis

1. Click "Average Grade" header → View top/bottom performers
2. Navigate through pages → Ranking order preserved
3. Identify students needing support or recognition


### Class Management

1. Click "Class" header → Group students by grade level
2. Browse pages → Class grouping maintained
3. Facilitate grade-level specific operations


### Subject Performance Review

1. Click "Student Average" in grades view → See strengths/weaknesses
2. Navigate subjects → Performance ranking preserved
3. Quick identification of problem areas


## Technical Implementation

### SearchCriteria Enhancement
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

### Controller Integration
Controllers automatically capture and preserve sorting preferences:

```java
// Capture user sort selection
String sortField = sortColumn.getId().replace("Column", "");
String sortDirection = sortColumn.getSortType().toString();
criteria.setSortField(sortField);
criteria.setSortDirection(sortDirection);
```

## User Benefits

- **Improved Efficiency**: No need to re-sort after each page change
- **Better Data Analysis**: Quick identification of patterns and trends
- **Intuitive Interface**: Standard sorting behavior expected in modern applications
- **Enhanced Navigation**: Seamless data exploration with maintained context

## Visual Indicators

The interface provides clear visual feedback:
- **↑ Arrow**: Ascending sort active
- **↓ Arrow**: Descending sort active
- **No Arrow**: Default/natural order

This sorting system significantly enhances the user experience by providing efficient data organization tools that work consistently across all application views.