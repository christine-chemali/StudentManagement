package com.studentmanagement.controller;

import static org.mockito.Mockito.reset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.studentmanagement.service.AuthenticationService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class RegisterControllerUITest {
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
        System.setProperty("java.awt.headless", "false");
    }

    @Start
    public void start(Stage stage) throws Exception{
        //Load FXML
        FXMLLoader loader = new
        FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        Parent root = loader.load();
        registerController.setAuthService(authServiceMock);
        //Inject the authentication service mock
        registerController.setAuthService(authServiceMock);
        //Config and display the scene
        stage.setScene(new Scene(root));
        stage.setTitle("Register Test");
        stage.show();
        stage.toFront();
    }

    @BeforeEach
    public void setUp(FxRobot robot){
        //Retrieve ref to UI elements
        usernameField = robot.lookup("#textFieldUsername").queryAs(TextField.class);
        passwordField = robot.lookup("#testFieldPassword").queryAs(TextField.class);
        confirmPasswordField = robot.lookup("#textFieldConfirmPassword").queryAs(TextField.class);
        //Inject mock before each test
        registerController.setAuthService(authServiceMock);
        //Reinit mock before each test
        reset(authServiceMock);
    }

    @AfterEach
    public void tearDown() throws Exception{
        FxToolkit.cleanupStages();
    }

    
}
