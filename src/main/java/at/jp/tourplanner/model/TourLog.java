package at.jp.tourplanner.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TourLog {
    private String comment;
    private int rating;
    private float actualTime;
    private float actualDistance;
    private String dateTimeStr;
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
    public void setDateTimeStr(LocalDateTime dateTimeStr) {
        this.dateTime = dateTimeStr;
        this.dateTimeStr = dateTimeStr.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public String getDateTimeStr()
    {
        return this.dateTimeStr;
    }
    public LocalDateTime getDateTime()
    {
        return this.dateTime;
    }
}
