package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.TourSearchViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TourSearchView implements Initializable {
    private final TourSearchViewModel viewModel;
    @FXML
    private ChoiceBox<String> filterTypeChoiceBox;

    @FXML
    private TextField searchInput;

    public TourSearchView(TourSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterTypeChoiceBox.valueProperty().bindBidirectional(viewModel.selectedFilterTypeProperty());
        searchInput.textProperty().bindBidirectional(viewModel.searchTextProperty());
    };
}
