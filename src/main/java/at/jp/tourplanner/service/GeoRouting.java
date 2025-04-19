package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;

import java.util.List;
import java.util.Optional;

public interface GeoRouting {
    Optional<Geocode> findGeocode(String text);
    Optional<RouteInfo> findRoute(Geocode geocodeStart, Geocode geocodeEnd, String transportType);
    List<Geocode> getRouteCoordinatesFromJson(String jsonRoute);
}
