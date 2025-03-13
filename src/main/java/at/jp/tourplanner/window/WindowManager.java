package at.jp.tourplanner.window;

import at.jp.tourplanner.FXMLDependencyInjector;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class WindowManager {
    private static WindowManager windowManager;
    private Stage currentStage;
    private final String windowFolder = "dialogs/";

    private WindowManager() {}

    public static WindowManager getInstance() {
        if (windowManager == null) {
            windowManager = new WindowManager();
        }
        return windowManager;
    }

    public void openWindow(Windows window) {
        String fxmlFile = "";
        switch(window) {
            case Windows.NEW_TOUR_WINDOW:
                fxmlFile = "newtour-view.fxml";
                break;
            case Windows.EDIT_TOUR_WINDOW:
                fxmlFile = "edittour-view.fxml";
                break;
            default:
                throw new IllegalArgumentException("Unsupported window: " + window);
        }

        try {
            Parent view = FXMLDependencyInjector.load(windowFolder+fxmlFile, Locale.ENGLISH);
            if (view != null) {
                Stage stage = new Stage();
                stage.setScene(new Scene(view));
                stage.show();
                this.currentStage = stage;
            } else {
                throw new RuntimeException("Failed to load FXML file: " + fxmlFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML: " + fxmlFile, e);
        }
    }

    public void closeWindow() {
        if (currentStage != null) {
            currentStage.close();
            currentStage = null;
        } else {
            System.out.println("Failed to close window.");
        }
    }
}
