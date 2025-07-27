package com.studentmanagement.controller;

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
import com.studentmanagement.utils.StudentValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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