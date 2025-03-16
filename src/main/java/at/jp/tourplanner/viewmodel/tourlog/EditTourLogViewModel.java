package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EditTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final TourLog editedTourLog;
    private final StringProperty errorMessageProperty;

    public EditTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        TourLog selectedTourLog = tourLogService.getSelectedTourLog();
        errorMessageProperty = new SimpleStringProperty("");
        this.editedTourLog = new TourLog();
        this.editedTourLog.tourLogCommentProperty().set(selectedTourLog.tourLogCommentProperty().get());
        this.editedTourLog.tourLogRatingProperty().set(selectedTourLog.tourLogRatingProperty().get());
    }

    public StringProperty tourLogCommentProperty() { return editedTourLog.tourLogCommentProperty(); }
    public IntegerProperty tourLogRatingProperty() { return editedTourLog.tourLogRatingProperty(); }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void editSelectedTourLog()
    {
        try
        {
            tourLogService.changeAndAdd(editedTourLog);
            windowManager.closeWindow();
        } catch (IllegalAccessException e) {
            errorMessageProperty.set("Some inputs might be empty!");
        }
    }
}
