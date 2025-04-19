package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.service.TourService;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourSearchViewModel {
    private final TourService tourService;
    private final StringProperty searchText = new SimpleStringProperty("");
    private final ObjectProperty<String> selectedFilterType = new SimpleObjectProperty<>("TourName");

    public TourSearchViewModel(TourService tourService) {
        this.tourService = tourService;
        this.searchText.addListener(this::onTourSearchTextChanged);
    }
    public StringProperty searchTextProperty() {
        return searchText;
    }

    public ObjectProperty<String> selectedFilterTypeProperty() {
        return selectedFilterType;
    }

    private void onTourSearchTextChanged(Observable observable, String oldSearchText, String newSearchText) {
        FilterTerm filterTerm = new FilterTerm();
        filterTerm.setText(newSearchText);
        filterTerm.setType(selectedFilterType.getValue());
        System.out.println(filterTerm.getText());
        tourService.updateSelectedFilter(filterTerm);
    }
}
