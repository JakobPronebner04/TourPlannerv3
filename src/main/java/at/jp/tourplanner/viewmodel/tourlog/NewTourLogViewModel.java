package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.exception.ErrorHandlingMode;
import at.jp.tourplanner.exception.ExceptionHandler;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import at.jp.tourplanner.window.Windows;
import jakarta.validation.ValidationException;
import javafx.beans.property.*;


public class NewTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final StringProperty actualTimeProperty;
    private final StringProperty actualDistanceProperty;
    private final StringProperty commentProperty;
    private final IntegerProperty ratingProperty;
    private final IntegerProperty difficultyProperty;

    private final StringProperty errorMessageProperty;

    public NewTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        this.actualTimeProperty = new SimpleStringProperty("");
        this.actualDistanceProperty = new SimpleStringProperty("");
        this.commentProperty = new SimpleStringProperty("");
        this.ratingProperty = new SimpleIntegerProperty(0);
        this.difficultyProperty = new SimpleIntegerProperty(0);
        this.errorMessageProperty = new SimpleStringProperty("");
    }

    public StringProperty tourLogCommentProperty() {
        return commentProperty;
    }

    public IntegerProperty tourLogRatingProperty() {
        return ratingProperty;
    }
    public IntegerProperty tourLogDifficultyProperty() { return difficultyProperty; }

    public StringProperty errorMessageProperty() {
        return errorMessageProperty;
    }
    public StringProperty tourLogActualTimeProperty() {return actualTimeProperty;}
    public StringProperty tourLogActualDistanceProperty() {return actualDistanceProperty;}

    public void addTourLog() {
        try {
            TourLog newTourLog = new TourLog();

            newTourLog.setComment(commentProperty.get());
            newTourLog.setRating(ratingProperty.get());
            newTourLog.setDifficulty(difficultyProperty.get());
            newTourLog.setActualTime(Float.parseFloat(actualTimeProperty.getValue()));
            newTourLog.setActualDistance(Float.parseFloat(actualDistanceProperty.getValue()));
            tourLogService.add(newTourLog);
            windowManager.closeWindow(Windows.NEW_TOURLOG_WINDOW);
        }catch(NumberFormatException e) {
            errorMessageProperty.set("Time and Distance should not be emtpy!");
        }catch(Exception e) {
            String msg = ExceptionHandler.handle(e, ErrorHandlingMode.LOG_ONLY);
            errorMessageProperty.set(msg);
        }
    }
}
