package at.jp.tourplanner.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class GeocodeDirectionsEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String jsonDirections;

    @OneToOne
    @JoinColumn(name = "tour_id")
    private TourEntity tour;

    public UUID getId() {
        return id;
    }

    public String getJsonDirections() {
        return jsonDirections;
    }

    public void setJsonDirections(String jsonDirections) {
        this.jsonDirections = jsonDirections;
    }

    public TourEntity getTour() {
        return tour;
    }

    public void setTour(TourEntity tour) {
        this.tour = tour;
    }
}