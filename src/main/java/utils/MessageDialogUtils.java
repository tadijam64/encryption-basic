package utils;

import javafx.scene.control.Alert;

public class MessageDialogUtils {

    public static void showMessage(Alert.AlertType alertType, String title, String headerText, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
