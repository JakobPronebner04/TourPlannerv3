package at.jp.tourplanner.viewmodel.tour;


import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.ExportService;
import at.jp.tourplanner.service.ImportService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public class TourMenuViewModel {

    private final TourService tourService;
    private final ExportService exportService;
    private final ImportService importService;
    private final WindowManager windowManager;
    private final EventManager eventManager;
    private final BooleanProperty editDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty detailsDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty exportDisabled = new SimpleBooleanProperty(true);

    public TourMenuViewModel(EventManager eventManager, TourService tourService, ImportService importService, ExportService exportService, WindowManager windowManager) {
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
       }catch(IOException e) {
          System.err.println(e.getMessage());
       }

    }
    public void exportTourAsJson(){
        try{
            exportService.exportSingleTourAsJSON();
        }catch(IOException | RuntimeException e) {
            if(e.getClass().equals(IOException.class))
            {
                System.err.println(e.getMessage());
                return;
            }
            System.err.println(e.getMessage());
        }
    }

    public void exportTourAsPDF()
    {
        this.eventManager.publish(Events.TOUR_EXPORT,"TOUR_EXPORT");
    }
}
