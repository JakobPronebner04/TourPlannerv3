package at.jp.tourplanner.viewmodel.tour;


import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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

    public void onTourSelectedChanged(Boolean state) {
        editDisabled.set(state);
        removeDisabled.set(state);
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
