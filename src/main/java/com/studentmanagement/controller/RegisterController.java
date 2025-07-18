package com.studentmanagement.controller;

import com.studentmanagement.service.AuthenticationService;
import com.studentmanagement.utils.AlertUtils;
import com.studentmanagement.model.User;
import com.studentmanagement.utils.SceneUtils;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

//Controller for the registration page
public class RegisterController {

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private TextField textFieldConfirmPassword;

    @FXML
    private Button buttonRegister;

    @FXML
    private Button buttonBackToLogin;

    private AuthenticationService authService;

    public RegisterController(){
        //Empty constructor - dependencies will be injected
    }

    @FXML
    private void initialize(){
        //set up event handlers
        setupEventHandlers();
    }

    //Configure event handlers for the buttons
    private void setupEventHandlers(){
        buttonRegister.setOnAction(event -> handleRegister());
        buttonBackToLogin.setOnAction(event -> handleBackToLogin());
    }

    //Handles the registration of a new user
    @FXML
    private void handleRegister(){
        String username = textFieldUsername.getText().trim();
        String password = textFieldPassword.getText().trim();
        String confirmPassword = textFieldConfirmPassword.getText().trim();
        
        //Required fields validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            AlertUtils.showError("Erreur", "Remplis tous les champs,\nil en manque sûrement un !");
            return;
        }

        //Check that password match
        if (!password.equals(confirmPassword)){
            AlertUtils.showError("Erreur", "Regarde bien tes mots de passe !\nIls ne sont pas identiques.");
            textFieldPassword.clear();
            textFieldConfirmPassword.clear();
            textFieldUsername.requestFocus();
            return;
        }

        //Password validation
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Ton mot de passe doit respecter ces critères :");
        if(password.length()<8) {
            errorMessage.append("\n- Au moins 8 caractères");
            isValid = false;
        }
        if (!password.matches(".*[A-Z].*")){
            errorMessage.append("\n- Au moins une lettre majuscule");
            isValid = false;
        }
        if (!password.matches(".*[a-z]")){
            errorMessage.append("\n- Au moins une lettre minuscule");
            isValid = false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")){
            errorMessage.append("\n- Au moins un caractère spécial (!@#$%^&* etc.)");
            isValid = false;
        }
        if (!isValid){
            AlertUtils.showError("Erreur", "Oups ! Mot de passe trop simple\n"  + errorMessage.toString());
            return;
        }
        try{
            //Create a new user
            User newUser = new User(username, password);

            //Use the register method of the service
            authService.register(newUser);

            //Registration success
            AlertUtils.showInformation("Succes", "Super !\nTu es bien enregisté,\nmaintenant tu peux te connecter");
            clearForm();
            showLoginView();
        }  catch (RuntimeException e) {
            AlertUtils.showInformation("Information", "Ce nom d'utilisateur existe peut-être déja.\nVérifie tes informations.");
            textFieldUsername.requestFocus();
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Aïe !\nUn petit problème technique est survenu.\nNe t'inquiète pas, nos équipes sont sur le coup !");
            clearForm();
        }
    }

    //Handles the return to the login page
    @FXML
    private void handleBackToLogin(){
        showLoginView();
    }

    //Displays the login view
    private void showLoginView(){
        try {
            clearForm();
            //Use utility method to change scene
            Stage stage = (Stage) buttonBackToLogin.getScene().getWindow();
            LoginController loginController = SceneUtils.changeScene(stage, "/fxml/login.fxml", "ÉduSys - Connection");
            //Inject the service into the login controller
            loginController.setAuthService(this.authService);
        } catch (Exception e){
            AlertUtils.showError("Erreur", "La page de connection n'est pas accessible.");
        }
    } 

    //Clears the form fields
    private void clearForm(){
        textFieldUsername.clear();
        textFieldPassword.clear();
        textFieldConfirmPassword.clear();
    }

    /**
     * Sets the AuthenticationService to be used by this controller
     * @param authService the AuthenticationService instance
     */
    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }
}