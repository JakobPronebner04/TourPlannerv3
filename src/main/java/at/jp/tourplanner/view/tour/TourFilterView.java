package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.TourFilterViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TourFilterView implements Initializable {
    private final TourFilterViewModel viewModel;

    @FXML
    private ChoiceBox<String> filterTypeChoiceBox;
    @FXML
    private TextField filterInput;
    @FXML
    private Button filterTourButton;

    public TourFilterView(TourFilterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel.selectedFilterTypeProperty().bind(filterTypeChoiceBox.valueProperty());
        filterInput.textProperty().bindBidirectional(viewModel.filterTextProperty());
    };

    public void onFilterTourClicked(){
        viewModel.filterTour();
    }
    public void onShowAllToursClicked()
    {
        viewModel.showAll();
    }

}
