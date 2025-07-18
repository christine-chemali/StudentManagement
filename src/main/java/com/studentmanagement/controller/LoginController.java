package com.studentmanagement.controller;

import com.studentmanagement.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import com.studentmanagement.utils.SceneUtils;
import javafx.scene.control.TextField;
import com.studentmanagement.utils.AlertUtils;
import javafx.scene.control.Button;


public class LoginController {

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonRegister;

   private AuthenticationService authService;

   public LoginController(){
    //Empty constructor: dependencies will be injected
   }

   @FXML
   private void initialize(){
    //Init controller
    setupEventHandlers();
   }

   private void setupEventHandlers(){
    //Add events to buttons
    buttonLogin.setOnAction(event -> handleLogin());
    buttonRegister.setOnAction(event -> handleRegister());
   }
   
   // Handle login
   @FXML
   private void handleLogin(){
        String username = textFieldUsername.getText().trim();
        String password = textFieldPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()){
            AlertUtils.showError("Erreur", "Remplis tous les champs,\nil en manque sûrement un !");
            return;
        }

        try {
            boolean loginSucess = authService.authenticate(username, password);
            if (loginSucess){
                clearForm();
                showMainView();
            } else {
                AlertUtils.showInformation("Information", "Ton surnom et/ou ton mot de passe sont incorrects !");    
                textFieldPassword.clear();     
                textFieldPassword.requestFocus();
            }
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Oups !\nUne erreur est survenue lors de la connexion :\n"+ e.getMessage()); 
            textFieldPassword.clear();
        }
   }

   //handle register
   @FXML
   private void handleRegister(){
        showRegisterView();
   }

   //show register view
   private void showRegisterView() {
        try {

            //Clear form before navigating to register
            clearForm();

        //Use scene utils to show register view
        Stage stage = (Stage) buttonRegister.getScene().getWindow();
        RegisterController registerController = SceneUtils.changeScene( stage,  "/fxml/register.fxml",   "ÉduSys - Inscription");
        
        //Inject the service into the registration controller
        registerController.setAuthService(this.authService);
        
        } catch (Exception e){
            AlertUtils.showError("Erreur", "Oups !\nImpossible de charger la page d'inscription :\n " + e.getMessage());
        }
   }

   //show main view
   private void showMainView(){
    try {
        //Use Scene utils to show main view
        Stage stage = (Stage) buttonLogin.getScene().getWindow();
        SceneUtils.changeScene( stage, "/fxml/tab.fxml", "ÉduSys - Gestion des Étudiants");
    } catch (Exception e){
        AlertUtils.showError("Erreur", "Oups !\nImpossible de charger la vue principale :\n " + e.getMessage());
    }
   }

   //Clear form
   private void clearForm() {
    textFieldUsername.clear();
    textFieldPassword.clear();
    }   

    /**
     * Sets the AuthenticationService to be used by this controller
     * @param authService the AuthenticationService instance
     */
    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }
}