package at.jp.tourplanner.service.importexport;

import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;

import java.util.List;

public class TourImportExport {
    private Tour tour;
    private List<TourLog> tourLogs;

    public TourImportExport() {}

    public TourImportExport(Tour tour, List<TourLog> tourLogs) {
        this.tour = tour;
        this.tourLogs = tourLogs;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public List<TourLog> getTourLogs() {
        return tourLogs;
    }

    public void setTourLogs(List<TourLog> tourLogs) {
        this.tourLogs = tourLogs;
    }
}
