package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.viewmodel.tour.TourHistoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
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

    public TourHistoryView(TourHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(cellData -> cellData.getValue().tourNameProperty());
        colDescription.setCellValueFactory(cellData -> cellData.getValue().tourDescriptionProperty());
        colStart.setCellValueFactory(cellData -> cellData.getValue().tourStartProperty());
        colDestination.setCellValueFactory(cellData -> cellData.getValue().tourDestinationProperty());
        viewModel.selectedTour().bind(tourHistoryTable.getSelectionModel().selectedItemProperty());
        if(tourHistoryTable!=null) tourHistoryTable.setItems(viewModel.getTourHistory());
    }
}