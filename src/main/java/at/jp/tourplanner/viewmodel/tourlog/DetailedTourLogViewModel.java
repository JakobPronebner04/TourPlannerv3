package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetailedTourLogViewModel {

    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty commentProperty;

    public DetailedTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        this.commentProperty = new SimpleStringProperty(tourLogService.getSelectedTourLog().getComment());
    }

    public StringProperty tourLogCommentProperty() {
        return commentProperty;
    }

    public void close() {
        windowManager.closeWindow(Windows.DETAILS_TOURLOG_WINDOW);
    }
}
