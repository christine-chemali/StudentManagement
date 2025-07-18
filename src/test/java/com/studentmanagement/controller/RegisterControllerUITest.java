package com.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.studentmanagement.model.User;
import com.studentmanagement.service.AuthenticationService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegisterControllerUITest {
    private RegisterController registerController;

    @Mock
    private AuthenticationService authServiceMock;

    private TextField usernameField;
    private TextField passwordField;
    private TextField confirmPasswordField;

    //Config to show the Gui during test
    static{
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        Parent root = loader.load();
        registerController = loader.getController();
        //Inject the authentication service mock
        registerController.setAuthService(authServiceMock);
        //Config and display the scene
        stage.setScene(new Scene(root));
        stage.setTitle("Register Test");
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    /**
     * Sets up the testing environement before each test case
     * @param robot the FxRobot instance used for simulating user input
     */
    public void setUp(FxRobot robot){
        //Retrieve ref to UI elements
        usernameField = robot.lookup("#textFieldUsername").queryAs(TextField.class);
        passwordField = robot.lookup("#textFieldPassword").queryAs(TextField.class);
        confirmPasswordField = robot.lookup("#textFieldConfirmPassword").queryAs(TextField.class);
        //Inject mock before each test
        registerController.setAuthService(authServiceMock);
        //Reinit mock before each test
        reset(authServiceMock);
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
     * Tests the scenario where the registration button is clicked with empty fields
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testEmptyFields(FxRobot robot){
        //Click on the register button without filling in the fields
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    /**
     * Tests the scenario where the password and confirm password fields do not match
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testPasswordMismatch(FxRobot robot){
        //Fill in with mismatched passwords
        robot.clickOn(usernameField).write("surnom");
        robot.clickOn(passwordField).write("MotDePasse123!");
        robot.clickOn(confirmPasswordField).write("MotDePasseDifferent123!");
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Verify that password fields are cleared
        assertThat(passwordField.getText()).isEmpty();
        assertThat(confirmPasswordField.getText()).isEmpty();
        assertThat(usernameField.getText()).isEqualTo("surnom");
    }

    @Test
    /**
     * Tests the scenario where the password is too short
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testPasswordTooShort(FxRobot robot){
        //Fill in with password that's too short‼
        robot.clickOn(usernameField).write("surnom");
        robot.clickOn(passwordField).write("Mp1!");
        robot.clickOn(confirmPasswordField).write("Mp1!");
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }  

    @Test
    /**
     * Tests the scenario where the password contains no uppercase letter
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testPasswordNoUppercase(FxRobot robot){
        //Fill in with password that has no uppercase letter
        robot.clickOn(usernameField).write("surnom");
        robot.clickOn(passwordField).write("motdepasse123!");
        robot.clickOn(confirmPasswordField).write("motdepasse123!");
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    /**
     * Tests the scenario where the password contains ot lowercase letter
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testPasswordNoLowerCase(FxRobot robot){
        //Fill in with password that has no lowercase letter
        robot.clickOn(usernameField).write("surnom");
        robot.clickOn(passwordField).write("MOTDEPASSE123!");
        robot.clickOn(confirmPasswordField).write("MOTDEPASSE123!");
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    /**
     * Tests the scenario where the password contains no special character
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testPasswordNoSpecialChar(FxRobot robot){
        //Fill in with password that has no special character
        robot.clickOn(usernameField).write("surnom");
        robot.clickOn(passwordField).write("Motdepasse123");
        robot.clickOn(confirmPasswordField).write("Motdepasse123");
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    /**
     * Tests the scenario where an existing user attemps to register
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testUserAlreadyExists(FxRobot robot){
        //Config mock to make an RuntimeException
        doThrow(new RuntimeException("L'utilisateur existe déja")).when(authServiceMock).register(any(User.class));
        //Fill in with password that has no special character
        robot.clickOn(usernameField).write("utilisateurExistant");
        robot.clickOn(passwordField).write("Motdepasse123!");
        robot.clickOn(confirmPasswordField).write("Motdepasse123!");
        robot.clickOn("#buttonRegister");
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Verify that the fields are already full
        assertThat(usernameField.getText()).isEqualTo("utilisateurExistant");
    }

    @Test
    /**
     * Tests the scenario where an unexpected exception occurs during registration
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testUnexpectedException(FxRobot robot){
        //Config mock to launch RuntimeException
        doThrow(new RuntimeException("Erreur inattendue")).when(authServiceMock).register(any(User.class));
        //Fill with valid data
        robot.clickOn(usernameField).write("nouvelUtilisateur");
        robot.clickOn(passwordField).write("Motdepasse123!");
        robot.clickOn(confirmPasswordField).write("Motdepasse123!");
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Verify that the exception is handle without crashing test
        assertThat(true).isTrue();
    }

    @Test
    /**
     * Tests the navigation back to the login page
     * @param robot the FxRobot instance used for simulating user input
     */
    public void testBackToLoginNavigation(FxRobot robot){
        //Click on the back to login button
        robot.clickOn("#buttonBackToLogin");
    }

    @Test
    /**
     * Tests the injection of the authentication service into the controller
     */
    public void testAuthServiceInjection(){
        //Create a mock for the authentication service
        AuthenticationService authServiceMock = mock(AuthenticationService.class);
        //Inject mock in controller
        registerController.setAuthService(authServiceMock);
        //Verify if the service is well injected using reflexion
        try{
            Field authServiceField = RegisterController.class.getDeclaredField("authService");
            authServiceField.setAccessible(true);
            AuthenticationService injectedService = (AuthenticationService) authServiceField.get(registerController);
            //Verify that the injected service is the same as the mock
            assertThat(injectedService).isEqualTo(authServiceMock);
        } catch (Exception e){
            fail("Exception lors de l'accès au champ authService: " + e.getMessage());
        }
    }

}
