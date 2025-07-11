package com.studentmanagement.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.Start;

import com.studentmanagement.service.AuthenticationService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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
        System.setProperty("java.awt.headless", "false");
    }

    @Start
    public void start(Stage stage) throws Exception{
        //Load FXML
        FXMLLoader loader = new
        FXMLLoader(getClass().getResource("/fxml/login.xml"));
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
}
