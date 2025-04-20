package at.jp.tourplanner.repository;

public enum TourFilterType {
    TourName("tourName"),
    TourDescription("tourDescription"),
    TourDepartureLocation("tourStart"),
    TourDestinationLocation("tourDestination"),
    TourTransportType("tourTransportType"),
    TourDistance("formattedDistance"),
    TourDuration("formattedDuration");

    private final String fieldName;

    TourFilterType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static TourFilterType fromString(String text) {
        for (TourFilterType type : TourFilterType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannter Typ: " + text);
    }
}
