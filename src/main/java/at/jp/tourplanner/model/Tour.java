package at.jp.tourplanner.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty tourName;

    private final StringProperty tourDescription;

    private final StringProperty tourStart;

    private final StringProperty tourDestination;

    public Tour() {
        this.tourName = new SimpleStringProperty("");
        this.tourDescription = new SimpleStringProperty("");
        this.tourStart = new SimpleStringProperty("");
        this.tourDestination = new SimpleStringProperty("");
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
