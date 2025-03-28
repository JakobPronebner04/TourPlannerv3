package at.jp.tourplanner.da;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;

public class StateDataAccess {
    private static StateDataAccess stateDataAccess;
    private TourLog selectedTourLog;
    private Tour selectedTour;

    private StateDataAccess() {
        selectedTourLog = new TourLog();
        selectedTour = new Tour();
    }
    public static StateDataAccess getInstance() {
        if (stateDataAccess == null) {
            stateDataAccess = new StateDataAccess();
        }
        return stateDataAccess;
    }

    public TourLog getSelectedTourLog() {
        return selectedTourLog;
    }
    public Tour getSelectedTour() {
        return selectedTour;
    }

    public void updateSelectedTour(Tour newSelectedTour)
    {
        selectedTour = newSelectedTour;
    }
    public void updateSelectedTourLog(TourLog newSelectedTourLog)
    {
        selectedTourLog = newSelectedTourLog;
    }
}
