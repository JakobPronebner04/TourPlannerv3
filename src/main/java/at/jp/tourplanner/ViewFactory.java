package at.jp.tourplanner;


import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.view.*;
import at.jp.tourplanner.viewmodel.EditTourViewModel;
import at.jp.tourplanner.viewmodel.TourHistoryViewModel;
import at.jp.tourplanner.viewmodel.TourMenuViewModel;
import at.jp.tourplanner.viewmodel.NewTourViewModel;
import at.jp.tourplanner.window.WindowManager;

public class ViewFactory {

    private static ViewFactory instance;

    private final EventManager eventManager;

    private final TourService tourService;

    private final WindowManager windowManager;

    private ViewFactory() {
        this.eventManager = new EventManager();
        this.tourService = new TourService(eventManager);
        this.windowManager = WindowManager.getInstance();
    }

    public static ViewFactory getInstance() {
        if (null == instance) {
            instance = new ViewFactory();
        }

        return instance;
    }

    public Object create(Class<?> viewClass) {
        if (MainView.class == viewClass) {
            return new MainView();
        }

        if (TourMenuView.class == viewClass) {
            return new TourMenuView(new TourMenuViewModel(eventManager,tourService,windowManager));
        }

        if(NewTourView.class == viewClass) {
            return new NewTourView(new NewTourViewModel(tourService,windowManager));
        }
        if(TourHistoryView.class == viewClass) {
            return new TourHistoryView(new TourHistoryViewModel(eventManager,tourService));
        }
        if(EditTourView.class == viewClass) {
            return new EditTourView(new EditTourViewModel(tourService,windowManager));
        }

        throw new IllegalArgumentException(
                "Unknown view class: " + viewClass
        );
    }
}
