package at.jp.tourplanner.service;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.repository.StateRepository;
import javafx.beans.property.StringProperty;

import java.lang.reflect.Field;
import java.util.*;

public class TourLogService {
    private final EventManager eventManager;
    private final StateRepository stateRepository;
    private final Map<Tour,List<TourLog>> tourLogsMap;

    public TourLogService(EventManager eventManager, StateRepository stateRepository) {
        this.eventManager = eventManager;
        this.stateRepository = stateRepository;
        this.tourLogsMap = new HashMap<>();
    }

    public void updateSelectedTourLog(TourLog tl) {
        stateRepository.updateSelectedTourLog(tl);
    }
    public TourLog getSelectedTourLog() {
        return stateRepository.getSelectedTourLog();
    }

    public List<TourLog> getTourLogs() {

        if (!tourLogsMap.containsKey(this.stateRepository.getSelectedTour())) {
            return new ArrayList<>();
        }

        List<TourLog> logs = tourLogsMap.get(this.stateRepository.getSelectedTour());

        if (logs == null || logs.isEmpty()) {
            return new ArrayList<>();
        }

        return logs;
    }

    public void add(TourLog tl) throws IllegalAccessException {
        hasNullProperties(tl);

        this.tourLogsMap.putIfAbsent(this.stateRepository.getSelectedTour(), new ArrayList<>());
        this.tourLogsMap.get(this.stateRepository.getSelectedTour()).add(tl);
        eventManager.publish(Events.TOURLOGS_CHANGED, "NEW_TOURLOG");
    }
    public void update()
    {
        List<TourLog> tourLogs = this.tourLogsMap.get(this.stateRepository.getPrevSelectedTour());
        tourLogsMap.remove(this.stateRepository.getPrevSelectedTour());
        tourLogsMap.put(stateRepository.getSelectedTour(), tourLogs);
        eventManager.publish(Events.TOURS_CHANGED, "EDITED_TOURS");
    }

    public void changeAndAdd(TourLog tl) throws IllegalAccessException {
        hasNullProperties(tl);
        List<TourLog> tourLogs = tourLogsMap.get(this.stateRepository.getSelectedTour());
        if (tourLogs == null) {
            return;
        }

        tourLogs.remove(this.stateRepository.getSelectedTourLog());
        tourLogs.add(tl);
        tourLogsMap.put(this.stateRepository.getSelectedTour(), tourLogs);
        eventManager.publish(Events.TOURLOGS_CHANGED, "EDITED_TOURLOG");
    }

    public void remove() {
        List<TourLog> tourLogs = tourLogsMap.get(this.stateRepository.getSelectedTour());
        if (tourLogs != null) {
            tourLogs.remove(this.stateRepository.getSelectedTourLog());
        }
        tourLogsMap.put(this.stateRepository.getSelectedTour(), tourLogs);
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

}
