package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.*;

public class NewTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty commentProperty;
    private final IntegerProperty ratingProperty;
    private final StringProperty errorMessageProperty;

    public NewTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        this.commentProperty = new SimpleStringProperty("");
        this.ratingProperty = new SimpleIntegerProperty(0);
        this.errorMessageProperty = new SimpleStringProperty("");
    }

    public StringProperty tourLogCommentProperty() {
        return commentProperty;
    }

    public IntegerProperty tourLogRatingProperty() {
        return ratingProperty;
    }

    public StringProperty errorMessageProperty() {
        return errorMessageProperty;
    }

    public void addTourLog() {
        try {
            TourLog newTourLog = new TourLog();
            newTourLog.setComment(commentProperty.get());
            newTourLog.setRating(ratingProperty.get());

            tourLogService.add(newTourLog);
            windowManager.closeWindow();
        } catch (IllegalAccessException ex) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}
