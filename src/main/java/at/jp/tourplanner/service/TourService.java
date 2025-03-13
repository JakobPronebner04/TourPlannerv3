package at.jp.tourplanner.service;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TourService {

    private final EventManager eventManager;

    private final List<Tour> tours;
    private Tour selectedTour;

    public TourService(EventManager eventManager) {
        this.eventManager = eventManager;
        this.tours = new ArrayList<>();
        selectedTour = new Tour();
    }
    public List<Tour> getTours() {
        return tours;
    }

    public void add(Tour t) throws IllegalAccessException {
        hasNullProperties(t);
        this.tours.add(t);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }

    public void addAndChange(Tour editedTour) throws IllegalAccessException
    {
        hasNullProperties(editedTour);
        int index = tours.indexOf(selectedTour);
        tours.set(index, editedTour);
        eventManager.publish(Events.TOURS_CHANGED, "EDITED_TOUR");
    }

    public void setSelectedTour(Tour t) {
        this.selectedTour = t;
    }
    public Tour getSelectedTour() {
        return this.selectedTour;
    }

    public void remove() {
        this.tours.remove(selectedTour);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }

    private void hasNullProperties(Tour tour) throws IllegalAccessException {
        if (tour == null) throw new IllegalAccessException();

        for (Field field : Tour.class.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(tour);

            if (value instanceof StringProperty) {
                StringProperty property = (StringProperty) value;
                if (property == null || property.get() == null || property.get().isEmpty()) {
                    throw new IllegalAccessException();
                }
            }
        }
    }
}
