package com.studentmanagement.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.studentmanagement.service.AuthenticationService;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;

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

    //Helper method to inject mock objects into private fields
    private void injectField(Object target, String fieldName, Object value) throws Exception{
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
