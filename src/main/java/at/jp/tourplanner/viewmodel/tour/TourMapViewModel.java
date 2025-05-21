package at.jp.tourplanner.viewmodel.tour;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.exception.ErrorHandlingMode;
import at.jp.tourplanner.exception.ExceptionHandler;
import at.jp.tourplanner.provider.SnapshotProvider;
import at.jp.tourplanner.service.ExceptionService;
import at.jp.tourplanner.service.ExportService;
import at.jp.tourplanner.service.MapRendererService;
import at.jp.tourplanner.service.TourService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.web.WebEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;

public class TourMapViewModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ExceptionService exceptionService;
    private final TourService tourService;
    private final EventManager eventManager;
    private final MapRendererService mapRendererService;
    private final ExportService exportService;
    private WebEngine webEngine;
    private SnapshotProvider snapshotProvider;

    public TourMapViewModel(EventManager eventManager, ExceptionService exceptionService, TourService tourService, MapRendererService mapRendererService, ExportService exportService) {
        this.exceptionService = exceptionService;
        this.eventManager = eventManager;
        this.mapRendererService = mapRendererService;
        this.tourService = tourService;
        this.exportService = exportService;
        this.eventManager.subscribe(Events.TOUR_SELECTED, this::onTourSelected);
        this.eventManager.subscribe(Events.TOUR_EXPORT,this::onTourExport);
    }

    private void onTourSelected(Boolean deselected) {
        webEngine.executeScript(mapRendererService.getClearScript());
        if(!deselected) {
            webEngine.executeScript(mapRendererService.getDrawScript());
        }
    }
    public void onTourExport(String info) {
        snapshotProvider.requestSnapshot(writableImage -> {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try{
                exportService.exportSingleTourAsPDF(bufferedImage);
                LOGGER.info("Exported tour as pdf");
            }catch(Exception e){
                LOGGER.error(e);
                exceptionService.updateCurrentExceptionMessage(e);
            }
        });
    }
    public void initMap(WebEngine webEngine) {
        this.webEngine = webEngine;
        webEngine.load(mapRendererService.getInitialState());
    }
    public void setSnapshotProvider(SnapshotProvider snapshotProvider) {
        this.snapshotProvider = snapshotProvider;
    }
}
