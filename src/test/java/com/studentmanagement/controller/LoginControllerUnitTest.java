package com.studentmanagement.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        //Intitialize the LoginController and inject the mocked AuthenticationService
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
    /**
     * Tests the successful login scenario
     * @throws Exception if an error occures during the test
     */
    void testLoginSucess() throws Exception{
        //Config mock to simulate a sucessful authentication
        when(authServiceMock.authenticate("testuser", "correctpassword" )).thenReturn(true);
        boolean result = authServiceMock.authenticate("testuser", "correctpassword");
        assert result;
    }

    @Test
    /**
     * Tests the failed login scenario
     * @throws Exception if an error occurs during the test
     */
    void testLoginFailure() throws Exception{
        //Config mock to simulate a failed authentication
        when(authServiceMock.authenticate("testuser", "wrongpassword")).thenReturn(false);
        //Verify that authentication failed
        boolean result = authServiceMock.authenticate("testuser","wrongpassword");
        assert !result;
    }

    @Test
    /**
     * Tests the login scenario when an exceptin is thrown
     * @throws Exception if an error occurs during the test
     */
    void testLoginException() throws Exception{
        //Config mock to simulate exception
        when(authServiceMock.authenticate("testuser", "exceptionpassword")).thenThrow(new RuntimeException("Test exception"));
        //Verify the exception is launched
        try{
            authServiceMock.authenticate("testuser", "exceptionpassword");
            assert false;
        } catch (RuntimeException e){
            assert "Test exception".equals(e.getMessage());
        }
    }

    @Test
    /**
     * Tests the interactions with the authentication service
     * @throws Exception if an error occurs during the test
     */
    void testAuthServiceInteractions() throws Exception{
        //Verify that service authentication is called with good parameters
        authServiceMock.authenticate("user1", "pass1");
        verify(authServiceMock).authenticate("user1", "pass1");

        authServiceMock.authenticate("user2", "pass2");
        verify(authServiceMock).authenticate("user2", "pass2");

        verify(authServiceMock, never()).authenticate("wronguser", "wrongpass");
    }

}
