package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.viewmodel.tourlog.TourLogMenuViewModel;
import at.jp.tourplanner.window.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogMenuView implements Initializable {
    private final TourLogMenuViewModel viewModel;
    @FXML
    private Button addTourLogButton;
    @FXML
    private Button editTourLogButton;
    @FXML
    private Button removeTourLogButton;
    @FXML
    private Button detailsTourLogButton;

    public TourLogMenuView(TourLogMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTourLogButton.disableProperty().bind(viewModel.addDisabledProperty());
        editTourLogButton.disableProperty().bind(viewModel.editDisabledProperty());
        removeTourLogButton.disableProperty().bind(viewModel.removeDisabledProperty());
        detailsTourLogButton.disableProperty().bind(viewModel.detailsDisabledProperty());
    }
    public void onTourLogAdd() {
        viewModel.openNewTourLogWindow(Windows.NEW_TOURLOG_WINDOW);
    }
    public void onTourLogEdit() {
        viewModel.openNewTourLogWindow(Windows.EDIT_TOURLOG_WINDOW);
    }
    public void onTourLogRemove() {viewModel.removeTourLog();}
    public void onTourLogDetails() {viewModel.openNewTourLogWindow(Windows.DETAILS_TOURLOG_WINDOW);}
}
