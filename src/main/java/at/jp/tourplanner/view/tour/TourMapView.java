package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.TourMapViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourMapView implements Initializable {

    private final TourMapViewModel viewModel;

    @FXML
    private WebView webViewMap;

    public TourMapView(TourMapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.viewModel.setWebEngine(webViewMap.getEngine());

        viewModel.init();
    }
}
