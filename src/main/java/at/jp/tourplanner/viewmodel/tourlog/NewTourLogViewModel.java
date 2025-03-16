package at.jp.tourplanner.viewmodel.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.window.WindowManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NewTourLogViewModel {
    private final TourLogService tourLogService;
    private final WindowManager windowManager;
    private final TourLog tourLog;
    private final StringProperty errorMessageProperty;


    public NewTourLogViewModel(TourLogService tourLogService, WindowManager windowManager) {
        this.tourLogService = tourLogService;
        this.windowManager = windowManager;
        errorMessageProperty = new SimpleStringProperty("");
        tourLog = new TourLog();
    }

    public StringProperty tourLogCommentProperty() { return tourLog.tourLogCommentProperty(); }
    public IntegerProperty tourLogRatingProperty() { return tourLog.tourLogRatingProperty(); }
    public StringProperty errorMessageProperty() {return errorMessageProperty;}
    public void addTourLog()
    {
        try
        {
            tourLogService.add(tourLog);
            windowManager.closeWindow();
        }catch(IllegalAccessException ex){
            errorMessageProperty.set("Some inputs might be empty!");
        }

    }
}