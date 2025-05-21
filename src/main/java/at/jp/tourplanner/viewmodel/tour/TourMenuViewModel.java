package at.jp.tourplanner.viewmodel.tour;


import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.exception.ErrorHandlingMode;
import at.jp.tourplanner.exception.ExceptionHandler;
import at.jp.tourplanner.service.ExceptionService;
import at.jp.tourplanner.service.ExportService;
import at.jp.tourplanner.service.ImportService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TourMenuViewModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ExceptionService exceptionService;
    private final TourService tourService;
    private final ExportService exportService;
    private final ImportService importService;
    private final WindowManager windowManager;
    private final EventManager eventManager;
    private final BooleanProperty editDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty detailsDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty exportDisabled = new SimpleBooleanProperty(true);

    public TourMenuViewModel(EventManager eventManager,ExceptionService exceptionService, TourService tourService, ImportService importService, ExportService exportService, WindowManager windowManager) {
        this.exceptionService = exceptionService;
        this.tourService = tourService;
        this.exportService = exportService;
        this.importService = importService;
        this.windowManager = windowManager;
        this.eventManager = eventManager;
        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onTourSelectedChanged
        );
    }

    public void onTourSelectedChanged(Boolean state) {
        editDisabled.set(state);
        removeDisabled.set(state);
        detailsDisabled.set(state);
        exportDisabled.set(state);
    }

    public void openNewTourWindow(){
            windowManager.openWindow(Windows.NEW_TOUR_WINDOW);
    }
    public void openEditTourWindow(){
        windowManager.openWindow(Windows.EDIT_TOUR_WINDOW);
    }
    public void openDetailsTourWindow(){
        windowManager.openWindow(Windows.DETAILS_TOUR_WINDOW);
    }
    public BooleanProperty editDisabledProperty() {
        return editDisabled;
    }
    public BooleanProperty removeDisabledProperty() {
        return removeDisabled;
    }
    public BooleanProperty exportDisabledProperty() {
        return exportDisabled;
    }
    public BooleanProperty detailsDisabledProperty() { return detailsDisabled;}
    public void deleteTour()
    {
        tourService.remove();
    }
    public void fileChosen(String filename)
    {
       try
       {
           importService.importSingleTour(filename);
           LOGGER.info("Imported new tour from: " + filename);
       }catch(Exception e) {
           LOGGER.error(e);
           exceptionService.updateCurrentExceptionMessage(e);
       }

    }
    public void exportTourAsJson(){
        try{
            exportService.exportSingleTourAsJSON();
            LOGGER.info("Exported Tour as JSON");
        }catch(Exception e) {
            LOGGER.error(e);
            exceptionService.updateCurrentExceptionMessage(e);
        }
    }

    public void exportTourAsPDF()
    {
        this.eventManager.publish(Events.TOUR_EXPORT,"TOUR_EXPORT");
    }
}
