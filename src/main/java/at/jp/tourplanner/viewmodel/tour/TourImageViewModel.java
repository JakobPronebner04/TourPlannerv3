package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.service.TourService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class TourImageViewModel {
    private final TourService tourService;
    private final EventManager eventManager;
    private final ObjectProperty<Image> imageProperty;
    public TourImageViewModel(TourService tourService, EventManager eventManager) {
        this.tourService = tourService;
        this.eventManager = eventManager;
        imageProperty = new SimpleObjectProperty<>(tourService.getPlaceHolderImage());
        this.eventManager.subscribe(
                Events.TOUR_SELECTED, this::onToursChanged
        );
    }

    private void onToursChanged(Boolean isDisabled)
    {
        if(isDisabled)
        {
            imageProperty.setValue(tourService.getPlaceHolderImage());
            return;
        }
        imageProperty.setValue(tourService.getSelectedTour().getTourImage());
    }

    public ObjectProperty<Image> tourImageProperty()
    {
        return imageProperty;
    }
}
