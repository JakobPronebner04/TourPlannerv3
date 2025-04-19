package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;

import java.util.List;

public interface MapRenderer {
    void draw(List<Geocode> coords);
    void clear();
    void loadInitial();
}
