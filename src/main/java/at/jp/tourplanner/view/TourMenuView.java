package at.jp.tourplanner.view;

import at.jp.tourplanner.viewmodel.TourMenuViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.rmi.NotBoundException;
import java.util.ResourceBundle;

public class TourMenuView implements Initializable {

    private final TourMenuViewModel viewModel;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    /*@FXML
    private Button removeButton;*/

    public TourMenuView(TourMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.disableProperty().bind(viewModel.editDisabledProperty());
    }

    @FXML
    public void onAddTourClicked(){
        viewModel.openNewTourWindow();
    }

    @FXML
    public void onEditTourClicked(){

    }
}
