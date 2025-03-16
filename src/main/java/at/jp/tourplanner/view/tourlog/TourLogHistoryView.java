package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.viewmodel.tourlog.TourLogHistoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogHistoryView implements Initializable {

    private final TourLogHistoryViewModel viewModel;

    @FXML
    private TableView<TourLog> tourLogHistoryTable;

    @FXML
    private TableColumn<TourLog, String> comment;
    @FXML
    private TableColumn<TourLog, Integer> rating;

    public TourLogHistoryView(TourLogHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comment.setCellValueFactory(cellData -> cellData.getValue().tourLogCommentProperty());
        comment.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.split("\n")[0]);
                }
            }
        });

        rating.setCellValueFactory(cellData -> cellData.getValue().tourLogRatingProperty().asObject());
        tourLogHistoryTable.disableProperty().bind(viewModel.tourLogTableDisabledProperty());
        viewModel.selectedTourLog().bind(tourLogHistoryTable.getSelectionModel().selectedItemProperty());
        if(tourLogHistoryTable!=null) tourLogHistoryTable.setItems(viewModel.getTourLogHistory());
    }
}