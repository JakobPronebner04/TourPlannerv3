package at.jp.tourplanner.service.openrouteservice;

public class GeocodeFeature {

    private String type;

    private GeometryPoint geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GeometryPoint getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryPoint geometry) {
        this.geometry = geometry;
    }
}
