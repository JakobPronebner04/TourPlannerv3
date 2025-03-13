package at.jp.tourplanner.viewmodel;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.service.TourService;
import javafx.beans.property.StringProperty;

public class NewTourViewModel {
    private final TourService tourService;
    private final Tour tour;

    public NewTourViewModel(TourService tourService) {
        this.tourService = tourService;
        tour = new Tour();
    }

    public StringProperty tourNameProperty() { return tour.tourNameProperty(); }
    public StringProperty tourDescriptionProperty() { return tour.tourDescriptionProperty(); }
    public StringProperty tourStartProperty() { return tour.tourStartProperty(); }
    public StringProperty tourDestinationProperty() { return tour.tourDestinationProperty(); }

    public void addTour()
    {
        tourService.add(tour);
    }
}
