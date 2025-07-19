package com.studentmanagement.controller;


import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.GradeService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.SubjectCommentService;

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
