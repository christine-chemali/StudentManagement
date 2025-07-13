package com.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

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
    public void tearDown() throws Exception{
        FxToolkit.cleanupStages();
    }

    @Test
    public void testEmptyFields(FxRobot robot){
        //Click on the register button without filling in the fields
        robot.clickOn("#buttonRegister");
        //Verify that register was not called
        verify(authServiceMock, never()).register(any(User.class));
        //Close the error dialog
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
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
     
}
