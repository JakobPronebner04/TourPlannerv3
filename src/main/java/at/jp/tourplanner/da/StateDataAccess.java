package at.jp.tourplanner.da;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;

public class StateDataAccess {
    private static StateDataAccess stateDataAccess;
    private TourLog selectedTourLog;
    private Tour selectedTour;
    private Tour prevTour;

    private StateDataAccess() {
        selectedTourLog = new TourLog();
        selectedTour = new Tour();
        prevTour = new Tour();
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

    public Tour getPrevSelectedTour() {
        return prevTour;
    }

    public void updateSelectedTour(Tour newSelectedTour)
    {
        selectedTour = newSelectedTour;
    }
    public void updateSelectedTourPrev(Tour prevTour)
    {
        this.prevTour = prevTour;
    }
    public void updateSelectedTourLog(TourLog newSelectedTourLog)
    {
        selectedTourLog = newSelectedTourLog;
    }
}
