package at.jp.tourplanner.model;

import java.awt.*;

public class Tour {
    private String tourName;
    private String tourDescription;
    private String tourStart;
    private String tourDestination;
    private String tourTransportType;
    private Image tourImage;

    public Tour() {
        this.tourName = "";
        this.tourDescription = "";
        this.tourStart = "";
        this.tourDestination = "";
        this.tourTransportType = "Car";
        this.tourImage = null;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getTourStart() {
        return tourStart;
    }

    public void setTourStart(String tourStart) {
        this.tourStart = tourStart;
    }

    public String getTourDestination() {
        return tourDestination;
    }

    public void setTourDestination(String tourDestination) {
        this.tourDestination = tourDestination;
    }

    public String getTourTransportType() {
        return tourTransportType;
    }
    public void setTourTransportType(String tourTransportType) {
        this.tourTransportType = tourTransportType;
    }

    public Image getTourImage() {
        return tourImage;
    }

    public void setTourImage(Image tourImage) {
        this.tourImage = tourImage;
    }
}
