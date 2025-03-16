package at.jp.tourplanner.repository;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;

public class StateRepository {
    private static StateRepository stateRepository;
    private TourLog selectedTourLog;
    private Tour selectedTour;
    private Tour prevTour;

    private StateRepository() {
        selectedTourLog = new TourLog();
        selectedTour = new Tour();
        prevTour = new Tour();
    }
    public static StateRepository getInstance() {
        if (stateRepository == null) {
            stateRepository = new StateRepository();
        }
        return stateRepository;
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
