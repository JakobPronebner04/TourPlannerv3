package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.TourMenuViewModel;
import at.jp.tourplanner.window.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TourMenuView implements Initializable {

    private final TourMenuViewModel viewModel;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button detailsButton;

    public TourMenuView(TourMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.disableProperty().bind(viewModel.editDisabledProperty());
        removeButton.disableProperty().bind(viewModel.removeDisabledProperty());
        detailsButton.disableProperty().bind(viewModel.detailsDisabledProperty());
    }

    public void onAddTourClicked(){
        viewModel.openNewTourWindow();
    }

    public void onEditTourClicked(){
        viewModel.openEditTourWindow();
    }

    public void onRemoveTourClicked(){
        viewModel.deleteTour();
    }

    public void onTourDetails() {
        viewModel.openDetailsTourWindow();
    }
}
