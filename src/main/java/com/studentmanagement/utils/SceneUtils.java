package com.studentmanagement.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Utility class for managing scenes
public class SceneUtils {

    /**
     * Changes the current scene of the given stage to a new FXML view
     * @param <T> the type of the controller associated with the FXML
     * @param stage the stage to change the scene for
     * @param fxmlPath the path to the FXML file
     * @param title the title to set for the stage
     * @return the controller associated with the loaded FXML
     * @throws Exception if loading the FXML fails
     */
    public static <T> T changeScene(Stage stage, String fxmlPath, String title) throws Exception {

        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        //Retrieve the current scene or create a new one
        Scene currentScene = stage.getScene();
        if (currentScene == null) {

            currentScene = new Scene(root);
            stage.setScene(currentScene);
        } else {
            currentScene.setRoot(root);
        }
        
        stage.setTitle(title);

        return loader.getController();
    }
}