package at.jp.tourplanner;

import at.jp.tourplanner.utils.SupportedLocales;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TourPlannerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Locale userLocale = Locale.getDefault();
        List<Locale> supportedLocales = SupportedLocales.getSupportedLocales();
        if (supportedLocales.stream().noneMatch(locale -> locale.getLanguage().equals(Locale.getDefault().getLanguage()))) {
            userLocale = Locale.ENGLISH;
        }

        Parent mainView = FXMLDependencyInjector.load(
                "main-view.fxml",
                userLocale
        );

        Scene scene = new Scene(mainView);
        stage.setTitle("Tourplanner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
