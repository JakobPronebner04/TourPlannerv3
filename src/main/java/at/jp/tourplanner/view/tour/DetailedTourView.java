package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.DetailedTourViewModel;
import at.jp.tourplanner.viewmodel.tourlog.DetailedTourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailedTourView implements Initializable {
    private final DetailedTourViewModel viewModel;
    @FXML
    private TextArea detailedTourDescription;
    @FXML
    private WebView webViewMap;

    public DetailedTourView(DetailedTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailedTourDescription.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        this.viewModel.initMap(webViewMap.getEngine());
    }

    public void onBackClicked() {
        viewModel.close();
    }
}
