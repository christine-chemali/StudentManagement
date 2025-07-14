package com.studentmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.studentmanagement.model.User;
import com.studentmanagement.service.AuthenticationService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.utils.SceneUtils;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegisterControllerUnitTest {
    
    private RegisterController registerController;
    @Mock
    private AuthenticationService authServiceMock;
    @Mock
    private TextField textFieldUsernameMock;
    @Mock
    private TextField textFieldPasswordMock;
    @Mock
    private TextField textFieldConfirmPasswordMock;
    @Mock
    private Button buttonBackToLoginMock;

    @BeforeEach
    public void setUp() throws Exception{
        //Create controller with mocked AuthenticationService
        registerController = new RegisterController();

        injectField(registerController, "authService", authServiceMock);

        //Inject UI component mocks
        injectField(registerController, "textFieldUsername", textFieldUsernameMock);
        injectField(registerController, "textFieldPassword", textFieldPasswordMock);
        injectField(registerController, "textFieldConfirmPassword", textFieldConfirmPasswordMock);
        injectField(registerController, "buttonBackToLogin", buttonBackToLoginMock);

        //Setup default behavior for mocks
        when(textFieldUsernameMock.getText()).thenReturn("");
        when(textFieldPasswordMock.getText()).thenReturn("");
        when(textFieldConfirmPasswordMock.getText()).thenReturn("");

    }

    @Test
    public void testHandleRegisterWithEmptyFields() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Setup mock behavior for empty fields
            when(textFieldUsernameMock.getText()).thenReturn("");
            when(textFieldPasswordMock.getText()).thenReturn("MotdepasseValide123!");
            when(textFieldConfirmPasswordMock.getText()).thenReturn("MotdepasseValide123!");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that an error alert was shown
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), anyString()));
        }
    }

    @Test
    public void testHandleRegisterWithPasswordMismatch() throws Exception{
        try(MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Setup mock behavior for password mismatch
            when(textFieldUsernameMock.getText()).thenReturn("UtilisateurValide");
            when(textFieldPasswordMock.getText()).thenReturn("Motdepasse123!");
            when(textFieldConfirmPasswordMock.getText()).thenReturn("MotdepasseDifferent123!");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that an error alert was shown
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), anyString()));
            //Verify that password fields were cleared
            verify(textFieldPasswordMock).clear();
            verify(textFieldConfirmPasswordMock).clear();
            //Verify that username field get focus
            verify(textFieldUsernameMock).requestFocus();
        }
    }

    @Test
    public void testPasswordValidationMissingUppercase() throws Exception{
        try(MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Setup mock behavior for password missing uppercase
            when(textFieldUsernameMock.getText()).thenReturn("utilisateurValide");
            when(textFieldPasswordMock.getText()).thenReturn("motdepassevalide123!");
            when(textFieldConfirmPasswordMock.getText()).thenReturn("motdepassevalide123!");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that error alert was shown with message containint uppercase requirement
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), argThat(msg -> msg.contains("majuscule"))));
        }
    }

    @Test
    public void testPasswordValidationMissingLowercase() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            // Setup mock behavior for password missing lowercase
            when(textFieldUsernameMock.getText()).thenReturn("utilisateurValide");
            when(textFieldPasswordMock.getText()).thenReturn("MOTDEPASSEVALIDE123!");
            when(textFieldConfirmPasswordMock.getText()).thenReturn("MOTDEPASSEVALIDE123!");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that error alert was shown with message containing lowercase requirement
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), argThat(msg -> msg.contains("minuscule"))));
        }
    }

    @Test
    public void testPasswordValidationMissingSpecialChar() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Setup mock behavior for password missing special character
            when(textFieldUsernameMock.getText()).thenReturn("utilisateurValide");
            when(textFieldPasswordMock.getText()).thenReturn("Motdepasse123");
            when(textFieldConfirmPasswordMock.getText()).thenReturn("Motdepasse123");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that error alert was shown with message containing special character requirement
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), argThat(msg -> msg.contains("caractère spécial"))));
        }
    }

    @Test
    public void testPasswordValidationTooShort() throws Exception{
        try (MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Setup mock behavior for password that's too short
            when(textFieldUsernameMock.getText()).thenReturn("utilisateurValide");
            when(textFieldPasswordMock.getText()).thenReturn("Mp1!");  //Only 4 characters
            when(textFieldConfirmPasswordMock.getText()).thenReturn("Mp1!");
            //Call handleRegister using reflection
            Method handleRegisterMethod = RegisterController.class.getDeclaredMethod("handleRegister");
            handleRegisterMethod.setAccessible(true);
            handleRegisterMethod.invoke(registerController);
            //Verify that register was not called
            verify(authServiceMock, never()).register(any(User.class));
            //Verify that error alert was shown with message containing length requirement
            alertUtilsMock.verify(() -> AlertUtils.showError(eq("Erreur"), argThat(msg -> msg.contains("8 caractères"))));
        }
    }
    
    @Test
    public void testHandleBackToLogin() throws Exception{
        try (MockedStatic<SceneUtils> sceneUtilsMock = mockStatic(SceneUtils.class);
            MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Mock the Stage
            Stage stageMock = mock(Stage.class);
            when(buttonBackToLoginMock.getScene()).thenReturn(mock(javafx.scene.Scene.class));
            when(buttonBackToLoginMock.getScene().getWindow()).thenReturn(stageMock);
            //Mock the LoginController
            LoginController loginControllerMock = mock(LoginController.class);
            //Configure SceneUtils to return the mocked LoginController
            sceneUtilsMock.when(() -> SceneUtils.changeScene(
                eq(stageMock), 
                eq("/fxml/login.fxml"), 
                eq("ÉduSys - Connection")
            )).thenReturn(loginControllerMock);
            //Call handleBackToLogin using reflection
            Method handleBackToLoginMethod = RegisterController.class.getDeclaredMethod("handleBackToLogin");
            handleBackToLoginMethod.setAccessible(true);
            handleBackToLoginMethod.invoke(registerController);
            //Verify that form was cleared
            verify(textFieldUsernameMock).clear();
            verify(textFieldPasswordMock).clear();
            verify(textFieldConfirmPasswordMock).clear();
            //Verify that SceneUtils.changeScene was called
            sceneUtilsMock.verify(() -> SceneUtils.changeScene(
                eq(stageMock), 
                eq("/fxml/login.fxml"), 
                eq("ÉduSys - Connection")
            ));
            // Verify that authService was injected into login controller
            verify(loginControllerMock).setAuthService(authServiceMock);
        }
    }

    @Test
    public void testHandleBackToLoginWithException() throws Exception{
        try (MockedStatic<SceneUtils> sceneUtilsMock = mockStatic(SceneUtils.class);
            MockedStatic<AlertUtils> alertUtilsMock = mockStatic(AlertUtils.class)){
            //Mock the Stage
            Stage stageMock = mock(Stage.class);
            when(buttonBackToLoginMock.getScene()).thenReturn(mock(javafx.scene.Scene.class));
            when(buttonBackToLoginMock.getScene().getWindow()).thenReturn(stageMock);
            //Configure SceneUtils to throw exception
            sceneUtilsMock.when(() -> SceneUtils.changeScene(
                any(Stage.class), 
                anyString(), 
                anyString()
            )).thenThrow(new RuntimeException("Erreur lors du chargement de la page login"));
            //Call handleBackToLogin using reflection
            Method handleBackToLoginMethod = RegisterController.class.getDeclaredMethod("handleBackToLogin");
            handleBackToLoginMethod.setAccessible(true);
            handleBackToLoginMethod.invoke(registerController);
            //Verify that error alert was shown
            alertUtilsMock.verify(() -> 
                AlertUtils.showError(eq("Erreur"), anyString()));
        }
    }



    //Helper method to inject mock objects into private fields
    private void injectField(Object target, String fieldName, Object value) throws Exception{
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
