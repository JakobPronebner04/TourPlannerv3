package at.jp.tourplanner.window;

import at.jp.tourplanner.FXMLDependencyInjector;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WindowManager {
    private final String dialogDir;
    private final Map<Windows, String> windowMap;
    private final Map<Windows, Stage> openStages;
    private final EventManager eventManager;

    public WindowManager(EventManager eventManager) {
        this.eventManager = eventManager;
        dialogDir = "dialogs/";
        windowMap = new HashMap<>();
        openStages = new HashMap<>();

        windowMap.put(Windows.NEW_TOUR_WINDOW,        "newtour-view");
        windowMap.put(Windows.EDIT_TOUR_WINDOW,       "edittour-view");
        windowMap.put(Windows.NEW_TOURLOG_WINDOW,     "newtourlog-view");
        windowMap.put(Windows.EDIT_TOURLOG_WINDOW,    "edittourlog-view");
        windowMap.put(Windows.DETAILS_TOURLOG_WINDOW, "detailedtourlog-view");
        windowMap.put(Windows.DETAILS_TOUR_WINDOW,    "detailedtour-view");
        windowMap.put(Windows.EXCEPTION_WINDOW,       "exception-view");
        eventManager.subscribe(Events.EXCEPTION_THROWN, e -> openWindow(Windows.EXCEPTION_WINDOW));
    }

    public void openWindow(Windows window) {
        if (!windowMap.containsKey(window)) {
            throw new IllegalArgumentException("Window does not exist: " + window);
        }

        if (openStages.containsKey(window)) {
            Stage existingStage = openStages.get(window);
            if (existingStage != null) {
                existingStage.toFront();
                return;
            }
        }

        String fxmlFile = windowMap.get(window);

        try {
            Parent view = FXMLDependencyInjector.load(dialogDir + fxmlFile + ".fxml", Locale.ENGLISH);
            if (view != null) {
                Stage stage = new Stage();
                stage.setScene(new Scene(view));
                stage.setOnCloseRequest(event -> openStages.remove(window));
                stage.show();
                openStages.put(window, stage);
            } else {
                throw new RuntimeException("Failed to load FXML file: " + fxmlFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML: " + fxmlFile, e);
        }
    }

    public void closeWindow(Windows window) {
        Stage stage = openStages.get(window);
        if (stage != null) {
            stage.close();
            openStages.remove(window);
        } else {
            throw new RuntimeException("No open window found for: " + window);
        }
    }
}
