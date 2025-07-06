package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.viewmodel.tour.TourMenuViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.net.URL;
import java.util.ResourceBundle;

public class TourMenuView implements Initializable {

    private final TourMenuViewModel viewModel;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button detailsButton;
    @FXML
    private Button exportAsPDFButton;

    @FXML
    private Button exportTourButton;
    @FXML
    private Button exportTourSummaryButton;
    @FXML
    private Button importTourButton;

    public TourMenuView(TourMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editButton.disableProperty().bind(viewModel.editDisabledProperty());
        removeButton.disableProperty().bind(viewModel.removeDisabledProperty());
        detailsButton.disableProperty().bind(viewModel.detailsDisabledProperty());
        exportAsPDFButton.disableProperty().bind(viewModel.exportDisabledProperty());
        exportTourButton.disableProperty().bind(viewModel.exportDisabledProperty());
        exportTourSummaryButton.disableProperty().bind(viewModel.exportSummaryDisabledProperty());
    }
    public void onAddTourClicked(){
        viewModel.openNewTourWindow();
    }

    public void onEditTourClicked(){
        viewModel.openEditTourWindow();
    }

    public void onRemoveTourClicked(){
        viewModel.deleteTour();
    }

    public void onTourDetails() {
        viewModel.openDetailsTourWindow();
    }

    public void onMapExport() {
        viewModel.exportTourAsPDF();
    }
    public void onExportTour()
    {
        viewModel.exportTourAsJson();
    }
    public void onExportSummary()
    {
        viewModel.exportTourSummary();
    }
    public void onImportTour() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("JSON Datei auswählen");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Dateien (*.json)", "*.json")
        );

        var selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());
        if (selectedFile != null) {
            viewModel.fileChosen(selectedFile.getAbsolutePath());
        }
    }
}
