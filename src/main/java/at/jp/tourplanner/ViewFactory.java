package at.jp.tourplanner;


import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.repository.*;
import at.jp.tourplanner.service.*;
import at.jp.tourplanner.view.ExceptionView;
import at.jp.tourplanner.view.tour.*;
import at.jp.tourplanner.view.tourlog.*;
import at.jp.tourplanner.viewmodel.ExceptionViewModel;
import at.jp.tourplanner.viewmodel.tour.*;
import at.jp.tourplanner.viewmodel.tourlog.*;
import at.jp.tourplanner.window.WindowManager;

public class ViewFactory {

    private static ViewFactory instance;

    private final EventManager eventManager;

    private final TourService tourService;
    private final ExceptionService exceptionService;
    private final TourLogService tourLogService;
    private final MapRendererService mapRendererService;
    private final ExportService exportService;
    private final ImportService importService;
    private final WindowManager windowManager;
    private final ConfigManager configManager;

    private final StateDataAccess stateDataAccess;
    private final TourRepositoryORM tourRepository;
    private final TourLogRepositoryORM tourLogRepository;
    private final OpenRouteServiceApi openRouteServiceApi;

    private ViewFactory() {
        this.configManager = new ConfigManager();
        this.eventManager = new EventManager();
        this.tourRepository = new TourRepositoryORM();
        this.tourLogRepository = new TourLogRepositoryORM();
        this.stateDataAccess = new StateDataAccess();
        this.openRouteServiceApi = new OpenRouteServiceApi(configManager);

        this.tourService = new TourService(openRouteServiceApi,eventManager, stateDataAccess, tourRepository);
        this.tourLogService = new TourLogService(eventManager,tourLogRepository,tourRepository, stateDataAccess);
        this.mapRendererService = new MapRendererService(tourRepository,stateDataAccess);
        this.exportService = new ExportService(tourRepository,stateDataAccess);
        this.importService = new ImportService(tourService,tourLogService,tourRepository,openRouteServiceApi);
        this.exceptionService = new ExceptionService(stateDataAccess,eventManager);
        this.windowManager = new WindowManager(eventManager);
    }

    public static ViewFactory getInstance() {
        if (null == instance) {
            instance = new ViewFactory();
        }

        return instance;
    }

    public Object create(Class<?> viewClass) {

        if (TourMenuView.class == viewClass) {
            return new TourMenuView(new TourMenuViewModel(eventManager,exceptionService,tourService,importService,exportService,windowManager));
        }
        if(NewTourView.class == viewClass) {
            return new NewTourView(new NewTourViewModel(tourService,exceptionService,windowManager));
        }
        if(TourHistoryView.class == viewClass) {
            return new TourHistoryView(new TourHistoryViewModel(eventManager,tourService));
        }
        if(EditTourView.class == viewClass) {
            return new EditTourView(new EditTourViewModel(tourService,exceptionService,windowManager));
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
            return new DetailedTourView(new DetailedTourViewModel(tourService,exceptionService,mapRendererService,windowManager,eventManager));
        }
        if(TourMapView.class == viewClass) {
            return new TourMapView(new TourMapViewModel(eventManager,exceptionService,tourService,mapRendererService,exportService));
        }
        if(TourFilterView.class == viewClass) {
            return new TourFilterView(new TourFilterViewModel(tourService));
        }
        if(TourLogFilterView.class == viewClass) {
            return new TourLogFilterView(new TourLogFilterViewModel(tourLogService,eventManager));
        }
        if(ExceptionView.class == viewClass) {
            return new ExceptionView(new ExceptionViewModel(exceptionService));
        }

        throw new IllegalArgumentException(
                "Unknown view class: " + viewClass
        );
    }
}
