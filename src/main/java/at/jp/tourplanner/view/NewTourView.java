package at.jp.tourplanner.view;

import at.jp.tourplanner.viewmodel.NewTourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

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

    public NewTourView(NewTourViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tourName.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDescription.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        tourStart.textProperty().bindBidirectional(viewModel.tourStartProperty());
        tourDestination.textProperty().bindBidirectional(viewModel.tourDestinationProperty());
    }

    public void onAddNewTour()
    {
        viewModel.addTour();
    }
}
