package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.DetailedTourViewModel;
import at.jp.tourplanner.viewmodel.tourlog.DetailedTourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailedTourView implements Initializable {
    private final DetailedTourViewModel viewModel;
    @FXML
    private TextArea detailedTourDescription;

    public DetailedTourView(DetailedTourViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailedTourDescription.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
    }

    public void onBackClicked() {
        viewModel.close();
    }
}
