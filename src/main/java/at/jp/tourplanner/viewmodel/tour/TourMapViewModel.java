package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.MapRendererService;
import at.jp.tourplanner.service.TourService;
import javafx.scene.web.WebEngine;

public class TourMapViewModel {

    private final TourService tourService;
    private final EventManager eventManager;
    private final MapRendererService mapRendererService;
    private WebEngine webEngine;

    public TourMapViewModel(EventManager eventManager, TourService tourService, MapRendererService mapRendererService) {
        this.eventManager = eventManager;
        this.mapRendererService = mapRendererService;
        this.tourService = tourService;
        this.eventManager.subscribe(Events.TOUR_SELECTED, this::onTourSelected);
    }

    private void onTourSelected(Boolean deselected) {
        webEngine.executeScript(mapRendererService.getClearScript());
        if(!deselected) {
            webEngine.executeScript(mapRendererService.getDrawScript(tourService.getRouteGeocodes()));
        }
    }
    public void initMap(WebEngine webEngine) {
        this.webEngine = webEngine;
        webEngine.load(mapRendererService.getInitialState());
    }
}
