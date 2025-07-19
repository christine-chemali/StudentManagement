package com.studentmanagement.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.studentmanagement.model.Grade;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.SubjectComment;
import com.studentmanagement.service.GradeService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.SubjectCommentService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.GradeValidator;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentControllerUnitTest {
    
    private StudentController studentController;

    @Mock
    private StudentService studentServiceMock;
    @Mock
    private GradeService gradeServiceMock;
    @Mock
    private SubjectCommentService commentServiceMock;
    @Mock
    private TextField studentIdFieldMock;
    @Mock
    private Label studentNameLabelMock;
    @Mock
    private Label studentClassLabelMock;
    @Mock
    private ComboBox<String> subjectComboBoxMock;
    @Mock
    private TextField gradeFieldMock;
    @Mock
    private TextField coefficientFieldMock;
    @Mock
    private TextArea commentAreaMock;
    @Mock
    private Pagination paginationMock;

    private Student testStudent;

    @BeforeEach
    /**
     * Sets up the testing environement before each test case
     * This method initializes the StudentController and injects mock services and UI components
     * It also set up default behavior for the mocked components and creates a test student object
     * @throws Exception if an error occurs during the setup process
     */
    public void setUp() throws Exception{
        //Create the controller
        studentController = new StudentController();

        //Inject the mocked services
        injectField(studentController, "studentService", studentServiceMock);
        injectField(studentController, "gradeService", gradeServiceMock);
        injectField(studentController, "commentService", commentServiceMock);
        
        //Injected the mocked UI components
        injectField(studentController, "studentIdField", studentIdFieldMock);
        injectField(studentController, "studentNameLabel", studentNameLabelMock);
        injectField(studentController, "studentClassLabel", studentClassLabelMock);
        injectField(studentController, "subjectComboBox", subjectComboBoxMock);
        injectField(studentController, "gradeField", gradeFieldMock);
        injectField(studentController, "coefficientField", coefficientFieldMock);
        injectField(studentController, "commentArea", commentAreaMock);
        //Attempt to inject the mock pagination with different possible names
        try{
            injectField(studentController, "pagination", paginationMock);
        } catch (NoSuchFieldException e){
            //Try other possible names
            try{
                injectField(studentController, "tablePagination", paginationMock);
            } catch (NoSuchFieldException e2){
                try{
                    injectField(studentController, "studentTablePagination", paginationMock);
                } catch (NoSuchFieldException e3){
                    System.out.println("Aucun champ de paginatio trouvé, les tests utiliseront des spies");
                }
            }
        }
        //Create a test student
        testStudent = new Student();
        testStudent.setFirstName("Harry");
        testStudent.setLastName("Potter");
        testStudent.setClassName("3A");
        //Configure the default behavior of mocks
        when(studentIdFieldMock.getText()).thenReturn("");
        when(subjectComboBoxMock.getValue()).thenReturn("Métamorphose");
        when(gradeFieldMock.getText()).thenReturn("15.5");
        when(commentAreaMock.getText()).thenReturn("Harry, ta tentative de transformer ton livre de métamorphose en un dragon était, disons, ambitieuse! Malheureusement, je ne pense pas que la bibliothèque soit prête pour un nouvel occupant cracheur de feu. Peut-être que la prochaine fois, tu pourrais commmencer par quelque chose d'un peu moins ... flamboyant ? Une grenouille, par exemple! Rapelle-toi, même les plus grands sorciers ont commencé par faire sauter des couvercles de chaudrons avant de se lancer dans des transformations spectaculaires !");
        when(paginationMock.getCurrentPageIndex()).thenReturn(0);
    }

    @Test
    /**
     * Tests the functionnality of loading a student with a valid student ID
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleLoadStudentWithValidId() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            long studentId = 123L;

            when(studentIdFieldMock.getText()).thenReturn(String.valueOf(studentId));
            when(studentServiceMock.getStudentByID(studentId)).thenReturn(testStudent);

            Method handleLoadStudentMethod = StudentController.class.getDeclaredMethod("handleLoadStudent");
            handleLoadStudentMethod.setAccessible(true);
            handleLoadStudentMethod.invoke(studentController);

            verify(studentServiceMock).getStudentByID(studentId);
            verify(studentNameLabelMock).setText("Nom de l'étudiant : " + testStudent.getFullName());
            verify(studentClassLabelMock).setText("Classe : " + testStudent.getClassName());

            Field currentStudentField = StudentController.class.getDeclaredField("currentStudent");
            currentStudentField.setAccessible(true);
            Student currentStudent = (Student) currentStudentField.get(studentController);
            assertNotNull(currentStudent);
            assertEquals(testStudent.getFirstName(), currentStudent.getFirstName());
            
        }
    }

    @Test
    /**
     * Tests the functionality of loading a student with an invalid student ID
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleLoadStudentWithInvalidId() throws Exception {
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            when(studentIdFieldMock.getText()).thenReturn("Lumos");

            Method handleLoadStudentMethod = StudentController.class.getDeclaredMethod("handleLoadStudent");
            handleLoadStudentMethod.setAccessible(true);
            handleLoadStudentMethod.invoke(studentController);
            
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("L'ID étudiant doit être un nombre entier.")));
        }
    }

    @Test
    /**
     * Tests the functionality of loading a student with an empty student ID
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleLoadStudentWithEmptyId() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            when(studentIdFieldMock.getText()).thenReturn("");

            Method handleLoadStudentMethod = StudentController.class.getDeclaredMethod("handleLoadStudent");
            handleLoadStudentMethod.setAccessible(true);
            handleLoadStudentMethod.invoke(studentController);

            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("Tu dois remplir l'ID d'étudiant !")));
        }
    }

    @Test
    /**
     * Tests the functionnality of loading a student when the student ID does not correspond to any existing student
     * @throws Exception if an error occurs during test execution
     */
    public void testHandleLoadStudentNotFound() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)) {
            long studentId = 999L;
            
            when(studentIdFieldMock.getText()).thenReturn(String.valueOf(studentId));
            when(studentServiceMock.getStudentByID(studentId)).thenReturn(null);
            
            Method handleLoadStudentMethod = StudentController.class.getDeclaredMethod("handleLoadStudent");
            handleLoadStudentMethod.setAccessible(true);
            handleLoadStudentMethod.invoke(studentController);
            
            verify(studentServiceMock).getStudentByID(studentId);
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("Aucun étudiant trouvé avec cet ID.")));
        }
    }

    @Test
    /**
     * Tests the functionnality of successfuly adding a grade for a student
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleAddGradeSuccess() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class);
             MockedStatic<GradeValidator> gradeValidatorMock = mockStatic(GradeValidator.class)){
            
            injectField(studentController, "currentStudent", testStudent);
            
            when(subjectComboBoxMock.getValue()).thenReturn("Métamorphoses");
            when(gradeFieldMock.getText()).thenReturn("15.5");
            when(coefficientFieldMock.getText()).thenReturn("2.0");
            
            //Create a valid validation result
            GradeValidator.ValidationResult validResult = mock(GradeValidator.ValidationResult.class);
            when(validResult.isValid()).thenReturn(true);
            
            //Create a test grade
            Grade testGrade = new Grade();
            testGrade.setStudentId(testStudent.getStudentId());
            testGrade.setSubject("Métamorphoses");
            testGrade.setValue(15.5);
            testGrade.setCoefficient(2.0);
            
            //Configure the mocks statics
            gradeValidatorMock.when(() -> GradeValidator.validateGradeInput(gradeFieldMock, coefficientFieldMock))
                             .thenReturn(validResult);
            gradeValidatorMock.when(() -> GradeValidator.createGradeFromFields(
                testStudent.getStudentId(), "Métamorphoses", gradeFieldMock, coefficientFieldMock))
                             .thenReturn(testGrade);
            
            //Create a spy of the controller to mock refreshTable
            StudentController spyController = spy(studentController);
            doNothing().when(spyController).refreshTable();
            
            //Call the method on the spy
            Method handleAddGradeMethod = StudentController.class.getDeclaredMethod("handleAddGrade");
            handleAddGradeMethod.setAccessible(true);
            handleAddGradeMethod.invoke(spyController);

            verify(gradeServiceMock).saveGrade(testGrade);
            verify(gradeFieldMock).clear();
            verify(coefficientFieldMock).clear();
            alertUtilsMock.verify(() -> AlertUtils.showInformation(eq("Succès"), eq("La note a été ajoutée avec succès.")));
        }
    }

    
    @Test
    /**
     * Tests the functionality of adding a grade when no subject is selected.
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleAddGradeWithoutSubject() throws Exception {
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)) {

            when(subjectComboBoxMock.getValue()).thenReturn(null);
            
            Method handleAddGradeMethod = StudentController.class.getDeclaredMethod("handleAddGrade");
            handleAddGradeMethod.setAccessible(true);
            handleAddGradeMethod.invoke(studentController);
            
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("Tu dois sélectionner une matière!")));
        }
    }

    @Test
    /**
     *  Tests the functionality of adding a grade with invalid input values.
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleAddGradeWithInvalidInput() throws Exception {
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class);
             MockedStatic<GradeValidator> gradeValidatorMock = mockStatic(GradeValidator.class)) {
            
            injectField(studentController, "currentStudent", testStudent);
            
            when(subjectComboBoxMock.getValue()).thenReturn("Métamorphoses");
            
            //Create an invalid validation result
            GradeValidator.ValidationResult invalidResult = mock(GradeValidator.ValidationResult.class);
            when(invalidResult.isValid()).thenReturn(false);
            when(invalidResult.getErrorMessage()).thenReturn("Note invalide");
            
            //Configure the static mock
            gradeValidatorMock.when(() -> GradeValidator.validateGradeInput(gradeFieldMock, coefficientFieldMock))
                             .thenReturn(invalidResult);
            
            Method handleAddGradeMethod = StudentController.class.getDeclaredMethod("handleAddGrade");
            handleAddGradeMethod.setAccessible(true);
            handleAddGradeMethod.invoke(studentController);
            
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("Note invalide")));
        }
    }

  @Test
  /**
   * Tests the functionality of successfully adding a comment for a student.
   * @throws Exception if an error occurs during the test excecution
   */
    public void testHandleAddCommentSuccess() throws Exception {
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)) {

            injectField(studentController, "currentStudent", testStudent);

            when(subjectComboBoxMock.getValue()).thenReturn("Potions");
            when(commentAreaMock.getText()).thenReturn("A réussi à créer une potion qui a explosé ! Prometteur, mais peut-être un peu trop d'energie a dépenser ...");
            
            //Create a spy of the controller to mock refreshTable
            StudentController spyController = spy(studentController);
            doNothing().when(spyController).refreshTable();
            
            //Call the method on the spy
            Method handleAddCommentMethod = StudentController.class.getDeclaredMethod("handleAddComment");
            handleAddCommentMethod.setAccessible(true);
            handleAddCommentMethod.invoke(spyController);
            
            verify(commentServiceMock).saveComment(any(SubjectComment.class));
            verify(commentAreaMock).clear();
            alertUtilsMock.verify(() -> AlertUtils.showInformation(eq("Succès"), eq("Le commentaire a été ajouté avec succès.")));
        }
    }

    @Test
    /**
     * Tests the functionality of adding a comment when no subject is selected.
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleAddCommentWithoutSubject() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){

            when(subjectComboBoxMock.getValue()).thenReturn(null);
            
            Method handleAddCommentMethod = StudentController.class.getDeclaredMethod("handleAddComment");
            handleAddCommentMethod.setAccessible(true);
            handleAddCommentMethod.invoke(studentController);
            
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), eq("Sélectionne une matière !")));
        }
    }

    @Test
    /**
     * Tests the functionality of adding a comment when the comment area is empty.
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleAddCommentWithEmptyComment() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){

            injectField(studentController, "currentStudent", testStudent);
            
            when(subjectComboBoxMock.getValue()).thenReturn("Potions");
            when(commentAreaMock.getText()).thenReturn("   ");
            
            Method handleAddCommentMethod = StudentController.class.getDeclaredMethod("handleAddComment");
            handleAddCommentMethod.setAccessible(true);
            handleAddCommentMethod.invoke(studentController);
            
            alertUtilsMock.verify(() -> AlertUtils.showInformation(eq("Information"), 
                eq("Il n'y a pas de commentaire à ajouter.\nRédiges-en un !")));
            verify(commentAreaMock).requestFocus();
        }
    }

    @Test
    /*
     * Tests the functionality of setting a new StudentService in the StudentController.
     */
    public void testSetStudentService() {
        StudentService newService = mock(StudentService.class);
        studentController.setStudentService(newService);
        
        assertNotNull(newService);
    }

    @Test
    /*
     * Tests the functionality of setting a new GradeService in the StudentController.
     */
    public void testSetGradeService() {
        GradeService newService = mock(GradeService.class);
        studentController.setGradeService(newService);
        
        assertNotNull(newService);
        
    }

    @Test
    /**
     * Tests the functionality of setting a new CommentService in the StudentController.
     */
    public void testSetCommentService() {
        SubjectCommentService newService = mock(SubjectCommentService.class);
        studentController.setCommentService(newService);
        
        assertNotNull(newService);
    }

    @Test
    /**
     * Tests the functionality of handling sorting in the StudentController.
     * @throws Exception if an error occurs during the test execution
     */
    public void testHandleSort() throws Exception{
        try (MockedStatic<Platform> platformMock = mockStatic(Platform.class)){

            injectField(studentController, "currentStudent", testStudent);
            
            //Configure Platform.runLater to execute immediately
            platformMock.when(() -> Platform.runLater(any(Runnable.class)))
                      .thenAnswer(invocation -> {
                          Runnable runnable = invocation.getArgument(0);
                          runnable.run();
                          return null;
                      });
            
            //Create a spy of the controller to mock refreshTable
            StudentController spyController = spy(studentController);
            doNothing().when(spyController).refreshTable();
            
            Method handleSortMethod = StudentController.class.getDeclaredMethod("handleSort");
            handleSortMethod.setAccessible(true);
            handleSortMethod.invoke(spyController);
            
            //Verify that refreshTable has been called
            verify(spyController).refreshTable();
        }
    }

    /**
     * Injects a value into a private field of the specified target object
     * @param target the object containing the field to inject to
     * @param fieldName the name of the field to inject into
     * @param value the value to inject into the field
     * @throws Exception if an error occurs during reflection ot it the field is not found
     */
    private void injectField(Object target, String fieldName, Object value) throws Exception{
        try{
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e){
            Class<?> currentClass = target.getClass().getSuperclass();
            while (currentClass !=null){
                try{
                    Field field = currentClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(target, value);
                    return;
                } catch (NoSuchFieldException ignored){
                    currentClass = currentClass.getSuperclass();
                }
            }
            throw e;
        }
    }
}
