package at.jp.tourplanner.model;

public class TourLog {
    private String comment;
    private int rating;

    public TourLog() {
        this.comment = "";
        this.rating = 0;
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
}
