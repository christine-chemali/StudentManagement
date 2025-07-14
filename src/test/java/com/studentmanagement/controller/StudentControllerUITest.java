package com.studentmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.GradeService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.SubjectCommentService;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.SubjectResult;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentControllerUITest {
    
       private StudentController studentController;
    
    @Mock
    private StudentService studentServiceMock;
    @Mock
    private GradeService gradeServiceMock;
    @Mock
    private SubjectCommentService commentServiceMock;
    
    private TextField studentIdField;
    private ComboBox<String> subjectComboBox;
    private TextField gradeField;
    private TextField coefficientField;
    private TextArea commentArea;
    private TableView<SubjectResult> gradesTable;
    private Label studentNameLabel;
    private Label studentClassLabel;
    
    //Mock student data
    private Student mockStudent;
    private List<SubjectResult> mockSubjectResults;

     //Config to show the GUI during tests
    static {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "false");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "false");
            }
    
    @Start
    public void start(Stage stage) throws Exception{
        //Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student.fxml"));
        Parent root = loader.load();
        studentController = loader.getController();
        
        //Inject service mocks
        studentController.setStudentService(studentServiceMock);
        studentController.setGradeService(gradeServiceMock);
        studentController.setCommentService(commentServiceMock);
        
        //Config and display the scene
        stage.setScene(new Scene(root));
        stage.setTitle("Student Controller Test");
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp(FxRobot robot) {
        setupMockData();
        
        //Retrieve references to UI elements with proper type casting
        studentIdField = robot.lookup("#studentIdField").queryAs(TextField.class);

        @SuppressWarnings("unchecked")
        ComboBox<String> tempComboBox = (ComboBox<String>) robot.lookup("#subjectComboBox").query();
        subjectComboBox = tempComboBox;
        
        gradeField = robot.lookup("#gradeField").queryAs(TextField.class);
        coefficientField = robot.lookup("#coefficientField").queryAs(TextField.class);
        commentArea = robot.lookup("#commentArea").queryAs(TextArea.class);
        
        @SuppressWarnings("unchecked")
        TableView<SubjectResult> tempTableView = (TableView<SubjectResult>) robot.lookup("#gradesTable").query();
        gradesTable = tempTableView;
        
        studentNameLabel = robot.lookup("#studentNameLabel").queryAs(Label.class);
        studentClassLabel = robot.lookup("#studentClassLabel").queryAs(Label.class);
    }

        private void setupMockData() {
        //Create mock student with data
        mockStudent = mock(Student.class);
        when(mockStudent.getStudentId()).thenReturn(1L);
        when(mockStudent.getFirstName()).thenReturn("Jean");
        when(mockStudent.getLastName()).thenReturn("Dupont");
        when(mockStudent.getFullName()).thenReturn("Jean Dupont");
        when(mockStudent.getStudentClassName()).thenReturn("5A");
        
        //Create mock subject results
        mockSubjectResults = new ArrayList<>();
        
        SubjectResult frenchResult = new SubjectResult("Français", "14.0 (coef: 2), 16.0 (coef: 1)", 14.67, 9.0, 17.5, "Très bien");
        SubjectResult englishResult = new SubjectResult("Anglais", "15.0 (coef: 1), 13.5 (coef: 2)", 14.0, 10.0, 18.0, "Bon travail");
        SubjectResult mathResult = new SubjectResult("Mathématiques", "15.5 (coef: 2), 12.0 (coef: 1)", 14.0, 8.5, 18.0, "Bon travail");
        SubjectResult historyResult = new SubjectResult("Histoire", "14.0 (coef: 1), 12.0 (coef: 1)", 13.0, 9.0, 17.0, "Peut mieux faire");
        SubjectResult geographyResult = new SubjectResult("Géographie", "13.5 (coef: 1), 15.0 (coef: 1)", 14.25, 10.5, 16.0, "Bonne progression");
        SubjectResult physicsResult = new SubjectResult("Sciences Physiques", "12.0 (coef: 2), 14.5 (coef: 1)", 12.83, 9.5, 17.5, "Effort à maintenir");
        SubjectResult biologyResult = new SubjectResult("Sciences de la Vie et de la Terre", "16.0 (coef: 1), 13.0 (coef: 2)", 14.0, 11.0, 18.0, "Très intéressé");
        SubjectResult artResult = new SubjectResult("Arts Plastiques", "15.0 (coef: 1), 17.0 (coef: 1)", 16.0, 12.0, 19.0, "Créatif");
        SubjectResult sportsResult = new SubjectResult("Éducation Physique et Sportive", "14.5 (coef: 1), 16.5 (coef: 1)", 15.5, 11.5, 18.5, "Bon esprit d'équipe");
        SubjectResult techResult = new SubjectResult("Technologie", "13.0 (coef: 1), 15.5 (coef: 1)", 14.25, 10.0, 17.0, "Habile de ses mains");
        SubjectResult musicResult = new SubjectResult("Musique", "16.5 (coef: 1), 14.0 (coef: 1)", 15.25, 12.5, 18.0, "Bon sens du rythme");
        SubjectResult spanishResult = new SubjectResult("Espagnol", "12.5 (coef: 1), 14.0 (coef: 1)", 13.25, 9.0, 16.5, "Effort en expression orale");
        SubjectResult germanResult = new SubjectResult("Allemand", "15.0 (coef: 1), 13.5 (coef: 1)", 14.25, 10.5, 17.0, "Bonne compréhension");
        
        mockSubjectResults.add(frenchResult);
        mockSubjectResults.add(englishResult);
        mockSubjectResults.add(mathResult);
        mockSubjectResults.add(historyResult);
        mockSubjectResults.add(geographyResult);
        mockSubjectResults.add(physicsResult);
        mockSubjectResults.add(biologyResult);
        mockSubjectResults.add(artResult);
        mockSubjectResults.add(sportsResult);
        mockSubjectResults.add(techResult);
        mockSubjectResults.add(musicResult);
        mockSubjectResults.add(spanishResult);
        mockSubjectResults.add(germanResult);
        
        //Create mock student for invalid ID test
        Student mockStudentMultiple = mock(Student.class);
        when(mockStudentMultiple.getStudentId()).thenReturn(2L);
        when(mockStudentMultiple.getFirstName()).thenReturn("Alice");
        when(mockStudentMultiple.getLastName()).thenReturn("Durand");
        when(mockStudentMultiple.getFullName()).thenReturn("Alice Durand");
        when(mockStudentMultiple.getStudentClassName()).thenReturn("6B");
        
        //Set up mock behaviors
        when(studentServiceMock.getStudentByID(1L)).thenReturn(mockStudent);
        when(studentServiceMock.getStudentByID(2L)).thenReturn(mockStudentMultiple);
        when(studentServiceMock.getStudentByID(999L)).thenReturn(null);
        
        when(gradeServiceMock.searchBySubject(eq(1L), any(SearchCriteria.class))).thenReturn(mockSubjectResults);
        when(gradeServiceMock.searchBySubject(eq(2L), any(SearchCriteria.class))).thenReturn(mockSubjectResults);
        when(gradeServiceMock.countBySubject(eq(1L), anyString())).thenReturn(mockSubjectResults.size());
        when(gradeServiceMock.countBySubject(eq(2L), anyString())).thenReturn(mockSubjectResults.size());
    }
    
    @AfterEach
    public void tearDown() throws Exception{
        FxToolkit.cleanupStages();
    }


}
