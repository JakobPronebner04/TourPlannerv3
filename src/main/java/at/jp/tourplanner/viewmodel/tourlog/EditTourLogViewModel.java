package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.*;

public class EditTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty actualTimeProperty;
    private final StringProperty actualDistanceProperty;
    private final StringProperty commentProperty;
    private final IntegerProperty ratingProperty;
    private final StringProperty errorMessageProperty;

    public EditTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        TourLog selectedTourLog = tourLogService.getSelectedTourLog();
        this.errorMessageProperty = new SimpleStringProperty("");
        this.actualTimeProperty = new SimpleStringProperty(Float.toString(selectedTourLog.getActualTime()));
        this.actualDistanceProperty = new SimpleStringProperty(Float.toString(selectedTourLog.getActualDistance()));
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

    public StringProperty tourLogActualTimeProperty() {return actualTimeProperty;}
    public StringProperty tourLogActualDistanceProperty() {return actualDistanceProperty;}

    public void editSelectedTourLog() {
        try {
            TourLog updatedTourLog = new TourLog();
            updatedTourLog.setComment(commentProperty.get());
            updatedTourLog.setActualTime(Float.parseFloat(actualTimeProperty.getValue()));
            updatedTourLog.setActualDistance(Float.parseFloat(actualDistanceProperty.getValue()));
            updatedTourLog.setRating(ratingProperty.get());

            tourLogService.changeAndAdd(updatedTourLog);
            windowManager.closeWindow();
        } catch (IllegalAccessException | NumberFormatException e) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}
