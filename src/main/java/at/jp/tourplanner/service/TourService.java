package at.jp.tourplanner.service;

import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.repository.StateRepository;
import at.jp.tourplanner.repository.TourRepository;
import at.jp.tourplanner.repository.TourRepositoryORM;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.lang.reflect.Field;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourService {

    private final EventManager eventManager;
    private final StateRepository stateRepository;
    private final TourRepositoryORM tourRepository;
    private final List<Tour> tours;

    public TourService(EventManager eventManager, StateRepository stateRepository, TourRepositoryORM tourRepository) {
        this.eventManager = eventManager;
        this.stateRepository = stateRepository;
        this.tourRepository = tourRepository;
        this.tours = new ArrayList<>();
    }
    public List<Tour> getTours() {
        return tourRepository.findAll().stream().map(this::mapEntityToModel).toList();
    }

    public void add(Tour t) throws IllegalAccessException, AlreadyBoundException {
        hasNullProperties(t);

        if(tourRepository.findByName(t.getTourName()).isPresent()) {
            throw new AlreadyBoundException("Tour name already exists");
        }

        TourEntity te = mapModelToEntity(t);
        tourRepository.save(te);
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
        Optional<TourEntity> te = this.tourRepository.findByName(stateRepository.getSelectedTour().getTourName());
        if(!te.isPresent()) {
            return;
        }
        this.tourRepository.delete(te.get());
        eventManager.publish(Events.TOURS_CHANGED, "REMOVE_TOUR");
    }

    public Image getPlaceHolderImage()
    {
        return new Image(getClass().getResource("/at/jp/tourplanner/images/mapplaceholder.png").toExternalForm());
    }

    private void hasNullProperties(Tour tour) throws IllegalAccessException {
        if (tour == null) throw new IllegalAccessException();

        for (Field field : Tour.class.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(tour);

            if (value instanceof String) {
                String stringValue = (String) value;
                if (stringValue == null || stringValue.isEmpty()) {
                    throw new IllegalAccessException();
                }
            }
            else if(value instanceof Float)
            {
                float floatValue = (float) value;
                if(floatValue == 0.0f)
                {
                    throw new IllegalAccessException();
                }
            }
        }
    }

    private TourEntity mapModelToEntity(Tour t)
    {
        TourEntity te = new TourEntity();
        te.setName(t.getTourName());
        te.setDescription(t.getTourDescription());
        te.setStart(t.getTourStart());
        te.setDestination(t.getTourDestination());
        te.setTransportType(t.getTourTransportType());
        return te;
    }

    private Tour mapEntityToModel(TourEntity entity) {
        Tour t = new Tour();
        t.setTourName(entity.getName());
        t.setTourDescription(entity.getDescription());
        t.setTourStart(entity.getStart());
        t.setTourDestination(entity.getDestination());
        t.setTourTransportType(entity.getTransportType());
        return t;
    }


}
