package at.jp.tourplanner.service.openrouteservice;

public class DirectionsFeature {

    private String type;
    private GeometryLine geometry;
    private DirectionsProperties properties;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeometryLine getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryLine geometry) {
        this.geometry = geometry;
    }

    public DirectionsProperties getProperties() {
        return properties;
    }
    public void setProperties(DirectionsProperties properties) {
        this.properties = properties;
    }
}
