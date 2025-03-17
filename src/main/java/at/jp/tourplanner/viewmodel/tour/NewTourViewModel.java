package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final StringProperty errorMessageProperty;
    private final StringProperty tourNameProperty;
    private final StringProperty tourDescriptionProperty;
    private final StringProperty tourStartProperty;
    private final StringProperty tourDestinationProperty;


    public NewTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        errorMessageProperty = new SimpleStringProperty("");
        tourNameProperty = new SimpleStringProperty("");
        tourDescriptionProperty = new SimpleStringProperty("");
        tourStartProperty = new SimpleStringProperty("");
        tourDestinationProperty = new SimpleStringProperty("");
    }

    public StringProperty tourNameProperty() { return tourNameProperty; }
    public StringProperty tourDescriptionProperty() { return tourDescriptionProperty; }
    public StringProperty tourStartProperty() { return tourStartProperty; }
    public StringProperty tourDestinationProperty() { return tourDestinationProperty; }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void addTour()
    {
        try
        {
            Tour newTour = new Tour();
            newTour.setTourName(tourNameProperty.getValue());
            newTour.setTourDescription(tourDescriptionProperty.getValue());
            newTour.setTourStart(tourStartProperty.getValue());
            newTour.setTourDestination(tourDestinationProperty.getValue());
            tourService.add(newTour);
            windowManager.closeWindow();
        }catch(IllegalAccessException ex){
            errorMessageProperty.set("Some inputs might be empty!");
        }

    }
}
