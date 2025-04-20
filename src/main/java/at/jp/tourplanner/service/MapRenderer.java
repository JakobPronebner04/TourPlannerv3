package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;

import java.util.List;

public interface MapRenderer {
    String getDrawScript(List<Geocode> coords);
    String getClearScript();
    String getInitialState();
}
