package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
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

public class OpenRouteServiceApi implements GeoRouting {

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
            throw new RuntimeException("Could not find coordinates for " + text);
        }
    }
    @Override
    public Optional<RouteInfo> findRoute(Geocode geocodeStart, Geocode geocodeEnd, String transportType)
    {
        String profile = TransportProfile.fromDisplayName(transportType).getApiProfile();
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
            GeocodeDirectionsSearchResponse geocodeDirectionsSearchResponse = objectMapper.readValue(
                    response.body(), GeocodeDirectionsSearchResponse.class
            );

            if(geocodeDirectionsSearchResponse.getFeatures().isEmpty()) {
                return Optional.empty();
            }

            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setDistance(geocodeDirectionsSearchResponse.getFeatures().getFirst().getProperties().getSummary().getDistance());
            routeInfo.setDuration(geocodeDirectionsSearchResponse.getFeatures().getFirst().getProperties().getSummary().getDuration());
            routeInfo.setJsonRoute(objectMapper.writeValueAsString(geocodeDirectionsSearchResponse));
            return Optional.of(routeInfo);
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve route because distance exceeded");
        }
    }
    @Override
    public List<Geocode> getRouteCoordinatesFromJson(String jsonRoute)
    {
        List<Geocode> route = new ArrayList<>();
        try{
            GeocodeDirectionsSearchResponse gdsr = objectMapper.readValue(jsonRoute,GeocodeDirectionsSearchResponse.class);
            gdsr.getFeatures().getFirst().getGeometry().getCoordinates()
                    .forEach(coord -> {
                        Geocode geocode = new Geocode();
                        geocode.setLatitude(coord[1]);
                        geocode.setLongitude(coord[0]);
                        route.add(geocode);
                    });
            return route;
        }catch(Exception e)
        {
            throw new RuntimeException("Could not map Route to Geocodes");
        }
    }
}