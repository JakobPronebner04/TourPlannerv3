package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.service.openrouteservice.GeocodeMapSearchResponse;
import at.jp.tourplanner.service.openrouteservice.GeocodeSearchResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.spi.ExecutionControl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OpenRouteServiceApi implements MapService {

    private final static String API_KEY =
            "5b3ce3597851110001cf6248945838c073a6485aa146a620e558f97e";

    private final static String GEOCODE_SEARCH_URI =
            "https://api.openrouteservice.org/geocode/search?api_key=%s&text=%s";
    private final static String MAP_POINTS_SEARCH_URI =
            "https://api.openrouteservice.org/v2/directions/%s/geojson";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public OpenRouteServiceApi() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Optional<Geocode> findGeocode(String text) {
        String uri = String.format(GEOCODE_SEARCH_URI, API_KEY, text);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            GeocodeSearchResponse geocodeSearchResponse = objectMapper.readValue(
                    response.body(), GeocodeSearchResponse.class
            );

            if (geocodeSearchResponse.getFeatures().isEmpty()) {
                return Optional.empty();
            }

            Geocode geocode = new Geocode();
            geocode.setText(text);
            geocode.setLongitude(geocodeSearchResponse.getFeatures().getFirst().getGeometry().getCoordinates()[0]);
            geocode.setLatitude(geocodeSearchResponse.getFeatures().getFirst().getGeometry().getCoordinates()[1]);

            return Optional.of(geocode);
        } catch (Exception e) {
            throw new RuntimeException("Could not find Geocode for text: " + text, e);
        }
    }
    @Override
    public String findRouteAsJson(Geocode geocodeStart, Geocode geocodeEnd)
    {
        String profile = "driving-car";
        String uri = String.format(MAP_POINTS_SEARCH_URI, profile);

        try {
            String requestBody = objectMapper.writeValueAsString(
                    Map.of("coordinates", List.of(
                            List.of(geocodeStart.getLongitude(), geocodeStart.getLatitude()),
                            List.of(geocodeEnd.getLongitude(), geocodeEnd.getLatitude())
                    ))
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Authorization", API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );
            System.out.println(response.body());
            GeocodeMapSearchResponse geocodeMapSearchResponse = objectMapper.readValue(
                    response.body(), GeocodeMapSearchResponse.class
            );
            System.out.println(geocodeMapSearchResponse.getFeatures().getFirst().getGeometry().getCoordinates()[0]);
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve route" + e, e);
        }
    }
}