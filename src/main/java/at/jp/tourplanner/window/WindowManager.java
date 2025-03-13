package at.jp.tourplanner.window;

import at.jp.tourplanner.FXMLDependencyInjector;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class WindowManager {
    private static WindowManager windowManager;
    private WindowManager() {

    }

    public static WindowManager getInstance() {
        if (windowManager == null) {
            windowManager = new WindowManager();
        }
        return windowManager;
    }

    public void openWindow(Windows window)  {
        String fxmlFile = "";
        switch(window)
        {
            case Windows.NEW_TOUR_WINDOW:
                fxmlFile = "newtour-view.fxml";
                break;
            default:
                throw new IllegalArgumentException("Unsupported window: " + window);
        }

        Parent view = null;
        try {
            view = FXMLDependencyInjector.load(fxmlFile, Locale.ENGLISH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (view != null) {
            Stage stage = new Stage();
            stage.setScene(new Scene(view));
            stage.show();
        }else {
            throw new RuntimeException("Failed to load FXML file: " + fxmlFile);
        }
    }
}
