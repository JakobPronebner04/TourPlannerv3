package at.jp.tourplanner.service.openrouteservice;

import java.util.List;

public class GeocodeDirectionsSearchResponse {
    private List<DirectionsFeature> features;

    public List<DirectionsFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<DirectionsFeature> features) {
        this.features = features;
    }
}
