package at.jp.tourplanner.service.openrouteservice;

import java.util.List;

public class GeocodeMapSearchResponse {
    private List<Feature> features;

    public List<Feature> getFeature() {
        return features;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
