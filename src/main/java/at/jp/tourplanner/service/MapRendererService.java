package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import javafx.scene.web.WebEngine;

import java.util.List;

public class MapRendererService implements MapRenderer{

    private WebEngine webEngine;

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
    @Override
    public void loadInitial() {
        String html = getClass().getResource("/at/jp/tourplanner/map.html").toExternalForm();
        webEngine.load(html);
    }
    @Override
    public void clear() {
        String script =
            """
                map.eachLayer(function(layer) {
                    if (!!layer.toGeoJSON) {
                        map.removeLayer(layer);
                    }
                });
                map.setView([51.505, -0.09], 13);
            """;
        
        webEngine.executeScript(script);
    }
    @Override
    public void draw(List<Geocode> geocodes) {
        StringBuilder scriptBuilder = new StringBuilder("var latlngs = [");
        for (Geocode g : geocodes) {
            scriptBuilder.append(String.format(java.util.Locale.ENGLISH, "[%f, %f],", g.getLatitude(), g.getLongitude()));
        }
        scriptBuilder.setLength(scriptBuilder.length() - 1);
        scriptBuilder.append("]; var polyline = L.polyline(latlngs, {color: 'blue'}).addTo(map); map.fitBounds(polyline.getBounds());");
        webEngine.executeScript(scriptBuilder.toString());
    }
}