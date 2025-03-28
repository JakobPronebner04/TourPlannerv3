package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.utils.ControlsFormatter;
import at.jp.tourplanner.viewmodel.tourlog.TourLogHistoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class TourLogHistoryView implements Initializable {

    private final TourLogHistoryViewModel viewModel;

    @FXML
    private TableView<TourLog> tourLogHistoryTable;

    @FXML
    private TableColumn<TourLog, String> comment;
    @FXML
    private TableColumn<TourLog, Integer> rating;
    @FXML
    private TableColumn<TourLog, Date> dateTime;
    @FXML
    private TableColumn<TourLog, String> actualTime;
    @FXML
    private TableColumn<TourLog, String> actualDistance;

    public TourLogHistoryView(TourLogHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTimeStr"));
        actualTime.setCellValueFactory(new PropertyValueFactory<>("actualTime"));
        actualDistance.setCellValueFactory(new PropertyValueFactory<>("actualDistance"));
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        ControlsFormatter.setTableColumnCutOff(comment);

        tourLogHistoryTable.disableProperty().bind(viewModel.tourLogTableDisabledProperty());
        viewModel.selectedTourLog().bind(tourLogHistoryTable.getSelectionModel().selectedItemProperty());

        if (tourLogHistoryTable != null) {
            tourLogHistoryTable.setItems(viewModel.getTourLogHistory());
        }
    }
}
