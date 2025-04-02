package at.jp.tourplanner.inputmodel;

import jakarta.validation.constraints.NotBlank;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tour {
    @NotBlank(message = "tourName should not be blanked!")
    private String tourName;
    @NotBlank(message = "tourDescription should not be blanked!")
    private String tourDescription;
    @NotBlank(message = "tourStart should not be blanked!")
    private String tourStart;
    @NotBlank(message = "tourDestination should not be blanked!")
    private String tourDestination;

    private String tourTransportType;
    private Image tourImage;

    public Tour() {
        this.tourName = "";
        this.tourDescription = "";
        this.tourStart = "";
        this.tourDestination = "";
        this.tourTransportType = "";
        this.tourImage = new Image(getClass().getResource("/at/jp/tourplanner/images/map.png").toExternalForm());
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
