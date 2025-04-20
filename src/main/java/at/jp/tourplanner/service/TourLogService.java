package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.repository.*;
import at.jp.tourplanner.utils.PropertyValidator;
import jakarta.validation.ValidationException;

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
    public void updateSelectedFilter(FilterTerm filterTerm) {
        stateDataAccess.updateTourLogFilter(filterTerm);
        eventManager.publish(Events.TOURLOGS_CHANGED, "FILTER_TOURLOGS");
    }
    public TourLog getSelectedTourLog() {
        return stateDataAccess.getSelectedTourLog();
    }

    public List<TourLog> getTourLogs() {
        Optional<TourEntity> selectedTourEntity = tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if (selectedTourEntity.isEmpty()) return new ArrayList<>();

        UUID tourId = selectedTourEntity.get().getId();
        Optional<FilterTerm> filterTerm =  this.stateDataAccess.getSelectedTourLogFilterTerm();
        if (filterTerm.isEmpty()) {
            return tourLogRepository.findByTourId(tourId).stream()
                    .map(this::mapEntityToModel)
                    .toList();
        }
        return tourLogRepository.findByFilterTermAndTourId(
                tourId,
                filterTerm.get().getText(),
                filterTerm.get().getType()).stream()
                .map(this::mapEntityToModel)
                .toList();
    }


    public void add(TourLog tl) {
        PropertyValidator.validateOrThrow(tl);

        Optional<TourEntity> selectedTourEntity =
                tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if (selectedTourEntity.isEmpty()) {
            throw new RuntimeException("No tour has been selected!");
        }

        TourEntity tourEntity = selectedTourEntity.get();

        TourLogEntity entity = new TourLogEntity();

        mapModelToEntity(entity,tl);
        entity.setTour(tourEntity);

        tourLogRepository.save(entity);

        eventManager.publish(Events.TOURLOGS_CHANGED, "NEW_TOURLOG");
    }

    public void edit(TourLog tl) {
        PropertyValidator.validateOrThrow(tl);

        Optional<TourLogEntity> selectedTourLogEntity =
                tourLogRepository.findByLocalDate(stateDataAccess.getSelectedTourLog().getDateTime());
        if (selectedTourLogEntity.isEmpty()) {
            throw new RuntimeException("No tour has been selected!");
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


    public TourLog mapEntityToModel(TourLogEntity tle)
    {
        TourLog tourLog = new TourLog();
        tourLog.setActualDistance(tle.getActualDistance());
        tourLog.setActualTime(tle.getActualTime());
        tourLog.setComment(tle.getComment());
        tourLog.setRating(tle.getRating());
        tourLog.setDateTime(tle.getDateTime());
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
