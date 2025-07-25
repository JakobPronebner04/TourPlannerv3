package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.EditTourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ResourceBundle;

public class EditTourView implements Initializable {

    private final EditTourViewModel viewModel;

    @FXML
    private TextField tourNameEdit;
    @FXML
    private TextArea tourDescriptionEdit;
    @FXML
    private TextField tourStartEdit;
    @FXML
    private TextField tourDestinationEdit;
    @FXML
    private ChoiceBox<String> transportChoiceBox;

    @FXML
    private Text errorMessage;

    public EditTourView(EditTourViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transportChoiceBox.valueProperty().bindBidirectional(viewModel.tourTransportTypeProperty());
        tourNameEdit.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescriptionEdit.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourStartEdit.textProperty().bindBidirectional(viewModel.tourStartProperty());
        tourDestinationEdit.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
        errorMessage.textProperty().bind(viewModel.errorMessageProperty());
    }

    public void onEditSelectedTour()
    {
        viewModel.editSelectedTour();
    }
}
