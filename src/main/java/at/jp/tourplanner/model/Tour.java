package at.jp.tourplanner.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.*;

public class Tour {
    private final StringProperty tourName;

    private final StringProperty tourDescription;

    private final StringProperty tourStart;

    private final StringProperty tourDestination;
    private final Image tourImage;

    public Tour() {
        this.tourName = new SimpleStringProperty("");
        this.tourDescription = new SimpleStringProperty("");
        this.tourStart = new SimpleStringProperty("");
        this.tourDestination = new SimpleStringProperty("");
        this.tourImage = null;
    }

    public StringProperty tourNameProperty()
    {
        return this.tourName;
    }
    public StringProperty tourDescriptionProperty()
    {
        return this.tourDescription;
    }
    public StringProperty tourStartProperty()
    {
        return this.tourStart;
    }
    public StringProperty tourDestinationProperty()
    {
        return this.tourDestination;
    }
}
