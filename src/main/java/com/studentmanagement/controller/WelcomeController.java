package com.studentmanagement.controller;

import com.studentmanagement.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable{

    @FXML
    private Label usernameLabel;
    
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources){
    }
    
    /**
     * Sets the current user and updates the UI to reflect the user's information
     * @param user the user object representing the current user
     */
    public void setCurrentUser(User user){
        this.currentUser = user;
        updateUI();
    }
    
    /**
     * Updates the UI components based on the current user's information
     * If the current user is not set, displays a default message
     */
    private void updateUI(){
        if (currentUser != null){
            usernameLabel.setText(currentUser.getUsername());
        } else{
            usernameLabel.setText("[Utilisateur non connecté]");
        }
    }
}