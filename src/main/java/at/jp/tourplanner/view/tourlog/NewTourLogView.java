package at.jp.tourplanner.view.tourlog;

import at.jp.tourplanner.viewmodel.tourlog.NewTourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static at.jp.tourplanner.utils.TextFieldFormatter.setTextFieldFormatFloat;

public class NewTourLogView implements Initializable {
    private final NewTourLogViewModel viewModel;
    @FXML
    private TextArea commentInput;
    @FXML
    private TextField actualTimeInput;
    @FXML
    private TextField actualDistanceInput;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Text errorMessage;

    public NewTourLogView(NewTourLogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commentInput.textProperty().bindBidirectional(viewModel.tourLogCommentProperty());
        actualTimeInput.textProperty().bindBidirectional(viewModel.tourLogActualTimeProperty());
        actualDistanceInput.textProperty().bindBidirectional(viewModel.tourLogActualDistanceProperty());
        ratingSlider.valueProperty().bindBidirectional(viewModel.tourLogRatingProperty());
        errorMessage.textProperty().bind(viewModel.errorMessageProperty());
        setTextFieldFormatFloat(actualTimeInput);
        setTextFieldFormatFloat(actualDistanceInput);
    }


    public void onAddNewTourLogEntry()
    {
        viewModel.addTourLog();
    }
}

