package at.jp.tourplanner.viewmodel;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.service.TourService;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourHistoryViewModel {
    private final EventManager eventManager;

    private final TourService tourService;

    private final ObservableList<Tour> tourHistory
            = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public TourHistoryViewModel(EventManager eventManager, TourService tourService) {
        this.eventManager = eventManager;
        this.tourService = tourService;

        this.selectedTour.addListener(this::onSelectedTourChanged);
        this.eventManager.subscribe(
                Events.TOURS_CHANGED, this::onToursChanged
        );
    }

    private void onSelectedTourChanged(Observable observable, Tour oldTour, Tour newTour) {
        boolean isDisabled = (newTour == null);
        this.eventManager.publish(Events.TOUR_SELECTED, String.valueOf(isDisabled));
    }

    private void onToursChanged(String message) {
        this.tourHistory.clear();
        this.tourHistory.addAll(tourService.getTours());
    }

    public ObservableList<Tour> getTourHistory() {
        return tourHistory;
    }
    public ObjectProperty<Tour> selectedTour() {
        return selectedTour;
    }
}

