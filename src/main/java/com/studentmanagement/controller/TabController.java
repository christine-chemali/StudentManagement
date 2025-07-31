package com.studentmanagement.controller;

import com.studentmanagement.service.StatisticsService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TabController implements Initializable {

    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab dashboardTab;
    @FXML
    private Tab studentsTab;
    @FXML
    private Tab studentsStats;
    @FXML
    private Tab student;
    @FXML 
    private Tab studentStats;
    @FXML
    private Tab backupTab;

      private StatisticsService statisticsService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the controller
        System.out.println("TabController initialized");
        //Wait for tabs to load
        mainTabPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null){
                //Inject the sevice in the statistics tab controllers
                injectStatisticsService();
                //Init backup tab
                setupBackupTab();
            }
        });
    }

    /**
     * Inject statistics service in statistics tab controllers
     */
    private void injectStatisticsService(){

    }
    
    /**
     * Config backup tab
     */
    private void setupBackupTab(){
        
    }

    @FXML
    private void handleButtonAction() {
        System.out.println("Button clicked!");
    }
}