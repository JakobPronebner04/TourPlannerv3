package at.jp.tourplanner.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
public class TourLogEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String comment;
    private int rating;
    private float actualTime;
    private float actualDistance;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.dateTime = LocalDateTime.now();
    }

    public TourLogEntity() {
        this.comment = "";
        this.rating = 0;
        this.actualTime = 0;
        this.actualDistance = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public float getActualTime() {
        return actualTime;
    }

    public void setActualTime(float actualTime) {
        this.actualTime = actualTime;
    }

    public float getActualDistance() {
        return actualDistance;
    }

    public void setActualDistance(float actualDistance) {
        this.actualDistance = actualDistance;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TourEntity getTour() {
        return tour;
    }

    public void setTour(TourEntity tour) {
        this.tour = tour;
    }
}
