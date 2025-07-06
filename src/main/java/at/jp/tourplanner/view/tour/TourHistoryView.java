package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.utils.ControlsFormatter;
import at.jp.tourplanner.viewmodel.tour.TourHistoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TourHistoryView implements Initializable {

    private final TourHistoryViewModel viewModel;

    @FXML
    private TableView<Tour> tourHistoryTable;

    @FXML
    private TableColumn<Tour, String> colName;
    @FXML
    private TableColumn<Tour, String> colStart;
    @FXML
    private TableColumn<Tour, String> colDestination;
    @FXML
    private TableColumn<Tour, String> colDescription;
    @FXML
    private TableColumn<Tour, String> colTransportType;
    @FXML
    private TableColumn<Tour, String> colDuration;
    @FXML
    private TableColumn<Tour, String> colDistance;
    @FXML
    private TableColumn<Tour, Integer> colPopularity;
    @FXML
    private TableColumn<Tour, Integer> colChildfriendliness;

    public TourHistoryView(TourHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(new PropertyValueFactory<>("tourName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("tourDescription"));
        ControlsFormatter.setTableColumnCutOff(colDescription);

        colStart.setCellValueFactory(new PropertyValueFactory<>("tourStart"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("tourDestination"));
        colTransportType.setCellValueFactory(new PropertyValueFactory<>("tourTransportType"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("tourDuration"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("tourDistance"));
        colPopularity.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        colChildfriendliness.setCellValueFactory(new PropertyValueFactory<>("childFriendliness"));

        viewModel.selectedTour().bind(tourHistoryTable.getSelectionModel().selectedItemProperty());
        tourHistoryTable.setItems(viewModel.getTourHistory());
    }

}
