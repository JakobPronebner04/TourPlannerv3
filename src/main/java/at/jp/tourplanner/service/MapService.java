package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.Geocode;

import java.util.List;
import java.util.Optional;

public interface MapService {
    Optional<Geocode> findGeocode(String text);
    List<Geocode> findRoute(Geocode geocodeStart, Geocode geocodeEnd);
}
