package at.jp.tourplanner.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLogEntity> tourLogs = new ArrayList<>();

    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private GeocodeDirectionsEntity geocodeDirections;

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

    public void setTourLogs(List<TourLogEntity> tourLogs) {
        this.tourLogs = tourLogs;
    }
    public List<TourLogEntity> getTourLogs() {
        return tourLogs;
    }

    public void addTourLog(TourLogEntity log) {
        tourLogs.add(log);
        log.setTour(this);
    }
    public GeocodeDirectionsEntity getGeocodeDirections() {
        return geocodeDirections;
    }

    public void setGeocodeDirections(GeocodeDirectionsEntity geocodeDirections) {
        this.geocodeDirections = geocodeDirections;
        geocodeDirections.setTour(this);
    }

   /* public Image getTourImage() {
        return tourImage;
    }

    public void setTourImage(Image tourImage) {
        this.tourImage = tourImage;
    }*/
}
