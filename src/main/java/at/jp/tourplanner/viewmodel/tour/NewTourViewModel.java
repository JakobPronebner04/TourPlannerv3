package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final Tour tour;
    private final StringProperty errorMessageProperty;


    public NewTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        errorMessageProperty = new SimpleStringProperty("");
        tour = new Tour();
    }

    public StringProperty tourNameProperty() { return tour.tourNameProperty(); }
    public StringProperty tourDescriptionProperty() { return tour.tourDescriptionProperty(); }
    public StringProperty tourStartProperty() { return tour.tourStartProperty(); }
    public StringProperty tourDestinationProperty() { return tour.tourDestinationProperty(); }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void addTour()
    {
        try
        {
            tourService.add(tour);
            windowManager.closeWindow();
        }catch(IllegalAccessException ex){
            errorMessageProperty.set("Some inputs might be empty!");
        }

    }
}
