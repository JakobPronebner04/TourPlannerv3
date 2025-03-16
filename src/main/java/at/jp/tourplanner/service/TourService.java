package at.jp.tourplanner.service;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.repository.StateRepository;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TourService {

    private final EventManager eventManager;
    private final StateRepository stateRepository;

    private final List<Tour> tours;

    public TourService(EventManager eventManager, StateRepository stateRepository) {
        this.eventManager = eventManager;
        this.stateRepository = stateRepository;
        this.tours = new ArrayList<>();
    }
    public List<Tour> getTours() {
        return tours;
    }

    public void add(Tour t) throws IllegalAccessException {
        hasNullProperties(t);
        this.tours.add(t);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }
    public void updateSelectedTour(Tour t) {
        stateRepository.updateSelectedTour(t);
    }
    public void updateSelectedTourPrev(Tour prevTour) {
        stateRepository.updateSelectedTourPrev(prevTour);
    }


    public Tour getSelectedTour()
    {
        return stateRepository.getSelectedTour();
    }

    public void Change(Tour editedTour) throws IllegalAccessException
    {
        hasNullProperties(editedTour);
        int index = tours.indexOf(stateRepository.getSelectedTour());
        tours.set(index, editedTour);
        stateRepository.updateSelectedTourPrev(stateRepository.getSelectedTour());
        stateRepository.updateSelectedTour(editedTour);
        eventManager.publish(Events.TOURS_EDITED, "UPDATE_TOURLOGS");
    }

    public void remove() {
        this.tours.remove(stateRepository.getSelectedTour());
        eventManager.publish(Events.TOURS_CHANGED, "REMOVE_TOUR");
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
