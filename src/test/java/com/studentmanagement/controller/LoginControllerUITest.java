package com.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.studentmanagement.service.AuthenticationService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoginControllerUITest {
    private LoginController loginController;

    @Mock
    private AuthenticationService authServiceMock;

    private TextField usernameField;
    private TextField passwordField;

    //Config to show the Gui during tests
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
        FXMLLoader loader = new
        FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        loginController = loader.getController();

        //Inject the authentication service mock
        loginController.setAuthService(authServiceMock);

        //Config and display the scene
        stage.setScene(new Scene(root));
        stage.setTitle("Login Test");
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp(FxRobot robot){
        //Set up the behaviour of the mock
        when(authServiceMock.authenticate(anyString(), anyString())).thenReturn(false);
        when(authServiceMock.authenticate("validUser", "validPass")).thenReturn(true);
        //Retrieve references to UI elements
        usernameField = robot.lookup("#textFieldUsername").queryAs(TextField.class);
        passwordField = robot.lookup("#textFieldPassword").queryAs(TextField.class);
    }

    @AfterEach
    public void tearDown() throws Exception{
        FxToolkit.cleanupStages();
    }

    @Test
    public void testSuccessfulLogin(FxRobot robot){
        //Simulate user input
        robot.clickOn(usernameField).write("validUser");
        robot.clickOn(passwordField).write("validPass");
        robot.clickOn("#buttonLogin");
        //Verify that authentication was callled with the correct parameters
        verify(authServiceMock).authenticate("validUser", "validPass");
    }

    @Test
    public void testFailedLogin(FxRobot robot) {
        //Simulate user input
        robot.clickOn(usernameField).write("invalidUser");
        robot.clickOn(passwordField).write("anyPassword");
        robot.clickOn("#buttonLogin");
        //Verify that authentication was called with the correct parameters
        verify(authServiceMock).authenticate("invalidUser", "anyPassword");
        //Close the information dialog (by pressing Enter)
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Verify that the password field is empty but not the username field
        assertThat(usernameField.getText()).isEqualTo("invalidUser");
        assertThat(passwordField.getText()).isEmpty();
    }

    @Test
    public void testEmptyFields(FxRobot robot){
        //Click on the login button without filling in the fields
        robot.clickOn("#buttonLogin");
        //Verify that authentication was not called
        verify(authServiceMock, never()).authenticate(anyString(), anyString());
        //close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    public void testEmptyUsername(FxRobot robot){
        //Fill in only the password
        robot.clickOn(passwordField).write("password");
        robot.clickOn("#buttonLogin");
        //Verify that authentication was not called
        verify(authServiceMock, never()).authenticate(anyString(), anyString());
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

        @Test
    public void testEmptyPassword(FxRobot robot) {
        // Fill in only the username
        robot.clickOn(usernameField).write("username");
        robot.clickOn("#buttonLogin");
        
        // Verify that authentication was not called
        verify(authServiceMock, never()).authenticate(anyString(), anyString());
        
        // Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    public void testRegisterNavigation(FxRobot robot){
        //Click on the register button
        robot.clickOn("#buttonRegister");
    }

    @Test
    public void testSuccessfulLoginClearsFields(FxRobot robot){
        //Simulate successful login
        robot.clickOn(usernameField).write("validUser");
        robot.clickOn(passwordField).write("validPass");
        robot.clickOn("#buttonLogin");
        //Verify fields are cleared after successful login
        assertThat(usernameField.getText()).isEmpty();
        assertThat(passwordField.getText()).isEmpty();
    }

    @Test
    public void testAuthenticationException(FxRobot robot){
        //Setup mock to throw an exception
        when(authServiceMock.authenticate(("exceptionUser"), "exceptionPass")).thenThrow(new RuntimeException("test exception"));
        //Simulate login that will cause exception
        robot.clickOn(usernameField).write("exceptionUser");
        robot.clickOn(passwordField).write("exceptionPass");
        robot.clickOn("#buttonLogin");
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Verify password field is cleared but username remains
        assertThat(usernameField.getText()).isEqualTo("exceptionUser");
        assertThat(passwordField.getText()).isEmpty();
    }

    @Test
    public void testLongCredentials(FxRobot robot){
        //Test with the very long username and password
        String longString = "a".repeat(100);
        robot.clickOn(usernameField).write(longString);
        robot.clickOn(passwordField).write(longString);
        robot.clickOn("#buttonLogin");
        //Verify authentication was called with the long strings
        verify(authServiceMock).authenticate(longString, longString);
    }

    @Test
    public void testSpecialCharactersInCredentials(FxRobot robot){
        //Test with special characters
        String specialChars = "!@#$%^&*()_+{}[]|\"':;<>,.?/";
        robot.clickOn(usernameField).write(specialChars);
        robot.clickOn(passwordField).write(specialChars);
        robot.clickOn("#buttonLogin");
        //Verify authentication was called with special characters
        verify(authServiceMock).authenticate(specialChars, specialChars);
    }

}
