package at.jp.tourplanner.dataaccess;

import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class StateDataAccess {
    private TourLog selectedTourLog;
    private Tour selectedTour;
    private FilterTerm selectedTourFilterTerm;
    private FilterTerm selectedTourLogFilterTerm;
    private BufferedImage selectedImage;
    private String exceptionMessage;

    public StateDataAccess() {
    }
    public BufferedImage getSelectedImage() {
        return selectedImage;
    }
    public TourLog getSelectedTourLog() {
        return selectedTourLog;
    }
    public Tour getSelectedTour() {
        return selectedTour;
    }
    public String getException(){
        return exceptionMessage;
    }

    public Optional<FilterTerm> getSelectedTourFilterTerm() {
        if (selectedTourFilterTerm == null || selectedTourFilterTerm.getText().isEmpty()) {
            return Optional.empty();
        };
        return Optional.of(selectedTourFilterTerm);
    }

    public Optional<FilterTerm> getSelectedTourLogFilterTerm() {
        if (selectedTourLogFilterTerm == null || selectedTourLogFilterTerm.getText().isEmpty()) {
            return Optional.empty();
        };
        return Optional.of(selectedTourLogFilterTerm);
    }
    public void updateException(String eMessage) {
        exceptionMessage = eMessage;
    }
    public void updateSelectedImage(BufferedImage selectedImage) {
        this.selectedImage = selectedImage;
    }
    public void updateSelectedTour(Tour newSelectedTour)
    {
        selectedTour = newSelectedTour;
    }
    public void updateSelectedTourLog(TourLog newSelectedTourLog)
    {
        selectedTourLog = newSelectedTourLog;
    }

    public void updateTourFilter(FilterTerm filterTerm)
    {
        this.selectedTourFilterTerm = filterTerm;
    }
    public void updateTourLogFilter(FilterTerm filterTerm)
    {
        this.selectedTourLogFilterTerm = filterTerm;
    }
}
