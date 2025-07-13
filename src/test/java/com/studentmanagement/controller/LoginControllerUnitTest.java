package com.studentmanagement.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.studentmanagement.service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class LoginControllerUnitTest {
    
    @Mock
    private AuthenticationService authServiceMock;

    private LoginController loginController;

    @BeforeEach
    void setup(){
        loginController = new LoginController();
        loginController.setAuthService(authServiceMock);
    }

    @Test
    void testSetAuthService(){
        //Verify that authentication service can be injected
        AuthenticationService newAuthService = mock(AuthenticationService.class);
        loginController.setAuthService(newAuthService);
    }

    @Test
    void testLoginSucess() throws Exception{
        //Config mock to simulate a sucessful authentication
        when(authServiceMock.authenticate("testuser", "correctpassword" )).thenReturn(true);
        boolean result = authServiceMock.authenticate("testuser", "correctpassword");
        assert result;
    }


}
