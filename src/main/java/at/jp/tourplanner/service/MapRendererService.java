package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.repository.TourRepositoryORM;
import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapRendererService implements MapRenderer{
    private final TourRepositoryORM tourRepository;
    private final StateDataAccess stateDataAccess;
    public MapRendererService(TourRepositoryORM tourRepository, StateDataAccess stateDataAccess) {
        this.tourRepository = tourRepository;
        this.stateDataAccess = stateDataAccess;
    }
    @Override
    public String getInitialState() {
        return getClass().getResource("/at/jp/tourplanner/map.html").toExternalForm();
    }
    @Override
    public String getClearScript() {
        return
            """
                map.eachLayer(function(layer) {
                    if (!!layer.toGeoJSON) {
                        map.removeLayer(layer);
                    }
                });
                map.setView([51.505, -0.09], 13);
            """;
    }
    @Override
    public String getDrawScript() {
        Optional<TourEntity> tour = tourRepository.findByName(this.stateDataAccess.getSelectedTour().getTourName());
        if(tour.isEmpty()) {
            throw new RuntimeException("Coordinates not found!");
        }

        StringBuilder scriptBuilder = new StringBuilder("var latlngs = ");
        scriptBuilder.append(tour.get().getGeocodeDirections().getJsonDirections());
        scriptBuilder.append("; var polyline = L.polyline(latlngs, {color: 'blue'}).addTo(map); map.fitBounds(polyline.getBounds());");
        return scriptBuilder.toString();
    }
}