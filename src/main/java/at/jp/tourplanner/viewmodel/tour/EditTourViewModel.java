package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.*;

import java.rmi.AlreadyBoundException;

public class EditTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final StringProperty errorMessageProperty;
    private final StringProperty tourNameProperty;
    private final StringProperty tourDescriptionProperty;
    private final StringProperty tourStartProperty;
    private final StringProperty tourDestinationProperty;
    private final ObjectProperty<String> tourTransportTypeProperty;
    private final Tour updatedTour;

    public EditTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        Tour selectedTour = tourService.getSelectedTour();
        errorMessageProperty = new SimpleStringProperty("");
        this.tourNameProperty = new SimpleStringProperty(selectedTour.getTourName());
        this.tourDescriptionProperty = new SimpleStringProperty(selectedTour.getTourDescription());
        this.tourStartProperty = new SimpleStringProperty(selectedTour.getTourStart());
        this.tourDestinationProperty = new SimpleStringProperty(selectedTour.getTourDestination());
        this.tourTransportTypeProperty = new SimpleObjectProperty<>(selectedTour.getTourTransportType());
        this.updatedTour = new Tour();
    }


    public StringProperty tourNameProperty() { return tourNameProperty; }
    public StringProperty tourDescriptionProperty() { return tourDescriptionProperty; }
    public StringProperty tourStartProperty() { return tourStartProperty; }
    public StringProperty tourDestinationProperty() { return tourDestinationProperty; }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public ObjectProperty<String> tourTransportTypeProperty() { return tourTransportTypeProperty; }


    public void editSelectedTour()
    {
        try
        {
            updatedTour.setTourName(tourNameProperty.getValue());
            updatedTour.setTourDescription(tourDescriptionProperty.getValue());
            updatedTour.setTourStart(tourStartProperty.getValue());
            updatedTour.setTourDestination(tourDestinationProperty.getValue());
            updatedTour.setTourTransportType(tourTransportTypeProperty.getValue());

            tourService.edit(updatedTour);
            windowManager.closeWindow();
        } catch (IllegalAccessException | AlreadyBoundException e) {
            if(e.getClass() == AlreadyBoundException.class){
                errorMessageProperty.set("Tour already exists");
                return;
            }
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}