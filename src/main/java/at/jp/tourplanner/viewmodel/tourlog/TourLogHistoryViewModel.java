package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

public class TourLogHistoryViewModel {
    private final EventManager eventManager;
    private final TourLogService tourLogService;
    private final WindowManager windowManager;

    private final BooleanProperty tourLogTableDisabled = new SimpleBooleanProperty(true);
    private final ObjectProperty<TourLog> selectedTourLog = new SimpleObjectProperty<>();

    private final ObservableList<TourLog> tourLogHistory
            = FXCollections.observableArrayList();

    public TourLogHistoryViewModel(EventManager eventManager, TourLogService tourLogService,WindowManager windowManager) {
        this.eventManager = eventManager;
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;

        this.selectedTourLog.addListener(this::onSelectedTourLogChanged);
        this.eventManager.subscribe(
                Events.TOURLOGS_CHANGED, this::onTourLogsChanged
        );

        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onTourSelectedChanged
        );
    }

    public void onSelectedTourLogChanged(Observable observable, TourLog oldTourLog, TourLog newTourLog)
    {
        boolean isDisabled = (newTourLog == null);
        tourLogService.updateSelectedTourLog(newTourLog);
        this.eventManager.publish(Events.TOURLOG_SELECTED, isDisabled);
    }

    public void onTourLogsChanged(String message) {
        tourLogHistory.clear();
        tourLogHistory.addAll(tourLogService.getTourLogs());
    }
    public void onTourSelectedChanged(boolean state) {
            tourLogTableDisabled.set(state);
            if(state) {
                tourLogHistory.clear();
                return;
            }
        tourLogHistory.setAll(tourLogService.getTourLogs());
    }
    public BooleanProperty tourLogTableDisabledProperty() {
        return tourLogTableDisabled;
    }
    public ObservableList<TourLog> getTourLogHistory() {
        return tourLogHistory;
    }
    public ObjectProperty<TourLog> selectedTourLog() {
        return selectedTourLog;
    }
}
