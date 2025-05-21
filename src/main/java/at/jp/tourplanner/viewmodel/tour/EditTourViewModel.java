package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.exception.ErrorHandlingMode;
import at.jp.tourplanner.exception.ExceptionHandler;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.service.ExceptionService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EditTourViewModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ExceptionService exceptionService;
    private final TourService tourService;
    private final WindowManager windowManager;
    private final StringProperty errorMessageProperty;
    private final StringProperty tourNameProperty;
    private final StringProperty tourDescriptionProperty;
    private final StringProperty tourStartProperty;
    private final StringProperty tourDestinationProperty;
    private final ObjectProperty<String> tourTransportTypeProperty;
    private final Tour updatedTour;

    public EditTourViewModel(TourService tourService,ExceptionService exceptionService, WindowManager windowManager) {
        this.exceptionService = exceptionService;
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
            LOGGER.info("Tour edited");
            windowManager.closeWindow(Windows.EDIT_TOUR_WINDOW);
        } catch (Exception e) {
            LOGGER.error(e);
            exceptionService.updateCurrentExceptionMessage(e);
        }
    }
}