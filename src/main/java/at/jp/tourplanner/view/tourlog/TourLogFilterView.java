package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.viewmodel.tour.TourFilterViewModel;
import at.jp.tourplanner.viewmodel.tourlog.TourLogFilterViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogFilterView implements Initializable {
    private final TourLogFilterViewModel viewModel;

    @FXML
    private ChoiceBox<String> filterTypeChoiceBox;
    @FXML
    private TextField filterInput;
    @FXML
    private Button filterTourLogButton;
    @FXML
    private Button showAllTourLogsButton;

    public TourLogFilterView(TourLogFilterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewModel.selectedFilterTypeProperty().bind(filterTypeChoiceBox.valueProperty());
        filterInput.textProperty().bindBidirectional(viewModel.filterTextProperty());
        filterTourLogButton.disableProperty().bind(viewModel.filterTourLogProperty());
        showAllTourLogsButton.disableProperty().bind(viewModel.showAllTourLogsProperty());
    };

    public void onFilterTourLogClicked(){
        viewModel.filterTour();
    }
    public void onShowAllTourLogsClicked()
    {
        viewModel.showAll();
    }

}
