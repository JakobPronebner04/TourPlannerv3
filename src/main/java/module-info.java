module at.jp.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens at.jp.tourplanner to javafx.fxml;
    opens at.jp.tourplanner.model;
    exports at.jp.tourplanner.entity;
    exports at.jp.tourplanner;
    exports at.jp.tourplanner.service;
    exports at.jp.tourplanner.event;
    exports at.jp.tourplanner.model;
    exports at.jp.tourplanner.viewmodel.tour;
    exports at.jp.tourplanner.view.tour;

    opens at.jp.tourplanner.view.tour to javafx.fxml;

    exports at.jp.tourplanner.viewmodel.tourlog;
    exports at.jp.tourplanner.view.tourlog;
    opens at.jp.tourplanner.view.tourlog to javafx.fxml;
    exports at.jp.tourplanner.repository;
    opens at.jp.tourplanner.repository to javafx.fxml;
    exports at.jp.tourplanner.utils;
    opens at.jp.tourplanner.entity to org.hibernate.orm.core;
    exports at.jp.tourplanner.da;
    opens at.jp.tourplanner.da to javafx.fxml;

}