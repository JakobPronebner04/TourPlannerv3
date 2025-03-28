package at.jp.tourplanner;


import at.jp.tourplanner.da.StateDataAccess;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.repository.*;
import at.jp.tourplanner.service.TourLogService;
import at.jp.tourplanner.service.TourService;
import at.jp.tourplanner.view.tour.*;
import at.jp.tourplanner.view.tourlog.*;
import at.jp.tourplanner.viewmodel.tour.*;
import at.jp.tourplanner.viewmodel.tourlog.*;
import at.jp.tourplanner.window.WindowManager;

public class ViewFactory {

    private static ViewFactory instance;

    private final EventManager eventManager;

    private final TourService tourService;
    private final TourLogService tourLogService;

    private final WindowManager windowManager;

    private final StateDataAccess stateDataAccess;
    private final TourRepositoryORM tourRepository;
    private final TourLogRepositoryORM tourLogRepository;

    private ViewFactory() {
        this.eventManager = new EventManager();
        this.tourRepository = new TourRepositoryORM();
        this.tourLogRepository = new TourLogRepositoryORM();
        this.stateDataAccess = StateDataAccess.getInstance();

        this.tourService = new TourService(eventManager, stateDataAccess, tourRepository);
        this.tourLogService = new TourLogService(eventManager,tourLogRepository,tourRepository, stateDataAccess);
        this.windowManager = WindowManager.getInstance();
    }

    public static ViewFactory getInstance() {
        if (null == instance) {
            instance = new ViewFactory();
        }

        return instance;
    }

    public Object create(Class<?> viewClass) {

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
        if(TourLogMenuView.class == viewClass) {
            return new TourLogMenuView(new TourLogMenuViewModel(eventManager,tourLogService,windowManager));
        }

        if(TourLogHistoryView.class == viewClass) {
            return new TourLogHistoryView(new TourLogHistoryViewModel(eventManager,tourLogService,windowManager));
        }

        if(NewTourLogView.class == viewClass) {
            return new NewTourLogView(new NewTourLogViewModel(tourLogService,windowManager));
        }
        if(EditTourLogView.class == viewClass) {
            return new EditTourLogView(new EditTourLogViewModel(tourLogService,windowManager));
        }
        if(DetailedTourLogView.class == viewClass) {
            return new DetailedTourLogView(new DetailedTourLogViewModel(tourLogService,windowManager));
        }
        if(DetailedTourView.class == viewClass) {
            return new DetailedTourView(new DetailedTourViewModel(tourService,windowManager));
        }
        if(TourImageView.class == viewClass){
            return new TourImageView(new TourImageViewModel(tourService,eventManager));
        }

        throw new IllegalArgumentException(
                "Unknown view class: " + viewClass
        );
    }
}
