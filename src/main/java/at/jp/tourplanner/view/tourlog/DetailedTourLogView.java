package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.viewmodel.tourlog.DetailedTourLogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailedTourLogView implements Initializable {
    private final DetailedTourLogViewModel viewModel;
    @FXML
    private TextArea detailedTourLogComment;

    public DetailedTourLogView(DetailedTourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailedTourLogComment.textProperty().bindBidirectional(viewModel.tourLogCommentProperty());
    }

    public void onBackClicked() {
        viewModel.close();
    }
}
