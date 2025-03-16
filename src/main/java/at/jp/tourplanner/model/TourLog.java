package at.jp.tourplanner.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourLog {
    private final StringProperty comment;

    private final IntegerProperty rating;

    public TourLog() {
        this.comment = new SimpleStringProperty("");
        this.rating = new SimpleIntegerProperty(0);
    }

    public StringProperty tourLogCommentProperty()
    {
        return this.comment;
    }
    public IntegerProperty tourLogRatingProperty()
    {
        return this.rating;
    }
}
