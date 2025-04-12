package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.viewmodel.tour.TourMapViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TourMapView implements Initializable {

    @FXML
    private WebView webViewMap;

    private final TourMapViewModel viewModel;

    public TourMapView(TourMapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = webViewMap.getEngine();
        String html = getClass().getResource("/at/jp/tourplanner/map.html").toExternalForm();
        webEngine.load(html);

        viewModel.routeCoordinatesProperty().addListener((observable, oldVal, newVal) -> {
            String clearScript = """
                map.eachLayer(function(layer) {
                    if (!!layer.toGeoJSON) {
                        map.removeLayer(layer);
                    }
                });
                map.setView([51.505, -0.09], 13);
                """;
            webEngine.executeScript(clearScript);

            if (newVal != null && !newVal.isEmpty()) {
                StringBuilder scriptBuilder = new StringBuilder("var latlngs = [");
                for (Geocode g : newVal) {
                    scriptBuilder.append(String.format(java.util.Locale.ENGLISH, "[%f, %f],", g.getLatitude(), g.getLongitude()));
                }
                scriptBuilder.setLength(scriptBuilder.length() - 1);
                scriptBuilder.append("]; var polyline = L.polyline(latlngs, {color: 'blue'}).addTo(map); map.fitBounds(polyline.getBounds());");
                webEngine.executeScript(scriptBuilder.toString());
            }
        });
    }
}
