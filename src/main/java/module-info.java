module at.jp.tourplanner {
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jakarta.validation;
    requires jdk.jshell;
    requires javafx.web;
    requires java.net.http;
    requires javafx.swing;
    requires org.apache.pdfbox;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.databind;
    requires net.bytebuddy;

    opens at.jp.tourplanner to javafx.fxml;
    opens at.jp.tourplanner.inputmodel;
    exports at.jp.tourplanner.entity;
    exports at.jp.tourplanner;
    exports at.jp.tourplanner.service;
    exports at.jp.tourplanner.event;
    exports at.jp.tourplanner.inputmodel;
    exports at.jp.tourplanner.viewmodel.tour;
    exports at.jp.tourplanner.view.tour;
    exports at.jp.tourplanner.dto;
    exports at.jp.tourplanner.service.openrouteservice;

    opens at.jp.tourplanner.view.tour to javafx.fxml;

    exports at.jp.tourplanner.viewmodel.tourlog;
    exports at.jp.tourplanner.view.tourlog;
    opens at.jp.tourplanner.view.tourlog to javafx.fxml;
    exports at.jp.tourplanner.repository;
    opens at.jp.tourplanner.repository to javafx.fxml;
    exports at.jp.tourplanner.utils;
    opens at.jp.tourplanner.entity to org.hibernate.orm.core;
    exports at.jp.tourplanner.dataaccess;
    opens at.jp.tourplanner.dataaccess to javafx.fxml;
    exports at.jp.tourplanner.service.importexport;
    opens at.jp.tourplanner.view to javafx.fxml;

}