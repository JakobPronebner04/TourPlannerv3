package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TourLogMenuViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final EventManager eventManager;

    private final BooleanProperty addDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty editDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty detailsDisabled = new SimpleBooleanProperty(true);
    public TourLogMenuViewModel(EventManager eventManager, TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        this.eventManager = eventManager;

        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onTourSelectedChanged
        );
        this.eventManager.subscribe(
                Events.TOURLOG_SELECTED, this::onTourLogSelectedChanged
        );
    }
    public void onTourSelectedChanged(boolean state) {
        addDisabled.set(state);
    }
    public void onTourLogSelectedChanged(boolean state) {
        editDisabled.set(state);
        removeDisabled.set(state);
        detailsDisabled.set(state);
    }

    public BooleanProperty addDisabledProperty() {
        return addDisabled;
    }
    public BooleanProperty editDisabledProperty() {
        return editDisabled;
    }
    public BooleanProperty removeDisabledProperty() {
        return removeDisabled;
    }
    public BooleanProperty detailsDisabledProperty() {
        return detailsDisabled;
    }

    public void openNewTourLogWindow(Windows window){
        windowManager.openWindow(window);
    }

    public void removeTourLog()
    {
        tourLogService.remove();
    }

}
