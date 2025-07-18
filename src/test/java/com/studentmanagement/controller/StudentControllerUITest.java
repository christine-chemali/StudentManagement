package com.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import com.studentmanagement.model.Grade;
import com.studentmanagement.model.Student;
import com.studentmanagement.service.GradeService;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.service.SubjectCommentService;
import com.studentmanagement.utils.SearchCriteria;
import com.studentmanagement.utils.SubjectResult;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;


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
    /**
     * Initializes the test environement by loading the FXML and setting up the controller
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs while loading the FXML
     */
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
    /**
     * Sets up the testing environement before each test case
     * @param robot the FxRobot instance used for simulating user input
     */
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
    /**
     * Cleans up the testing environement after each test case
     * @throws Exception if an error occurs during cleanup
     */
    public void tearDown() throws Exception{
        FxToolkit.cleanupStages();
    }

    @Test
    /**
     * Tests the initial state of the UI components
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testInitialState(FxRobot robot){
        //Verify that controls are disabled initially
        assertThat(subjectComboBox.isDisabled()).isTrue();
        assertThat(gradeField.isDisabled()).isTrue();
        assertThat(coefficientField.isDisabled()).isTrue();
        assertThat(commentArea.isDisabled()).isTrue();
        //Verify that student info labels are empty or have default text
        assertThat(studentNameLabel.getText()).contains("Nom de l'étudiant");
        assertThat(studentClassLabel.getText()).contains("Classe");
    }

     @Test
     /**
      * Tests the succesful loading of a student
      * @param robot the FxRobot instance used for simulating user input
      */
    public void testSuccessfulStudentLoad(FxRobot robot){
        //Load a valid student
        robot.clickOn(studentIdField).write("1");
        robot.clickOn("#okButton");
        //Verify student service was called
        verify(studentServiceMock).getStudentByID(1L);
        //Verify UI is updated
        assertThat(studentNameLabel.getText()).contains("Jean Dupont");
        assertThat(studentClassLabel.getText()).contains("5A");   
        //Verify controls are enabled
        assertThat(subjectComboBox.isDisabled()).isFalse();
        assertThat(gradeField.isDisabled()).isFalse();
        assertThat(coefficientField.isDisabled()).isFalse();
        assertThat(commentArea.isDisabled()).isFalse();
        //Verify table is populated with all subjects
        assertThat(gradesTable.getItems()).hasSize(13);
        assertThat(gradesTable.getItems().get(0).getSubject()).isEqualTo("Français");
        assertThat(gradesTable.getItems().get(1).getSubject()).isEqualTo("Anglais");
        assertThat(gradesTable.getItems().get(2).getSubject()).isEqualTo("Mathématiques");
    }

    @Test
    /**
     * Tests the scenario where an invalid student ID is entered
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testInvalidStudentId(FxRobot robot){
        //Try to load non existent student
        robot.clickOn(studentIdField).write("999");
        robot.clickOn("#okButton");
        //Verify student service was called
        verify(studentServiceMock).getStudentByID(999L);
        //Close error dialog
        try{
            robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        } catch (Exception e){
            //dialog might not appear or already closed
        }
        //Verify controld remain disabled
        assertThat(subjectComboBox.isDisable()).isTrue();
        assertThat(gradeField.isDisable()).isTrue();
        assertThat(coefficientField.isDisable()).isTrue();
        assertThat(commentArea.isDisable()).isTrue();
    }

    @Test
    /**
     * Tests the scenario where the student ID field is empty
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testEmptyStudentId(FxRobot robot){
        //Try to load without entering student ID
        robot.clickOn("#okButton");  
        //Verify student service was not called
        verify(studentServiceMock, never()).getStudentByID(anyLong());
        //Close error dialog if it appears
        try {
            robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        } catch (Exception e) {
            // Dialog might not appear or might be closed already
        } 
        // Verify controls remain disabled
        assertThat(subjectComboBox.isDisabled()).isTrue();
    }

    @Test
    /**
     * Tests the scenario where an invalid student ID format is entered
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testInvalidStudentIdFormat(FxRobot robot){
        //Try to load with invalid format
        robot.clickOn(studentIdField).write("abc");
        robot.clickOn("#okButton");
        //Verify student service was not called
        verify(studentServiceMock, never()).getStudentByID(anyLong());
        //Close error dialog if it appears
        try{
            robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        } catch (Exception e){
            //Dialog might not appear or might be closed already
        }
        //Verify field content
        assertThat(studentIdField.getText()).isEqualTo("abc");
    }

    @Test
    /**
     * Tests the successful addition of a grade
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testAddGradeSuccess(FxRobot robot){
        //Activate logging
        System.out.println("Début du test testAddGradeSuccess");
        try{
            //Setup a dialog handler that automatically responds to alerts
            setupAutomaticDialogHandler();
            //Load a valid student
            System.out.println("Chargement de l'étudiant ...");
            loadValidStudent(robot);
            System.out.println("Etudiant chargé avec succès");
            //Wait for the UI to update
            WaitForAsyncUtils.waitForFxEvents();
            //Check the initial state of the console
            System.out.println("Etat du subjectComboBox : " + (subjectComboBox.isDisabled() ? "désactivé" : "activé"));
            //Select a subject 
            ComboBox<String> comboBox = robot.lookup("#subjectComboBox").queryComboBox();
            System.out.println("ComboBox trouvé : " + comboBox);
            System.out.println("Items dans le ComboBox : " + comboBox.getItems());
            robot.interact(() -> {
                if(!comboBox.getItems().isEmpty()){
                    comboBox.getSelectionModel().select("Mathématiques");
                    System.out.println("Matière sélectionnée : " + comboBox.getValue());
                } else{
                    System.out.println("Le ComboBox est vide !");
                }
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Fill in the grade field
            System.out.println("Remplissage du champ de note ... ");
            TextField gradeTextField = robot.lookup("#gradeField").queryAs(TextField.class);
            robot.interact(() -> {
                gradeTextField.clear();
                gradeTextField.setText("16.5");
                System.out.println("Texte du champ de note après saisie : " + gradeTextField.getText());
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Fill in the coefficient field
            System.out.println("Remplissage du champ de coefficient ...");
            TextField coeffTextField = robot.lookup("#coefficientField").queryAs(TextField.class);
            robot.interact(() -> {
                coeffTextField.clear();
                coeffTextField.setText("2");
                System.out.println("Texte du champ de coefficient après saisie : " + coeffTextField.getText());
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Check the state before clicking the button
            System.out.println("Vérification avant clic sur le bouton : ");
            System.out.println("- Matière : " + comboBox.getValue());
            System.out.println("- Note : " + gradeTextField.getText());
            System.out.println(" - Coefficient : " + coeffTextField.getText());
            //Click the add button
            Button addButton = robot.lookup("#addGradeButton").queryButton();
            System.out.println("Etat du bouton : " + (addButton.isDisabled() ? "désactivé" : "activé"));
            if(!addButton.isDisabled()){
                System.out.println("Clic sur le bouton d'ajout ...");
                robot.interact(() -> addButton.requestFocus());
                WaitForAsyncUtils.waitForFxEvents();
                //Directly triggering the action event
                robot.interact(() -> {
                    System.out.println("Déclenchement direct de l'évenement d'action ...");
                    Event.fireEvent(addButton, new ActionEvent(addButton, null));
                });
                System.out.println("Evenement déclenché");
            } else {
                System.out.println("Le bouton est désactivé, impossible de cliquer");
            }
            //Wait for the action to be processed
            WaitForAsyncUtils.waitForFxEvents();
            //Verify that the service was called 
            System.out.println("Vérification de l'appel au service ...");
            verify(gradeServiceMock, timeout(5000)).saveGrade(any(Grade.class));
            System.out.println("Service appelé avec succès");
            //Wait for the field to be cleared
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> gradeTextField.getText().isEmpty() && coeffTextField.getText().isEmpty());
            //Check the final state
            System.out.println("Etat final : ");
            System.out.println("- Champ de note : " + gradeTextField.getText());
            System.out.println("- Champ de coefficient : " + coeffTextField.getText());
        } catch (Exception e){
            System.out.println("Exception dans le test : " + e.getClass().getName() + ":" + e.getMessage());
            e.printStackTrace();
            fail("Le test a échoué avec une exception : " + e.getMessage());
        }
    }

    @Test
    /**
     * Tests the scenario where an invalid grade format is entered
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testAddGradeInvalidFormat(FxRobot robot){
        System.out.println("Début du test testAddGradeInvalidFormat");
        try{
            //Set up a dialog handler that automatically responds to alerts
            setupAutomaticDialogHandler();
            //Load a valid Student
            System.out.println("Chargement de l'étudiant");
            loadValidStudent(robot);
            System.out.println("Etudiant chargé avec succès");
            //Wait for the UI to update
            WaitForAsyncUtils.waitForFxEvents();
            //Select a subject
            ComboBox<String> comboBox = robot.lookup("#subjectComboBox").queryComboBox();
            robot.interact(() -> {
                comboBox.getSelectionModel().select("Français");
                System.out.println("Matière selectionnée : " + comboBox.getValue());
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Fill in the grade field with an invalid format text
            System.out.println("Saisie d'une note au format invalide (texte) ...");
            TextField gradeTextField = robot.lookup("#gradeField").queryAs(TextField.class);
            robot.interact(() -> {
                gradeTextField.clear();
                gradeTextField.setText("quinze");
            });
            //Fill in the coeff field with a valid value
            TextField coeffTextField = robot.lookup("#coefficientField").queryAs(TextField.class);
            robot.interact(() -> {
                coefficientField.clear();
                coefficientField.setText("1");
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Click the add button
            Button addButton = robot.lookup("#addGradeButton").queryButton();
            System.out.println("Clic sur le bouton d'ajout ...");
            robot.interact(() -> addButton.requestFocus());
            WaitForAsyncUtils.waitForFxEvents();
            robot.interact(() -> {
                Event.fireEvent(addButton, new ActionEvent(addButton, null));
            });
            //Wait for the action to be processed
            WaitForAsyncUtils.waitForFxEvents();
            //Verify that the service was not called because the format is invalid
            System.out.println("Vérification que le service n'a pas été apellé ...");
            verify(gradeServiceMock, never()).saveGrade(any(Grade.class));
            //Verify that the fields are not cleared because the opertation failed
            assertThat(gradeTextField.getText()).isEqualTo("quinze");
            assertThat(coeffTextField.getText()).isEqualTo("1");
            System.out.println("Test réussi : le format invalide a été rejeté");
        } catch (Exception e){
            System.out.println("Exception dans le test : " + e.getMessage());
            e.printStackTrace();
            fail("Le test a échoué avec une exception : " + e.getMessage());
        }
    }

    @Test
    /**
     * Tests the scenario where a grade out of range is entered
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testAddGradeOutOfRange(FxRobot robot){
        System.out.println("Début du test testGradeOutOfRange");
        try{
            //Set up a dialog handler that automatically responds to alerts
            setupAutomaticDialogHandler();
            //Load a valid student
            System.out.println("Chargement de l'étudiant ...");
            loadValidStudent(robot);
            System.out.println("Etudiant chargé avec succès");
            //Wait for the UI to update
            WaitForAsyncUtils.waitForFxEvents();
            //Select a subject
            ComboBox<String> comboBox = robot.lookup("#subjectComboBox").queryComboBox();
            robot.interact(() -> {
                comboBox.getSelectionModel().select("Mathématiques");
                System.out.println("Matière sélectionnée : " + comboBox.getValue());
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Fill in the grade field with an out-of-range value (>20)
            System.out.println("Saisie d'une note hors limites (25) ...");
            TextField gradeTextField = robot.lookup("#gradeField").queryAs(TextField.class);
            robot.interact(() -> {
                gradeTextField.clear();
                gradeTextField.setText("25");
            });
            //Fill in the coefficient field with a valid value
            TextField coeffTextField = robot.lookup("#coefficientField").queryAs(TextField.class);
            robot.interact(() -> {
                coeffTextField.clear();
                coeffTextField.setText("2");
            });
            WaitForAsyncUtils.waitForFxEvents();
            //CLick the add button
            Button addButton = robot.lookup("#addGradeButton").queryButton();
            System.out.println("Clic sur le bouton d'ajout ...");
            robot.interact(() -> addButton.requestFocus());
            WaitForAsyncUtils.waitForFxEvents();
            robot.interact(() -> {
                Event.fireEvent(addButton, new ActionEvent(addButton, null));
            });
            //Wait for the action to be processed
            WaitForAsyncUtils.waitForFxEvents();
            //Verify that the service was not called
            System.out.println("Vérification que le service n'a pas été appelé ...");
            verify(gradeServiceMock, never()).saveGrade(any(Grade.class));
            //Verify that the fields are not cleared because the operation failed
            assertThat(gradeTextField.getText()).isEqualTo("25");
            assertThat(coeffTextField.getText()).isEqualTo("2");
            System.out.println("Test réussi : la note hors limites a été rejetée");
        } catch (Exception e){
            System.out.println("Exception dans le test : " + e.getMessage());
            e.printStackTrace();
            fail("le test a echoué avec execption : " + e.getMessage());
        }
    }

    @Test
    /**
     * Tests the scenario where a grade is added without selecting a subject
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testAddGradeWithoutSubject(FxRobot robot){
        System.out.println("Début du test testAddGradeWithoutSubject");
        try{
            //Set up a dialog handler that automatically responds to alerts
            setupAutomaticDialogHandler();
            //Load a valid student
            System.out.println("Chargement de l'étudiant ...");
            loadValidStudent(robot);
            System.out.println("Etudiant chargé avec succès");
            //Wait for the UI to update
            WaitForAsyncUtils.waitForFxEvents();
            //Do not select a subject, leave the ComboBox empty
            ComboBox<String> comboBox = robot.lookup("#subjectComboBox").queryComboBox();
            robot.interact(() -> {
                comboBox.getSelectionModel().clearSelection();
                System.out.println("Aucune matière selectionnée");
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Fill in the grade field with a valid value
            System.out.println("Saisie d'une note valide ...");
            TextField gradeTextField = robot.lookup("#gradeField").queryAs(TextField.class);
            robot.interact(() -> {
                gradeTextField.clear();
                gradeTextField.setText("15");
            });
            //Fill in the coefficient field with a valid value
            System.out.println("Saisie d' un coefficient valide ...");
            TextField coeffTextField = robot.lookup("#coefficientField").queryAs(TextField.class);
            robot.interact(() -> {
                coeffTextField.clear();
                coeffTextField.setText("2");
            });
            WaitForAsyncUtils.waitForFxEvents();
            //Click the add Button
            Button addButton = robot.lookup("#addGradeButton").queryButton();
            System.out.println("Clic sur le bouton d'ajout ...");
            robot.interact(() -> addButton.requestFocus());
            WaitForAsyncUtils.waitForFxEvents();
            robot.interact(() ->{
                Event.fireEvent(addButton, new ActionEvent(addButton, null));
            });
            //Wait for the action to be processed
            WaitForAsyncUtils.waitForFxEvents();
            //Verify that the service was not called because no subject is selected
            System.out.println("Vérification que le service n'a pas été appelé ...");
            verify(gradeServiceMock, never()).saveGrade(any(Grade.class));
            //Verify that the fields are not cleared because the operation failed
            assertThat(gradeTextField.getText()).isEqualTo("15");
            assertThat(coeffTextField.getText()).isEqualTo("2");
            System.out.println("Test réussi : L'ajout sans matière a été rejeté");
        } catch (Exception e){
            System.out.println("Exception dans le test : " + e.getMessage());
            e.printStackTrace();
            fail("Le test a échoué avec une exception : " + e.getMessage());
        }
    }

    /**
     * Sets up a handler for dialog boxes to automatically respond to alerts
     */
    private void setupAutomaticDialogHandler(){
        //Set up a handler for dialog boxes
        Thread dialogWatcherThread = new Thread(() ->{
            try{
                while(true){
                    //Check if a dialog box is present
                    Platform.runLater(() -> {
                        try{
                            //look for various types of dialog boxes
                            Stage dialogStage = getTopModalStage();
                            if(dialogStage != null){
                                System.out.println("Boite de dialogue détéctée, fermeture automatique ...");
                                //Simulate a click on the OK or Close button
                                Button okButton = findButtonInDialog(dialogStage, "OK");
                                if (okButton != null){
                                    Event.fireEvent(okButton, new ActionEvent(okButton, null));
                                } else{
                                    //Close the window if no button is found
                                    dialogStage.close();
                                }
                                System.out.println("Boite de dialogue fermée");
                            }
                        } catch (Exception e){
                            System.out.println("Erreur lors de la gestion automatique de la boite de dialogue : " + e.getMessage());
                        }
                    });
                    //Wait before the next check
                    Thread.sleep(500);
                }
            } catch (InterruptedException e){
                //Thread interrupted, this is normal at the end of the test
                System.out.println("Gestionnaire de dialogue interrompu");
            }
        });
        //Start the thread as a daemon so it terminates with the test
        dialogWatcherThread.setDaemon(true);
        dialogWatcherThread.start();
    }

    /**
     * Retrieves the topmost modal stage in the application
     * @return the top modal stage or null if none found
     */
    private Stage getTopModalStage(){
        //Retrieve all windows
        for (Window window : Stage.getWindows()){
            if (window instanceof Stage){ 
                Stage stage = (Stage) window;
                //Check if it is a modal and visible window
                if (stage.isShowing() && stage.getModality() != Modality.NONE){
                return stage;
                }
            }
        }
        return null;
    }

    /**
     * Finds a button in a dialog window by its text
     * @param dialogStage the dialog stage to search in
     * @param buttonText the text of the button to find
     * @return the found button or null if not found
     */
    private Button findButtonInDialog(Stage dialogStage, String buttonText){
        //Traverse all nodes in the scene to find a button
        Scene scene = dialogStage.getScene();
        if (scene != null && scene.getRoot() != null){
            return findButtonInNode(scene.getRoot(), buttonText);
        }
        return null;
    }

    /**
     * Recursively searches for a button in a node and its children
     * @param node the node to search in
     * @param buttonText the text of the button to find
     * @return the found button or null if not found
     */
    private Button findButtonInNode(Node node, String buttonText){
        if (node instanceof Button){
            Button button = (Button) node;
            if (button.getText().equalsIgnoreCase(buttonText)){
                return button;
            }
        }
        return null;
    }

    /**
     * Loads a valid student into the form using the provided robot
     * @param robot the FxRobot instance used for simulating user input
     */
    private void loadValidStudent(FxRobot robot){
        TextField idField = robot.lookup("#studentIdField").queryAs(TextField.class);
        robot.interact(() -> {
            idField.clear();
            idField.setText("1");
        });
        WaitForAsyncUtils.waitForFxEvents();
        Button okButton = robot.lookup("#okButton").queryButton();
        robot.clickOn(okButton);
        //Wait for the UI to update
        WaitForAsyncUtils.waitForFxEvents();
        System.out.println("Nom de l'étudiant chargé : " + studentNameLabel.getText());
    }
        
    @Test
    /**
     * Tests the functionnality of checkboxes showing buttons for modifying or deleting grades
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testCheckboxShowsButtons(FxRobot robot){
        System.out.println("Début du test testCheckboxShowsButtons");
        try{
            //Load a valid student
            System.out.println("Chargement de l'étudiant ...");
            loadValidStudent(robot);
            System.out.println("Etudiant chargé avec succès");

            WaitForAsyncUtils.waitForFxEvents();

            TableView<SubjectResult> table = robot.lookup("#gradesTable").queryTableView();
            //Ensure the table contains data
            WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, () -> !table.getItems().isEmpty());
            System.out.println("Le tableau contient " + table.getItems().size() + " éléments");
            int subjectIndex = 0;

            //Verify the table has at least one item
            if(table.getItems().isEmpty()){
                fail("Aucune matière n'a été trouvée dans le tableau ...");
            }
            //Get the name of the first subject
            String subjectName = table.getItems().get(subjectIndex).getSubject();
            System.out.println("La première matière trouvée est : " + subjectName + " à l'index " + subjectIndex);
            //Select the row
            final int finalSubjectIndex = subjectIndex;
            robot.interact(() -> {
                table.getSelectionModel().select(finalSubjectIndex);
                table.scrollTo(finalSubjectIndex);
            });
            WaitForAsyncUtils.waitForFxEvents();

            //Get the grade cell (second column, index 1)
            TableCell<?, ?> gradeCell = robot.lookup(".table-cell").nth(finalSubjectIndex * table.getColumns().size() + 1).query();
            System.out.println("Cellule des notes trouvée");

            //Find the checkbox in the cell
            CheckBox checkBox = robot.from(gradeCell).lookup(".check-box").queryAs(CheckBox.class);
            System.out.println("Checkbox trouvée, état initial: " + (checkBox.isSelected() ? "sélectionnée" : "non sélectionnée"));

            robot.clickOn(checkBox);

            WaitForAsyncUtils.waitForFxEvents();
            System.out.println("Checkbox cliquée, nouvel état: " + (checkBox.isSelected() ? "sélectionnée" : "non sélectionnée"));
            try{
                Thread.sleep(500);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            WaitForAsyncUtils.waitForFxEvents();

            //Check that the + and - buttons appear
            System.out.println("Recherche des boutons + et -...");
            Button plusButton = null;
            Button minusButton = null;
            try{
                plusButton = robot.lookup(".button").match(node -> 
                    node instanceof Button && 
                    ((Button)node).getText() != null && 
                    ((Button)node).getText().equals("+") && 
                    node.isVisible()).queryButton();
                System.out.println("Bouton '+' trouvé et visible");
            } catch (Exception e){
                System.out.println("Bouton '+' non trouvé: " + e.getMessage());
            }
            try{
                minusButton = robot.lookup(".button").match(node -> 
                    node instanceof Button && 
                    ((Button)node).getText() != null && 
                    ((Button)node).getText().equals("-") && 
                    node.isVisible()).queryButton();
                System.out.println("Bouton '-' trouvé et visible");
            } catch (Exception e){
                System.out.println("Bouton '-' non trouvé: " + e.getMessage());
            }

            //Verify that at least one button is visible
            assertTrue(plusButton != null || minusButton != null, 
                  "Les boutons + et/ou - devraient être visibles après avoir cliqué sur la checkbox");

            //If the + button is found, click on it to verify a window appears
            if (plusButton != null){
            System.out.println("Clic sur le bouton '+'...");
                robot.clickOn(plusButton);

                WaitForAsyncUtils.waitForFxEvents();
                //Wait for a dialog to appear
                boolean dialogAppeared = false;

                try{
                    WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> 
                        robot.lookup(".dialog-pane").tryQuery().isPresent() || 
                        robot.lookup(".text-field").tryQuery().isPresent());
                    dialogAppeared = true;
                    System.out.println("Fenêtre de dialogue détectée");
                } catch (Exception e){
                     System.out.println("Aucune fenêtre de dialogue détectée: " + e.getMessage());
                }
                assertTrue(dialogAppeared, 
                      "Une fenêtre de dialogue devrait s'ouvrir après avoir cliqué sur le bouton '+'");
            }
            System.out.println("Test réussi: Les boutons apparaissent correctement après clic sur la checkbox");
            
        } catch (Exception e){
        System.out.println("Exception dans le test: " + e.getMessage());
            e.printStackTrace();
            fail("le test a échoué avec une exception : " + e.getMessage());
        }
    }
    
}

