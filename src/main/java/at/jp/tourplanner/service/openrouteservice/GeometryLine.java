package at.jp.tourplanner.service.openrouteservice;

import java.util.List;

public class GeometryLine {

    private String type;

    private List<double[]> coordinates;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<double[]> coordinates) {
        this.coordinates = coordinates;
    }
}