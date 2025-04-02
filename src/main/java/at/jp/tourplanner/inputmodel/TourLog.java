package at.jp.tourplanner.inputmodel;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TourLog {
    @NotBlank(message = "Comment should not be blanked!")
    private String comment;

    @Min(value = 0, message = "Minimum allowed value of rating 0!")
    @Max(value = 5, message = "Maximum allowed value of rating 5!")
    private int rating;

    @Positive(message = "Time must be greater than 0!")
    private float actualTime;

    @Positive(message = "Distance must be greater than 0!")
    private float actualDistance;

    private LocalDateTime dateTime;


    public TourLog() {
        this.comment = "";
        this.rating = 0;
        this.actualTime = 0;
        this.actualDistance = 0;
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

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeStr()
    {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public LocalDateTime getDateTime()
    {
        return this.dateTime;
    }
}
