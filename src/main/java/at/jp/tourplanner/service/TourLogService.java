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
        if(filterTerm.get().getType().equals("AllTourLogFields"))
        {
            return tourLogRepository.findByTourIdAndTextFullSearch(
                            tourId,
                            filterTerm.get().getText())
                    .stream()
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


        TourLogEntity entity = new TourLogEntity();
        mapModelToEntity(entity,tl);

        entity.setTour(selectedTourEntity.get());
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
        if(selectedTourLogEntity.isEmpty()) throw new RuntimeException("No tour has been selected!");
        tourLogRepository.delete(selectedTourLogEntity.get().getId());

        eventManager.publish(Events.TOURLOGS_CHANGED, "REMOVED_TOURLOG");
    }


    public TourLog mapEntityToModel(TourLogEntity tle)
    {
        TourLog tourLog = new TourLog();
        tourLog.setActualDistance(tle.getActualDistance());
        tourLog.setActualTime(tle.getActualTime());
        tourLog.setComment(tle.getComment());
        tourLog.setRating(tle.getRating());
        tourLog.setDifficulty(tle.getDifficulty());
        tourLog.setDateTime(tle.getDateTime());
        return tourLog;
    }

    public void mapModelToEntity(TourLogEntity entity, TourLog model)
    {
        entity.setComment(model.getComment());
        entity.setRating(model.getRating());
        entity.setDifficulty(model.getDifficulty());
        entity.setActualTime(model.getActualTime());
        entity.setActualDistance(model.getActualDistance());
    }
    public void computeAverageValues() {
        Optional<TourEntity> tourEntity = tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if (tourEntity.isEmpty()) throw new RuntimeException("No tour has been selected!");

        List<TourLogEntity> tourLogEntities = tourEntity.get().getTourLogs();
        if (tourLogEntities.isEmpty())
        {
            tourEntity.get().setChildFriendliness(5);
            tourEntity.get().setPopularity(0);
            return;
        }

        double sumDifficulty = 0;
        double sumTime = 0;
        double sumDistance = 0;
        int logCount = tourLogEntities.size();

        for (TourLogEntity log : tourLogEntities) {
            sumDifficulty += log.getDifficulty();
            sumTime += log.getActualTime();
            sumDistance += log.getActualDistance();
        }

        double avgDifficulty = sumDifficulty / logCount;
        double avgTime = sumTime / logCount;
        double avgDistance = sumDistance / logCount;

        int points = 0;

        if (avgDifficulty <= 0.5) {
            points += 4;
        } else if (avgDifficulty <= 1.5) {
            points += 2;
        }

        if (avgTime < 2.0f) {
            points += 3;
        }

        if (avgDistance < 5.0f) {
            points += 3;
        }
        tourEntity.get().setChildFriendliness(points);
        tourEntity.get().setPopularity(tourLogEntities.size());

        tourRepository.save(tourEntity.get());
        eventManager.publish(Events.TOURS_CHANGED,"ADDED_COMPUTED_VALUES");
    }

}
