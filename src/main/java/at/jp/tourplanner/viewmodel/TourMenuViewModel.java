package at.jp.tourplanner.viewmodel;


import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.sql.SQLOutput;

public class TourMenuViewModel {

    private final TourService tourService;
    private final WindowManager windowManager;
    private final EventManager eventManager;
    private final BooleanProperty editDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);
    //private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    public TourMenuViewModel(EventManager eventManager, TourService tourService, WindowManager windowManager) {
        this.tourService = tourService;
        this.windowManager = windowManager;
        this.eventManager = eventManager;
        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onTourSelectedChanged
        );
    }

    public void onTourSelectedChanged(String message) {
        boolean isDisabled = Boolean.parseBoolean(message);
        editDisabled.set(isDisabled);
        removeDisabled.set(isDisabled);
    }
    public void openNewTourWindow(Windows window){
            windowManager.openWindow(window);
    }
    public BooleanProperty editDisabledProperty() {
        return editDisabled;
    }
    public BooleanProperty removeDisabledProperty() {
        return removeDisabled;
    }

    public void deleteTour()
    {
        tourService.remove();
    }
}
