module at.jp.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;


    opens at.jp.tourplanner to javafx.fxml;
    opens at.jp.tourplanner.view to javafx.fxml;

    exports at.jp.tourplanner;
    exports at.jp.tourplanner.view;
    exports at.jp.tourplanner.viewmodel;
}