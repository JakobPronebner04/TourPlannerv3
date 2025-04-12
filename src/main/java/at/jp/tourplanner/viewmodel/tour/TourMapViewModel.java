package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourService;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import java.util.List;

public class TourMapViewModel {

    private final TourService tourService;
    private final EventManager eventManager;

    private final ListProperty<Geocode> routeCoordinates = new SimpleListProperty<>(FXCollections.observableArrayList());

    public TourMapViewModel(EventManager eventManager, TourService tourService) {
        this.eventManager = eventManager;
        this.tourService = tourService;
        this.eventManager.subscribe(Events.TOUR_SELECTED, this::onTourSelected);
    }

    private void onTourSelected(Boolean isTourSelected) {
        if (isTourSelected) {
            routeCoordinates.clear();
            return;
        }
        routeCoordinates.setAll(tourService.getRouteGeocodes());
    }

    public ListProperty<Geocode> routeCoordinatesProperty() {
        return routeCoordinates;
    }
}
