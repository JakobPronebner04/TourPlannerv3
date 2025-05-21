package at.jp.tourplanner.exception;

import javafx.scene.control.Alert;

public class ExceptionHandler {

    public static String handle(Exception e, ErrorHandlingMode mode) {
        String title = e.getClass().getSimpleName();
        String message = e.getMessage();
        String fullMessage = title + ": " + message;

        switch (mode) {
            case ALERT:
                //showAlert(title, message);
                break;
            case LOG_ONLY:
                System.err.println("[ERROR] " + fullMessage);
                break;
        }
        return fullMessage;
    }

    /*private static void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
}

