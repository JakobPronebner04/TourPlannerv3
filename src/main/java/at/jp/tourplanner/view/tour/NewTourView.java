package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.NewTourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTourView implements Initializable {

    private final NewTourViewModel viewModel;

    @FXML
    private TextField tourName;
    @FXML
    private TextField tourDescription;
    @FXML
    private TextField tourStart;
    @FXML
    private TextField tourDestination;
    @FXML
    private ChoiceBox<String> transportChoiceBox;

    @FXML
    private Text errorMessage;

    public NewTourView(NewTourViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transportChoiceBox.valueProperty().bindBidirectional(viewModel.selectedTransportTypeProperty());
        tourName.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescription.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourStart.textProperty().bindBidirectional(viewModel.tourStartProperty());
        tourDestination.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
        errorMessage.textProperty().bind(viewModel.errorMessageProperty());
    }

    public void onAddNewTour()
    {
        viewModel.addTour();
    }
}
