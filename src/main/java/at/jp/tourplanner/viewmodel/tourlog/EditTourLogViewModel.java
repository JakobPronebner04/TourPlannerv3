package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class EditTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty commentProperty;
    private final IntegerProperty ratingProperty;
    private final StringProperty errorMessageProperty;

    public EditTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        TourLog selectedTourLog = tourLogService.getSelectedTourLog();
        this.errorMessageProperty = new SimpleStringProperty("");
        this.commentProperty = new SimpleStringProperty(selectedTourLog.getComment());
        this.ratingProperty = new SimpleIntegerProperty(selectedTourLog.getRating());
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

    public void editSelectedTourLog() {
        try {
            TourLog updatedTourLog = new TourLog();
            updatedTourLog.setComment(commentProperty.get());
            updatedTourLog.setRating(ratingProperty.get());

            tourLogService.changeAndAdd(updatedTourLog);
            windowManager.closeWindow();
        } catch (IllegalAccessException e) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}
