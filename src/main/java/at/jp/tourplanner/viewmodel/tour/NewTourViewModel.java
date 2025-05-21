package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.exception.ErrorHandlingMode;
import at.jp.tourplanner.exception.ExceptionHandler;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.service.ExceptionService;
import at.jp.tourplanner.service.OpenRouteServiceApi;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class NewTourViewModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private final TourService tourService;
    private final ExceptionService exceptionService;
    private final WindowManager windowManager;
    private final StringProperty errorMessageProperty;

    private final StringProperty tourNameProperty;
    private final StringProperty tourDescriptionProperty;
    private final StringProperty tourStartProperty;
    private final StringProperty tourDestinationProperty;
    private final ObjectProperty<String> selectedTransportType;

    private final Tour newTour;

    public NewTourViewModel(TourService tourService,ExceptionService exceptionService, WindowManager windowManager) {
        this.tourService = tourService;
        this.exceptionService = exceptionService;
        this.windowManager = windowManager;
        newTour = new Tour();
        errorMessageProperty = new SimpleStringProperty("");
        tourNameProperty = new SimpleStringProperty("");
        tourDescriptionProperty = new SimpleStringProperty("");
        tourStartProperty = new SimpleStringProperty("");
        tourDestinationProperty = new SimpleStringProperty("");
        selectedTransportType = new SimpleObjectProperty<>("Car");
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
            newTour.setTourName(tourNameProperty.getValue());
            newTour.setTourDescription(tourDescriptionProperty.getValue());
            newTour.setTourStart(tourStartProperty.getValue());
            newTour.setTourDestination(tourDestinationProperty.getValue());
            newTour.setTourTransportType(selectedTransportType.getValue());
            tourService.add(newTour);
            LOGGER.info("New tour created");
            windowManager.closeWindow(Windows.NEW_TOUR_WINDOW);
        } catch (Exception e) {
            LOGGER.error(e);
            exceptionService.updateCurrentExceptionMessage(e);
        }
    }

    public ObjectProperty<String> selectedTransportTypeProperty() {
        return selectedTransportType;
    }
}
