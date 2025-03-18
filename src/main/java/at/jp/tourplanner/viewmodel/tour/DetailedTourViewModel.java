package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetailedTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final StringProperty tourDescription;

    public DetailedTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        this.tourDescription = new SimpleStringProperty(tourService.getSelectedTour().getTourDescription());
    }

    public StringProperty tourDescriptionProperty() {
        return tourDescription;
    }

    public void close() {
        windowManager.closeWindow();
    }
}
