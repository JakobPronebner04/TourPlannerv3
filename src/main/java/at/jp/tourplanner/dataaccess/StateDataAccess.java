package at.jp.tourplanner.dataaccess;

import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;

import java.util.Optional;

public class StateDataAccess {
    private TourLog selectedTourLog;
    private Tour selectedTour;
    private FilterTerm selectedFilterTerm;

    public StateDataAccess() {
    }
    public TourLog getSelectedTourLog() {
        return selectedTourLog;
    }
    public Tour getSelectedTour() {
        return selectedTour;
    }
    public Optional<FilterTerm> getSelectedFilterTerm() {
        if (selectedFilterTerm == null) {
            return Optional.empty();
        };
        return Optional.of(selectedFilterTerm);
    }

    public void updateSelectedTour(Tour newSelectedTour)
    {
        selectedTour = newSelectedTour;
    }
    public void updateSelectedTourLog(TourLog newSelectedTourLog)
    {
        selectedTourLog = newSelectedTourLog;
    }
    public void updateFilter(FilterTerm filterTerm)
    {
        this.selectedFilterTerm = filterTerm;
    }
}
