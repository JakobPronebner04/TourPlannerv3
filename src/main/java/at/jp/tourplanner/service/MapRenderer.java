package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;

import java.util.List;

public interface MapRenderer {
    String getDrawScript();
    String getClearScript();
    String getInitialState();
}
