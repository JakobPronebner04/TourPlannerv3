package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.utils.ControlsFormatter;
import at.jp.tourplanner.viewmodel.tourlog.EditTourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class EditTourLogView implements Initializable {
    private final EditTourLogViewModel viewModel;
    @FXML
    private TextArea commentInputEdit;
    @FXML
    private TextField actualTimeInput;
    @FXML
    private TextField actualDistanceInput;
    @FXML
    private Slider ratingSliderEdit;
    @FXML
    private Slider difficultySlider;
    @FXML
    private Text errorMessage;

    public EditTourLogView(EditTourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commentInputEdit.textProperty().bindBidirectional(viewModel.tourLogCommentProperty());
        actualTimeInput.textProperty().bindBidirectional(viewModel.tourLogActualTimeProperty());
        actualDistanceInput.textProperty().bindBidirectional(viewModel.tourLogActualDistanceProperty());
        ratingSliderEdit.valueProperty().bindBidirectional(viewModel.tourLogRatingProperty());
        difficultySlider.valueProperty().bindBidirectional(viewModel.tourLogDifficultyProperty());
        errorMessage.textProperty().bind(viewModel.errorMessageProperty());
        ControlsFormatter.setTextFieldFormatFloat(actualTimeInput);
        ControlsFormatter.setTextFieldFormatFloat(actualDistanceInput);
    }

    public void onEditSelectedTourLog() {
        viewModel.editSelectedTourLog();
    }
}
