package com.studentmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
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

}
