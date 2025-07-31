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
        System.out.println("TabController est initialisé");
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
        //Inject in StudentStatsController
        if (studentStats != null && studentStats.getContent() != null){
            StudentStatsController controller = findController(studentStats.getContent(), StudentStatsController.class);
            if(controller != null){
                controller.setStatisticsService(statisticsService);
            }
        }
        //Inject in StudentsStatsController
        if (studentsStats != null && studentsStats.getContent() != null){
            StudentsStatsController controller = findController(studentsStats.getContent(), StudentsStatsController.class);
            if(controller != null){
                controller.setStatisticsService(statisticsService);
            }
        }
    }
    
    /**
     * Config backup tab
     */
    private void setupBackupTab(){
        //Verify if backup tab exist
        if (backupTab != null && backupTab.getContent() != null){
            //BackupController create its own backup saving service, do not have to inject here
            System.out.println("Backup tab est initialisée");
        }
    }

    /**
     * Recursively searches for a controller of the specified type within 
     * the given JavaFX node and its children.
     *
     * @param <T> the type of the controller to find
     * @param node the JavaFX node to search within
     * @param controllerClass the class type of the controller to find
     * @return the found controller of type T, or null if not found
     */
    @SuppressWarnings("unchecked")
    private <T> T findController(javafx.scene.Node node, Class<T> controllerClass){
        Object controller = node.getProperties().get("controller");
        if (controller != null && controllerClass.isInstance(controller)){
            return (T) controller;
        }
        if (node instanceof javafx.scene.Parent){
            for (javafx.scene.Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()){
                T result = findController(child, controllerClass);
                if (result != null){
                    return result;
                }
            }
        }
        return null;
    }

}