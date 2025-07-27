package com.studentmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.ImportExportService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.StudentValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentsControllerUnitTest {

    private StudentsController studentsController;
    
    @Mock
    private StudentService studentServiceMock;
    @Mock
    private ImportExportService importExportServiceMock;
    @Mock
    private TextField searchFieldMock;
    @Mock
    private Button searchButtonMock;
    @Mock
    private Button importButtonMock;
    @Mock
    private Button exportButtonMock;
    @Mock
    private TableView<Student> studentTableMock;
    @Mock
    private TableColumn<Student, Integer> idColumnMock;
    @Mock
    private TableColumn<Student, String> firstNameColumnMock;
    @Mock
    private TableColumn<Student, String> lastNameColumnMock;
    @Mock
    private TableColumn<Student, Integer> ageColumnMock;
    @Mock
    private TableColumn<Student, String> classNameColumnMock;
    @Mock
    private TableColumn<Student, Double> averageGradeColumnMock;
    @Mock
    private TableColumn<Student, Void> editColumnMock;
    @Mock
    private TableColumn<Student, Void> deleteColumnMock;
    @Mock
    private Pagination paginationMock;
    @Mock
    private TextField firstNameFieldMock;
    @Mock
    private TextField lastNameFieldMock;
    @Mock
    private TextField ageFieldMock;
    @Mock
    private TextField classNameFieldMock;
    @Mock
    private Button addButtonMock;

    private List<Student> testStudents;

    @BeforeEach
    /**
     * Initializes the test environement before each test case
     * @throws Exception if any unexcpected error occurs during setup
     */
    public void setUp() throws Exception{
        //Create the controller
        studentsController = new StudentsController();
        
        //Inject the mocked services
        setField(studentsController, "studentService", studentServiceMock);
        setField(studentsController, "importExportService", importExportServiceMock);
        
        //Inject the mocked UI components
        setField(studentsController, "searchField", searchFieldMock);
        setField(studentsController, "searchButton", searchButtonMock);
        setField(studentsController, "importButton", importButtonMock);
        setField(studentsController, "exportButton", exportButtonMock);
        setField(studentsController, "studentTable", studentTableMock);
        setField(studentsController, "idColumn", idColumnMock);
        setField(studentsController, "firstNameColumn", firstNameColumnMock);
        setField(studentsController, "lastNameColumn", lastNameColumnMock);
        setField(studentsController, "ageColumn", ageColumnMock);
        setField(studentsController, "classNameColumn", classNameColumnMock);
        setField(studentsController, "averageGradeColumn", averageGradeColumnMock);
        setField(studentsController, "editColumn", editColumnMock);
        setField(studentsController, "deleteColumn", deleteColumnMock);
        setField(studentsController, "pagination", paginationMock);
        setField(studentsController, "firstNameField", firstNameFieldMock);
        setField(studentsController, "lastNameField", lastNameFieldMock);
        setField(studentsController, "ageField", ageFieldMock);
        setField(studentsController, "classNameField", classNameFieldMock);
        setField(studentsController, "addButton", addButtonMock);
        
        //Create test data with mocked students to avoid issues with setStudentId
        testStudents = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Student student = mock(Student.class);
            when(student.getStudentId()).thenReturn((long) i);
            when(student.getFirstName()).thenReturn("Prénom" + i);
            when(student.getLastName()).thenReturn("Nom" + i);
            when(student.getAge()).thenReturn(15 + (i % 5));
            when(student.getClassName()).thenReturn("Classe" + (i % 3 + 1));
            when(student.getAverageGrade()).thenReturn(10.0 + (i % 10));
            when(student.getFullName()).thenReturn("Prénom" + i + " Nom" + i);
            testStudents.add(student);
        }
        
        //Configure the default behavior of mocks
        when(searchFieldMock.getText()).thenReturn("");
        when(firstNameFieldMock.getText()).thenReturn("Harry");
        when(lastNameFieldMock.getText()).thenReturn("Potter");
        when(ageFieldMock.getText()).thenReturn("13");
        when(classNameFieldMock.getText()).thenReturn("5A");
        when(paginationMock.getCurrentPageIndex()).thenReturn(0);
        
        //Configure the behavior for studentTableMock
        ObservableList<Student> observableList = FXCollections.observableArrayList(testStudents.subList(0, 15));
        when(studentTableMock.getItems()).thenReturn(observableList);
        
        //Configure behavior for sorting columns
        when(studentTableMock.getSortOrder()).thenReturn(FXCollections.observableArrayList());
    }

    @Test
    /**
     * Tests the setter for the student service
     */
    public void testSetStudentService(){
        StudentService newService = mock(StudentService.class);
        studentsController.setStudentService(newService);
        assertEquals(newService, getField(studentsController, "studentService"));
    }    

    @Test
    /**
     * Tests the setter for the import/export service
     */
    public void testSetImportExportService(){
        ImportExportService newService = mock(ImportExportService.class);
        studentsController.setImportExportService(newService);
        assertEquals(newService, getField(studentsController, "importExportService"));
    }

    @Test
    /**
     * Tests the successful handling of adding a student
     * @throws Exception if &ny unexpected error occurs during the test execution
     */
    public void testHandleAddStudentSuccess() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class);
        MockedStatic<StudentValidator> studentValidatorMock = mockStatic(StudentValidator.class)){
            //Create a spy to be able to mock refresTable
            StudentsController spyController = spy(studentsController);
            doNothing().when(spyController).refreshTable();
            //Create a valid validation result
            StudentValidator.ValidationResult validResult = mock(StudentValidator.ValidationResult.class);
            when(validResult.isValid()).thenReturn(true);
            //Create a test student(mocked)
            Student newStudent = mock(Student.class);
            when(newStudent.getFirstName()).thenReturn("Harry");
            when(newStudent.getLastName()).thenReturn("Potter");
            when(newStudent.getAge()).thenReturn(13);
            when(newStudent.getClassName()).thenReturn("5A");
            //Config static mocks
            studentValidatorMock.when(() -> StudentValidator.validateForCreation(firstNameFieldMock, lastNameFieldMock, ageFieldMock, classNameFieldMock)).thenReturn(validResult);
            studentValidatorMock.when(() -> StudentValidator.createStudentFromFields(firstNameFieldMock, lastNameFieldMock, ageFieldMock, classNameFieldMock)).thenReturn(newStudent);
            //Call the private handleAddStudent method
            Method handleAddStudentMethod = StudentsController.class.getDeclaredMethod("handleAddStudent");
            handleAddStudentMethod.setAccessible(true);
            handleAddStudentMethod.invoke(spyController);
            //Verify that the method have been called
            verify(studentServiceMock).createStudent(newStudent);
            verify(spyController).refreshTable();
            verify(firstNameFieldMock).clear();
            verify(lastNameFieldMock).clear();
            verify(ageFieldMock).clear();
            verify(classNameFieldMock).clear();
            alertUtilsMock.verify(() -> AlertUtils.showInformation(eq("Succès"), eq("L'étudiant a été ajouté avec succès")));
        }
    }

    @Test
    /**
     * Tests the handling of adding a student with invalid input
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testHandleAddStudentWithInvalidInput() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class);
        MockedStatic<StudentValidator> studentValidatorMock = mockStatic(StudentValidator.class)){
            //Create an invalid validation result
            StudentValidator.ValidationResult invalidResult = mock(StudentValidator.ValidationResult.class);
            when(invalidResult.isValid()).thenReturn(false);
            when(invalidResult.getErrorMessage()).thenReturn("Prénom invalide");
            when(invalidResult.getFocusField()).thenReturn(firstNameFieldMock);
            //Config static mocks
            studentValidatorMock.when(() -> StudentValidator.validateForCreation(firstNameFieldMock, lastNameFieldMock, ageFieldMock, classNameFieldMock)).thenReturn(invalidResult);
            //Call the private handleAddStudent method
            Method handleAddStudentMethod = StudentsController.class.getDeclaredMethod("handleAddStudent");
            handleAddStudentMethod.setAccessible(true);
            handleAddStudentMethod.invoke(studentsController);
            //Verify that the methods have been called
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur de saisie"), eq("Prénom invalide")));
            verify(firstNameFieldMock).selectAll();
            verify(firstNameFieldMock).requestFocus();
            verify(studentServiceMock, never()).createStudent(any(Student.class));
        }
    }
    
    @Test
    /**
     * Tests the handling of the search functionality.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testHandleSearch() throws Exception{
        //Create a spy to be able to mock refreshTableFromStart
        StudentsController spyController = spy(studentsController);
        doNothing().when(spyController).refreshTableFromStart();
        //Call the protected handleSearch method
        Method handleSearchMethod = StudentsController.class.getDeclaredMethod("handleSearch");
        handleSearchMethod.setAccessible(true);
        handleSearchMethod.invoke(spyController);
        //Verify that refreshTableFromStart has been called
        verify(spyController).refreshTableFromStart();
    }

    @Test
    /**
     * Test for handle import
     */
    public void testHandleImportSuccess() {
        //Check that the dependencies are properly injected.
        assertNotNull(getField(studentsController, "importButton"));
        assertNotNull(getField(studentsController, "importExportService"));
    }

    @Test
    /**
     * Test for handleExport
     */
    public void testHandleExportSuccess(){
        //Check that the dependencies are properly injected.
        assertNotNull(getField(studentsController, "exportButton"));
        assertNotNull(getField(studentsController, "importExportService"));
    }

    @Test
    /**
     * Tests for setupTableColumns
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testSetupTableColumns() throws Exception{
        //Call the protected setupTableColumns method
        Method setupTableColumnsMethod = StudentsController.class.getDeclaredMethod("setupTableColumns");
        setupTableColumnsMethod.setAccessible(true);
        setupTableColumnsMethod.invoke(studentsController);
        //Verify that the columns have been configured
        verify(idColumnMock).setCellValueFactory(any());
        verify(firstNameColumnMock).setCellValueFactory(any());
        verify(lastNameColumnMock).setCellValueFactory(any());
        verify(ageColumnMock).setCellValueFactory(any());
        verify(classNameColumnMock).setCellValueFactory(any());
        verify(averageGradeColumnMock).setCellValueFactory(any());
    }

    @Test
    /**
     * Tests the searchData method for retrieving student data based on search criteria.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testSearchData() throws Exception{
        //Create search criteria
        SearchCriteria criteria = new SearchCriteria("Harry");
        //Config the mock to return data
        when(studentServiceMock.searchStudents(criteria)).thenReturn(testStudents.subList(0, 5));
        //Call the protected searchData method
        Method searchDataMethod = StudentsController.class.getDeclaredMethod("searchData", SearchCriteria.class);
        searchDataMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Student> result = (List<Student>) searchDataMethod.invoke(studentsController, criteria);
        //Verify the result
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(studentServiceMock).searchStudents(criteria);
    }

    @Test
    /**
     * Tests the getTotalCount method for retrieving the total number of students based on search criteria.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testGetTotalCount() throws Exception{
        //Create search criteria
        SearchCriteria criteria = new SearchCriteria("Harry");
        //Config the mock to return a total number
        when(studentServiceMock.getTotalStudents(criteria)).thenReturn(100);
        //Call the protected getTotalCount method
        Method getTotalCountMethod = StudentsController.class.getDeclaredMethod("getTotalCount", SearchCriteria.class);
        getTotalCountMethod.setAccessible(true);
        int result = (int) getTotalCountMethod.invoke(studentsController, criteria);
        //Verify the result
        assertEquals(100, result);
        verify(studentServiceMock).getTotalStudents(criteria);
    }

    @Test
    /**
     * Tests the exportToCSV method for exporting student data to a CSV file.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testExportToCSV() throws Exception{
        //Create data and a test file
        List<Student> studentsToExport = testStudents.subList(0, 5);
        File testFile = new File("test.csv");
        //Call the protected exportToCSV method
        Method exportToCSVMethod = StudentsController.class.getDeclaredMethod("exportToCSV", List.class, File.class);
        exportToCSVMethod.setAccessible(true);
        exportToCSVMethod.invoke(studentsController, studentsToExport, testFile);
        //Verify that the export service has been called
        verify(importExportServiceMock).exportToCSV(studentsToExport, testFile);
    }

    @Test
    /**
     * Tests the clearAddForm method for resetting the add student form fields.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testClearAddForm() throws Exception{
        //Call the private clearAddForm method
        Method clearAddFormMethod = StudentsController.class.getDeclaredMethod("clearAddForm");
        clearAddFormMethod.setAccessible(true);
        clearAddFormMethod.invoke(studentsController);
        //Verify that all fields have been cleared
        verify(firstNameFieldMock).clear();
        verify(lastNameFieldMock).clear();
        verify(ageFieldMock).clear();
        verify(classNameFieldMock).clear();
    }

    @Test
    /**
     * Tests the calculatePageCount method for determining the number of pages 
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testCalculatePageCount() throws Exception{
        //Create a spy to be able to mock getTotalCount
        StudentsController spyController = spy(studentsController);
        //Inject the mocks into the spy
        setField(spyController, "searchField", searchFieldMock);
        //Config the mocks
        when(searchFieldMock.getText()).thenReturn("Harry");
        doReturn(47).when(spyController).getTotalCount(any(SearchCriteria.class));
        //Call the private calculatePageCount method
        Method calculatePageCountMethod = StudentsController.class.getDeclaredMethod("calculatePageCount");
        calculatePageCountMethod.setAccessible(true);
        int result = (int) calculatePageCountMethod.invoke(spyController);
        //Verify the result (47 students / 15 per page = 4 pages)
        assertEquals(4, result);
        verify(spyController).getTotalCount(any(SearchCriteria.class));
    }

    @Test
    /**
     * Tests the loadStudentsPage method for handling exceptions during the loading of student data.
     * @throws Exception if any unexpected error occurs during the test execution
     */
    public void testLoadStudentsPageWithException() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)) {
            //Create a spy to be able to mock searchData
            StudentsController spyController = spy(studentsController);
            //Inject the mocks into the spy
            setField(spyController, "studentService", studentServiceMock);
            setField(spyController, "searchField", searchFieldMock);
            setField(spyController, "studentTable", studentTableMock);
            //Config the mocks to throw an exception
            when(searchFieldMock.getText()).thenReturn("Harry");
            when(studentTableMock.getSortOrder()).thenReturn(FXCollections.observableArrayList());
            doThrow(new RuntimeException("Erreur de test")).when(spyController).searchData(any(SearchCriteria.class));
            //Call the private loadStudentsPage method
            Method loadStudentsPageMethod = StudentsController.class.getDeclaredMethod("loadStudentsPage", int.class);
            loadStudentsPageMethod.setAccessible(true);
            loadStudentsPageMethod.invoke(spyController, 0);
            //Verify that an error alert has been displayed
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), contains("Impossible de charger les données")));
        }
    }

    /**
     * Helper method to set a private field
     * @param target the object containing the field
     * @param fieldName the name of the field to get
     * @param value the value to set the field to
     */
    private void setField(Object target, String fieldName, Object value){
        try{
            Field field = findField(target.getClass(), fieldName);
            if (field != null){
                field.setAccessible(true);
                field.set(target, value);
            }
        } catch (Exception e){
            fail("Erreur lors de l'accès au champ " + fieldName + ": " + e.getMessage());
        }
    }

    /**
     *  Helper method to get the value of a private field
     * @param target the object containing the field
     * @param fieldName the name of the field to get
     * @return the value of the field
     */
    private Object getField(Object target, String fieldName){
        try {
            Field field = findField(target.getClass(), fieldName);
            if (field != null){
                field.setAccessible(true);
                return field.get(target);
            }
        } catch (Exception e){
            fail("Erreur lors de l'accès au champ " + fieldName + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Helper method to find a field by its name
     * @param clazz the class containing the field
     * @param fieldName the name of the field to find
     * @return the found field or null if not found
     */
    private Field findField(Class<?> clazz, String fieldName) {
        try{
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e){
            //If the field does not exist in the current class, search in superclasses
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null){
                return findField(superClass, fieldName);
            }
            return null;
        }
    }
}