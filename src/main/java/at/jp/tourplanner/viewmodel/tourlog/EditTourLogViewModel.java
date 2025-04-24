package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import jakarta.validation.ValidationException;
import javafx.beans.property.*;

import java.rmi.NotBoundException;

public class EditTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty actualTimeProperty;
    private final StringProperty actualDistanceProperty;
    private final StringProperty commentProperty;
    private final IntegerProperty ratingProperty;
    private final IntegerProperty difficultyProperty;
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
        this.difficultyProperty = new SimpleIntegerProperty(selectedTourLog.getDifficulty());
    }

    public StringProperty tourLogCommentProperty() {
        return commentProperty;
    }

    public IntegerProperty tourLogRatingProperty() {
        return ratingProperty;
    }
    public IntegerProperty tourLogDifficultyProperty() {
        return difficultyProperty;
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
            updatedTourLog.setDifficulty(difficultyProperty.get());

            tourLogService.edit(updatedTourLog);
            windowManager.closeWindow();
        }catch(NumberFormatException e) {
            errorMessageProperty.set("Time and Distance should not be emtpy!");
        }catch(RuntimeException e) {
            errorMessageProperty.set(e.getMessage());
        }
    }
}
