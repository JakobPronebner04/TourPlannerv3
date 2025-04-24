package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class DetailedTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final EventManager eventManager;
    private final StringProperty tourDescription;

    public DetailedTourViewModel(TourService tourService, WindowManager windowManager, EventManager eventManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        this.eventManager = eventManager;
        this.tourDescription = new SimpleStringProperty(tourService.getSelectedTour().getTourDescription());
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescription;
    }

    public void close() {
        windowManager.closeWindow();
    }
}
