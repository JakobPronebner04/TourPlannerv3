package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final StringProperty errorMessageProperty;
    private final StringProperty tourNameProperty;
    private final StringProperty tourDescriptionProperty;
    private final StringProperty tourStartProperty;
    private final StringProperty tourDestinationProperty;

    public EditTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        Tour selectedTour = tourService.getSelectedTour();
        errorMessageProperty = new SimpleStringProperty("");
        this.tourNameProperty = new SimpleStringProperty(selectedTour.getTourName());
        this.tourDescriptionProperty = new SimpleStringProperty(selectedTour.getTourDescription());
        this.tourStartProperty = new SimpleStringProperty(selectedTour.getTourStart());
        this.tourDestinationProperty = new SimpleStringProperty(selectedTour.getTourDescription());
    }

    public StringProperty tourNameProperty() { return tourNameProperty; }
    public StringProperty tourDescriptionProperty() { return tourDescriptionProperty; }
    public StringProperty tourStartProperty() { return tourStartProperty; }
    public StringProperty tourDestinationProperty() { return tourDestinationProperty; }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void editSelectedTour()
    {
        try
        {
            Tour updatedTour = new Tour();
            updatedTour.setTourName(tourNameProperty().get());
            updatedTour.setTourDescription(tourDescriptionProperty().get());
            updatedTour.setTourStart(tourStartProperty().get());
            updatedTour.setTourDescription(tourDescriptionProperty().get());

            tourService.Change(updatedTour);
        } catch (IllegalAccessException e) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}