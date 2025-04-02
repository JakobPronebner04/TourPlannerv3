package at.jp.tourplanner.window;

import at.jp.tourplanner.FXMLDependencyInjector;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WindowManager {
    private static WindowManager windowManager;
    private Stage currentStage;
    private final String dialogDir;
    private final Map<Windows,String> windowMap;

    private WindowManager() {
        dialogDir = "dialogs/";
        windowMap = new HashMap<>();
        windowMap.put(Windows.NEW_TOUR_WINDOW       ,"newtour-view");
        windowMap.put(Windows.EDIT_TOUR_WINDOW      ,"edittour-view");
        windowMap.put(Windows.NEW_TOURLOG_WINDOW    ,"newtourlog-view");
        windowMap.put(Windows.EDIT_TOURLOG_WINDOW   ,"edittourlog-view");
        windowMap.put(Windows.DETAILS_TOURLOG_WINDOW,"detailedtourlog-view");
        windowMap.put(Windows.DETAILS_TOUR_WINDOW   ,"detailedtour-view");
    }

    public static WindowManager getInstance() {
        if (windowManager == null) {
            windowManager = new WindowManager();
        }
        return windowManager;
    }

    public void openWindow(Windows window) {
       if(!windowMap.containsKey(window))
       {
           throw new IllegalArgumentException("Window does not exist: " + window);
       }
       String fxmlFile = windowMap.get(window);

        try {
            Parent view = FXMLDependencyInjector.load(dialogDir + fxmlFile +".fxml", Locale.ENGLISH);
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
            throw new RuntimeException("Failed to close current window!");
        }
    }
}
