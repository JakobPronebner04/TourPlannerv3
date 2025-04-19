package at.jp.tourplanner.dto;

public class RouteInfo {
    private float duration;
    private float distance;
    private String jsonRoute;

    public void setJsonRoute(String jsonRoute) {
        this.jsonRoute = jsonRoute;
    }
    public String getJsonRoute() {
        return jsonRoute;
    }
    public void setDistance(float distance) {
        this.distance = distance;
    }
    public float getDistance() {
        return distance;
    }
    public void setDuration(float duration) {
        this.duration = duration;
    }
    public float getDuration() {
        return duration;
    }

}
