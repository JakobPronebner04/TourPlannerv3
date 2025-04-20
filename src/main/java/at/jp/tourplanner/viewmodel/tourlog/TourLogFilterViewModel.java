package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.service.TourService;
import javafx.beans.property.*;

public class TourLogFilterViewModel {
    private final TourLogService tourLogService;
    private final EventManager eventManager;
    private final StringProperty filterText = new SimpleStringProperty("");
    private final ObjectProperty<String> selectedFilterType = new SimpleObjectProperty<>("");
    private final BooleanProperty filterTourLog = new SimpleBooleanProperty(true);
    private final BooleanProperty showAllTourLogs = new SimpleBooleanProperty(true);

    public TourLogFilterViewModel(TourLogService tourLogService, EventManager eventManager) {
        this.tourLogService = tourLogService;
        this.eventManager = eventManager;
        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onTourSelectedChanged
        );
    }
    private void onTourSelectedChanged(boolean state)
    {
        filterTourLog.set(state);
        showAllTourLogs.set(state);
    }
    public StringProperty filterTextProperty() {
        return filterText;
    }

    public ObjectProperty<String> selectedFilterTypeProperty() {
        return selectedFilterType;
    }

    public void filterTour() {
        FilterTerm filterTerm = new FilterTerm(filterText.getValue(), selectedFilterType.getValue());
        tourLogService.updateSelectedFilter(filterTerm);
    }
    public void showAll()
    {
        filterText.set("");
        tourLogService.updateSelectedFilter(new FilterTerm(filterText.getValue(),selectedFilterType.getValue()));
    }

    public BooleanProperty filterTourLogProperty()
    {
        return filterTourLog;
    }
    public BooleanProperty showAllTourLogsProperty()
    {
        return showAllTourLogs;
    }
}