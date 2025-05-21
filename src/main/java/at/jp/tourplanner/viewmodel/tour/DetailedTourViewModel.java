package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.service.ExceptionService;
import at.jp.tourplanner.service.MapRendererService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DetailedTourViewModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ExceptionService exceptionService;
    private final TourService tourService;
    private final MapRendererService mapRendererService;
    private WebEngine webEngine;
    private final WindowManager windowManager;
    private final EventManager eventManager;
    private final StringProperty tourDescription;

    public DetailedTourViewModel(TourService tourService,ExceptionService exceptionService,MapRendererService mapRendererService, WindowManager windowManager, EventManager eventManager) {
        this.exceptionService = exceptionService;
        this.tourService = tourService;
        this.mapRendererService = mapRendererService;
        this.windowManager = windowManager;
        this.eventManager = eventManager;
        this.tourDescription = new SimpleStringProperty(tourService.getSelectedTour().getTourDescription());
    }
    public void initMap(WebEngine engine) {
        this.webEngine = engine;

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                engine.executeScript(mapRendererService.getClearScript());
                engine.executeScript(mapRendererService.getDrawScript());
            }
        });

        if (engine.getLoadWorker().getState() == Worker.State.SUCCEEDED) {
            engine.reload();
        } else {
            engine.load(mapRendererService.getInitialState());
        }
    }


    public StringProperty tourDescriptionProperty() {
        return tourDescription;
    }

    public void close() {
        windowManager.closeWindow(Windows.DETAILS_TOUR_WINDOW);
    }
}
