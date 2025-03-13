package at.jp.tourplanner.view;

import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.viewmodel.TourHistoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TourHistoryView implements Initializable {

    private final TourHistoryViewModel viewModel;

    @FXML
    private TableView<Tour> searchHistoryTable;

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
        viewModel.selectedTour()
                .bind(searchHistoryTable.getSelectionModel().selectedItemProperty());
        if(searchHistoryTable!=null) searchHistoryTable.setItems(viewModel.getTourHistory());
    }
}