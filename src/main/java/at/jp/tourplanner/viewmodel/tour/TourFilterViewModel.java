package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.service.TourService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourFilterViewModel {
    private final TourService tourService;
    private final StringProperty filterText = new SimpleStringProperty("");
    private final ObjectProperty<String> selectedFilterType = new SimpleObjectProperty<>("");

    public TourFilterViewModel(TourService tourService) {
        this.tourService = tourService;
    }
    public StringProperty filterTextProperty() {
        return filterText;
    }

    public ObjectProperty<String> selectedFilterTypeProperty() {
        return selectedFilterType;
    }

    public void filterTour() {
        FilterTerm filterTerm = new FilterTerm(filterText.getValue(), selectedFilterType.getValue());
        tourService.updateSelectedFilter(filterTerm);
    }
    public void showAll()
    {
        filterText.set("");
        tourService.updateSelectedFilter(new FilterTerm(filterText.getValue(),selectedFilterType.getValue()));
    }
}