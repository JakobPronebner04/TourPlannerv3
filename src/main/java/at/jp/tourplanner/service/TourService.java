package at.jp.tourplanner.service;

import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.da.StateDataAccess;
import at.jp.tourplanner.repository.TourRepositoryORM;
import javafx.scene.image.Image;

import java.lang.reflect.Field;
import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Optional;

public class TourService {

    private final EventManager eventManager;
    private final StateDataAccess stateDataAccess;
    private final TourRepositoryORM tourRepository;

    public TourService(EventManager eventManager, StateDataAccess stateDataAccess, TourRepositoryORM tourRepository) {
        this.eventManager = eventManager;
        this.stateDataAccess = stateDataAccess;
        this.tourRepository = tourRepository;
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
        stateDataAccess.updateSelectedTour(t);
    }
    public Tour getSelectedTour()
    {
        return stateDataAccess.getSelectedTour();
    }

    public void edit(Tour editedTour) throws IllegalAccessException, AlreadyBoundException
    {
        hasNullProperties(editedTour);

        if(!editedTour.getTourName().equals(stateDataAccess.getSelectedTour().getTourName())
                && tourRepository.findByName(editedTour.getTourName()).isPresent()) {
            throw new AlreadyBoundException("Tour name already exists");
        }

        TourEntity te = tourRepository.findByName(this.stateDataAccess.getSelectedTour().getTourName()).get();

        te.setName(editedTour.getTourName());
        te.setDescription(editedTour.getTourDescription());
        te.setStart(editedTour.getTourStart());
        te.setDestination(editedTour.getTourDestination());
        te.setTransportType(editedTour.getTourTransportType());

        tourRepository.save(te);
        eventManager.publish(Events.TOURS_CHANGED, "UPDATED_TOUR");
    }

    public void remove() {
        Optional<TourEntity> te = this.tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if(te.isEmpty()) {
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

            if (value instanceof String stringValue) {
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
