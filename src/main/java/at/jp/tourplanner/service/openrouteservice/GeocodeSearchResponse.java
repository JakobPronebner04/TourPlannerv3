package at.jp.tourplanner.service.openrouteservice;

import java.util.List;

public class GeocodeSearchResponse {

    private String type;

    private List<GeocodeFeature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GeocodeFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<GeocodeFeature> features) {
        this.features = features;
    }
}