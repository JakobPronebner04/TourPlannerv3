package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetailedTourLogViewModel {

    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final TourLog shownTourLog;

    public DetailedTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        shownTourLog = new TourLog();
        this.shownTourLog.tourLogCommentProperty().set(tourLogService.getSelectedTourLog().tourLogCommentProperty().get());
    }
    public StringProperty tourLogCommentProperty() { return shownTourLog.tourLogCommentProperty(); }

    public void close()
    {
        windowManager.closeWindow();
    }
}
