package at.jp.tourplanner.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

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
    private int popularity;
    private int childFriendliness;
    private float tourDistance;
    private float tourDuration;

    @Formula("floor(tourduration / 3600) || 'h ' || " +
             "floor(((tourduration)::integer % 3600) / 60) || 'min'"
    )
    private String formattedDuration;

    @Formula("floor(tourdistance / 1000) || ' km'")
    private String formattedDistance;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    private List<TourLogEntity> tourLogs = new ArrayList<>();

    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    private GeocodeDirectionsEntity geocodeDirections;

    public TourEntity() {
        this.tourName = "";
        this.tourDescription = "";
        this.tourStart = "";
        this.tourDestination = "";
        this.tourTransportType = "";
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return tourName;
    }
    public String getFormattedDuration()  { return formattedDuration; }
    public String getFormattedDistance()  { return formattedDistance; }

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
    public int getPopularity() {
        return popularity;
    }
    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    public int getChildFriendliness() {
        return childFriendliness;
    }
    public void setChildFriendliness(int childFriendliness) {
        this.childFriendliness = childFriendliness;
    }
    public float getDistance() {
        return tourDistance;
    }
    public void setDistance(float tourDistance) {
        this.tourDistance = tourDistance;
    }
    public float getDuration() {
        return tourDuration;
    }
    public void setDuration(float tourDuration) {
        this.tourDuration = tourDuration;
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
}
