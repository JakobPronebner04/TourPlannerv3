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
        this.viewModel.initMap(webViewMap.getEngine());
    }
}
