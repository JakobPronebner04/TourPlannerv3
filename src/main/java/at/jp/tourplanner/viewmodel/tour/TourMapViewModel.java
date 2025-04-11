package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourService;
import javafx.scene.web.WebEngine;

public class TourMapViewModel {

    private WebEngine webEngine;

    private final EventManager eventManager;

    private final TourService tourService;

    public TourMapViewModel(EventManager eventManager, TourService tourService) {
        this.eventManager = eventManager;
        this.tourService = tourService;

        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onSearchTerm
        );
    }

    public void init() {
        String html = getClass()
                .getResource("/at/jp/tourplanner/map.html").toExternalForm();
        this.webEngine.load(html);
    }

    private void onSearchTerm(String message) {
        /*Geocode geocode = searchTermService.getRecentSearch();
        if (null == geocode) {
            return;
        }

        webEngine.executeScript(
                String.format("map.setView([%s, %s], 13);", geocode.getLatitude(), geocode.getLongitude())
        );*/
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
}
