package at.jp.tourplanner.service.openrouteservice;

public class GeometryPoint {

    private String type;

    private double[] coordinates;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}