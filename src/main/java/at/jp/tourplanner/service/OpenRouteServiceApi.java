package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.exception.GeocodingException;
import at.jp.tourplanner.exception.RoutingException;
import at.jp.tourplanner.service.openrouteservice.GeocodeDirectionsSearchResponse;
import at.jp.tourplanner.service.openrouteservice.GeocodeSearchResponse;
import at.jp.tourplanner.service.openrouteservice.TransportProfile;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OpenRouteServiceApi implements GeoRouting {

    private final ConfigManager configManager;
    private final static String GEOCODE_SEARCH_URI =
            "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s";
    private final static String ROUTE_SEARCH_URI =
            "https://api.openrouteservice.org/v2/directions/%s/geojson";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public OpenRouteServiceApi(ConfigManager configManager) {
        this.client = HttpClient.newHttpClient();
        this.configManager = configManager;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Optional<Geocode> findGeocode(String text) {
        String uri = String.format(GEOCODE_SEARCH_URI, configManager.get("api.key"), text);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            GeocodeSearchResponse result = objectMapper.readValue(response.body(), GeocodeSearchResponse.class);

            if (result.getFeatures().isEmpty()) return Optional.empty();

            Geocode geocode = new Geocode();
            geocode.setText(text);
            geocode.setLongitude(result.getFeatures().getFirst().getGeometry().getCoordinates()[0]);
            geocode.setLatitude(result.getFeatures().getFirst().getGeometry().getCoordinates()[1]);

            return Optional.of(geocode);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<RouteInfo> findRoute(Geocode geocodeStart, Geocode geocodeEnd, String transportType) {
        String profile = TransportProfile.fromDisplayName(transportType).getApiProfile();
        String uri = String.format(ROUTE_SEARCH_URI, profile);

        try {
            String requestBody = objectMapper.writeValueAsString(
                    Map.of("coordinates", List.of(
                            List.of(geocodeStart.getLongitude(), geocodeStart.getLatitude()),
                            List.of(geocodeEnd.getLongitude(), geocodeEnd.getLatitude())
                    ))
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Authorization", this.configManager.get("api.key"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            GeocodeDirectionsSearchResponse rsp =
                    objectMapper.readValue(response.body(), GeocodeDirectionsSearchResponse.class);

            if (rsp.getFeatures().isEmpty()) {
                return Optional.empty();
            }

            List<double[]> rawCoords = rsp.getFeatures().getFirst().getGeometry().getCoordinates();
            List<double[]> swappedCoords = rawCoords.stream()
                    .map(pt -> new double[]{ pt[1], pt[0] })
                    .collect(Collectors.toList());

            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setDistance(rsp.getFeatures().getFirst().getProperties().getSummary().getDistance());
            routeInfo.setDuration(rsp.getFeatures().getFirst().getProperties().getSummary().getDuration());
            routeInfo.setJsonRoute(objectMapper.writeValueAsString(swappedCoords));

            return Optional.of(routeInfo);

        } catch (Exception e) {
            return Optional.empty();
        }
    }


}