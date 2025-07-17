package com.studentmanagement.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

//Information alert message
public class AlertUtils {
    //Path to Css file
    private static final String CSS_FILE = "/css/style.css";

    /**
     * Applies custim styling to the specified alert dialog
     * @param alert the alert to which the custom style will be applied
     */
    private static void applyCustomStyle(Alert alert){
        try {
            DialogPane dialogPane = alert.getDialogPane();

            //load css sheet
            String css = AlertUtils.class.getResource(CSS_FILE).toExternalForm();
            dialogPane.getStylesheets().add(css);

            //Apply custom style class
            dialogPane.getStyleClass().add("custom-alert");

            //Remove default window decorations for cleaner look
            alert.initStyle(StageStyle.UNDECORATED);
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with the specified title and message
     * @param title the title of the alert dialog
     * @param message the message to be displayed in the alert dialog
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyCustomStyle(alert);
        alert.showAndWait();
    }

    /**
     * Displays an error alert dialog with the specified title and message
     * @param title the title of the error alert dialog
     * @param message the message to be displayed in the error alert dialog
     */   
    public static void showError(String  title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyCustomStyle(alert);
        alert.showAndWait();
    }

    /**
     * Displays a warning alert dialog with the specified title and message
     * @param title the title of the warning alert dialog
     * @param message the message to be displayed in the warning alert dialog
     */
    public static void showWarning(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyCustomStyle(alert);
        alert.showAndWait();
    }

    /**
     * Displays an information alert dialog with the specified title and message
     * @param title the title of the information alert dialog
     * @param message the message to be displayed in the information alert dialog
     */
    public static void showInformation(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyCustomStyle(alert);
        alert.showAndWait();
    }

}
