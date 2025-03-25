package at.jp.tourplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class TourEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String tourName;
    private String tourDescription;
    private String tourStart;
    private String tourDestination;
    private String tourTransportType;
    //private Image tourImage;

    public TourEntity() {
        this.tourName = "";
        this.tourDescription = "";
        this.tourStart = "";
        this.tourDestination = "";
        this.tourTransportType = "";
        //this.tourImage = new Image(getClass().getResource("/at/jp/tourplanner/images/map.png").toExternalForm());
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return tourName;
    }

    public void setName(String tourName) {
        this.tourName = tourName;
    }

    public String getDescription() {
        return tourDescription;
    }

    public void setDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public String getStart() {
        return tourStart;
    }

    public void setStart(String tourStart) {
        this.tourStart = tourStart;
    }

    public String getDestination() {
        return tourDestination;
    }

    public void setDestination(String tourDestination) {
        this.tourDestination = tourDestination;
    }

    public String getTransportType() {
        return tourTransportType;
    }
    public void setTransportType(String tourTransportType) {
        this.tourTransportType = tourTransportType;
    }

   /* public Image getTourImage() {
        return tourImage;
    }

    public void setTourImage(Image tourImage) {
        this.tourImage = tourImage;
    }*/
}
