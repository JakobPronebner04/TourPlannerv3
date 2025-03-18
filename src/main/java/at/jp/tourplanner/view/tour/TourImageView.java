package at.jp.tourplanner.view.tour;

import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.viewmodel.tour.TourImageViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourImageView implements Initializable {
    private final TourImageViewModel viewModel;

    @FXML
    private ImageView tourImageView;

    public TourImageView(TourImageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.tourImageView.imageProperty().bind(viewModel.tourImageProperty());
    }
}
