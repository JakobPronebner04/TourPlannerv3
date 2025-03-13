package at.jp.tourplanner.viewmodel;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditTourViewModel {
    private final TourService tourService;
    private final WindowManager windowManager;
    private final Tour editedTour;
    private final StringProperty errorMessageProperty;

    public EditTourViewModel(TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        Tour selectedTour = tourService.getSelectedTour();
        errorMessageProperty = new SimpleStringProperty("");
        this.editedTour = new Tour();
        this.editedTour.tourNameProperty().set(selectedTour.tourNameProperty().get());
        this.editedTour.tourDescriptionProperty().set(selectedTour.tourDescriptionProperty().get());
        this.editedTour.tourStartProperty().set(selectedTour.tourStartProperty().get());
        this.editedTour.tourDestinationProperty().set(selectedTour.tourDestinationProperty().get());
    }

    public StringProperty tourNameProperty() { return editedTour.tourNameProperty(); }
    public StringProperty tourDescriptionProperty() { return editedTour.tourDescriptionProperty(); }
    public StringProperty tourStartProperty() { return editedTour.tourStartProperty(); }
    public StringProperty tourDestinationProperty() { return editedTour.tourDestinationProperty(); }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void editSelectedTour()
    {
        try
        {
            tourService.addAndChange(editedTour);
            windowManager.closeWindow();
        } catch (IllegalAccessException e) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}