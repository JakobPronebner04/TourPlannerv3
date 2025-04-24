package at.jp.tourplanner.repository;

public enum TourLogFilterType {
    TourLogComment("comment"),
    TourLogRating("rating"),
    TourLogDifficulty("difficulty"),
    TourLogNeededTime("actualTime"),
    TourLogDistance("actualDistance");


    private final String fieldName;

    TourLogFilterType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static TourLogFilterType fromString(String text) {
        for (TourLogFilterType type : TourLogFilterType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannter Typ: " + text);
    }
}
