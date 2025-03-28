package at.jp.tourplanner.service;

import at.jp.tourplanner.da.StateDataAccess;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.repository.*;

import java.lang.reflect.Field;
import java.rmi.NotBoundException;
import java.util.*;

public class TourLogService {
    private final EventManager eventManager;
    private final StateDataAccess stateDataAccess;
    private final TourLogRepositoryORM tourLogRepository;
    private final TourRepositoryORM tourRepository;

    public TourLogService(EventManager eventManager, TourLogRepositoryORM tourLogRepository, TourRepositoryORM tourRepository, StateDataAccess stateDataAccess) {
        this.eventManager = eventManager;
        this.stateDataAccess = stateDataAccess;
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogRepository;
    }

    public void updateSelectedTourLog(TourLog tl) {
        stateDataAccess.updateSelectedTourLog(tl);
    }
    public TourLog getSelectedTourLog() {
        return stateDataAccess.getSelectedTourLog();
    }

    public List<TourLog> getTourLogs() {
        Optional<TourEntity> selectedTourEntity = tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if (selectedTourEntity.isEmpty()) return new ArrayList<>();

        UUID tourId = selectedTourEntity.get().getId();

        return tourLogRepository.findByTourId(tourId).stream()
                .map(this::mapEntityToModel)
                .toList();
    }


    public void add(TourLog tl) throws IllegalAccessException, NotBoundException {
        hasNullProperties(tl);

        Optional<TourEntity> selectedTourEntity =
                tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());

        if (selectedTourEntity.isEmpty()) {
            throw new NotBoundException("No tour has been selected!");
        }

        TourEntity tourEntity = selectedTourEntity.get();

        TourLogEntity entity = new TourLogEntity();
        entity.setComment(tl.getComment());
        entity.setRating(tl.getRating());
        entity.setActualTime(tl.getActualTime());
        entity.setActualDistance(tl.getActualDistance());

        entity.setTour(tourEntity);

        tourLogRepository.save(entity);

        eventManager.publish(Events.TOURLOGS_CHANGED, "NEW_TOURLOG");
    }

    public void edit(TourLog tl) throws IllegalAccessException, NotBoundException {
        hasNullProperties(tl);

        Optional<TourLogEntity> selectedTourLogEntity =
                tourLogRepository.findByLocalDate(stateDataAccess.getSelectedTourLog().getDateTime());

        if (selectedTourLogEntity.isEmpty()) {
            throw new NotBoundException("No tour has been selected!");
        }
        mapModelToEntity(selectedTourLogEntity.get(), tl);
        tourLogRepository.save(selectedTourLogEntity.get());

        eventManager.publish(Events.TOURLOGS_CHANGED, "EDITED_TOURLOG");
    }

    public void remove() {
        Optional<TourLogEntity> selectedTourLogEntity =
                tourLogRepository.findByLocalDate(stateDataAccess.getSelectedTourLog().getDateTime());

        tourLogRepository.delete(selectedTourLogEntity.get());
        eventManager.publish(Events.TOURLOGS_CHANGED, "EDITED_TOURLOG");
    }

    private void hasNullProperties(TourLog tourLog) throws IllegalAccessException {
        if (tourLog == null) throw new IllegalAccessException();

        for (Field field : TourLog.class.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(tourLog);

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

    public TourLog mapEntityToModel(TourLogEntity tle)
    {
        TourLog tourLog = new TourLog();
        tourLog.setActualDistance(tle.getActualDistance());
        tourLog.setActualTime(tle.getActualTime());
        tourLog.setComment(tle.getComment());
        tourLog.setRating(tle.getRating());
        tourLog.setDateTimeStr(tle.getDateTime());
        return tourLog;
    }

    public void mapModelToEntity(TourLogEntity entity, TourLog model)
    {
        entity.setComment(model.getComment());
        entity.setRating(model.getRating());
        entity.setActualTime(model.getActualTime());
        entity.setActualDistance(model.getActualDistance());
    }

}
