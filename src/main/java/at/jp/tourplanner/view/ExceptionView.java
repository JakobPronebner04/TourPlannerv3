package at.jp.tourplanner.view;

import at.jp.tourplanner.viewmodel.ExceptionViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ExceptionView implements Initializable {
    private final ExceptionViewModel exceptionViewModel;
    @FXML
    private Text exceptionText;
    public ExceptionView(ExceptionViewModel exceptionViewModel) {
        this.exceptionViewModel = exceptionViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exceptionText.textProperty().bind(exceptionViewModel.getMessageProperty());
    }
}
