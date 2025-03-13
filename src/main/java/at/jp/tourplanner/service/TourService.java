package at.jp.tourplanner.service;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourService {

    private final EventManager eventManager;

    private final ObservableList<Tour> tours;

    public TourService(EventManager eventManager) {
        this.eventManager = eventManager;
        this.tours = FXCollections.observableArrayList();
    }

    public void add(Tour t) {
        this.tours.add(t);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }
}
