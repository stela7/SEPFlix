package server.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class HelperServer {

    /**
     * Validates if textfied contains only numbers.
     */

    protected static boolean validateNumberField(TextField textField) {
        return textField.getText().matches("[0-9]+");

    }

    /**
     * Displays alert.
     *
     * @param title   title for alert
     * @param message alert's message
     */

    protected static void alertdisplay(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays success alert.
     *
     * @param title   tittle for mesage
     * @param message to show
     */

    protected static void successdisplay(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
