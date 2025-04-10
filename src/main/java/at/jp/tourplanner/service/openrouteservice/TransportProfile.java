package at.jp.tourplanner.service.openrouteservice;

import java.util.Arrays;

public enum TransportProfile {
    CAR("Car", "driving-car"),
    TRUCK("Truck (Heavy Vehicle)", "driving-hgv"),
    BICYCLE("Bicycle", "cycling-regular"),
    BICYCLE_ROAD("Bicycle (Road)", "cycling-road"),
    BICYCLE_MOUNTAIN("Bicycle (Mountain)", "cycling-mountain"),
    BICYCLE_ELECTRIC("Bicycle (E-Bike)", "cycling-electric"),
    WALKING("Walking", "foot-walking"),
    HIKING("Hiking", "foot-hiking"),
    WHEELCHAIR("Wheelchair Accessible", "wheelchair");

    private final String displayName;
    private final String apiProfile;

    TransportProfile(String displayName, String apiProfile) {
        this.displayName = displayName;
        this.apiProfile = apiProfile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiProfile() {
        return apiProfile;
    }

    public static TransportProfile fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(p -> p.displayName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown transporttype: " + name));
    }
}

