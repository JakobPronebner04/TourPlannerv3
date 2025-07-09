package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.exception.*;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.repository.TourRepositoryORM;
import at.jp.tourplanner.service.importexport.TourTransferModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportServiceTest {

    @Mock
    private TourRepositoryORM tourRepository;
    @Mock
    private OpenRouteServiceApi openRouteServiceApi;
    @Mock
    private TourService tourService;
    @Mock
    private TourLogService tourLogService;

    @InjectMocks
    private ImportService importService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Tour makeSampleTour() {
        Tour t = new Tour();
        t.setTourName("MyTour");
        t.setTourDescription("Desc");
        t.setTourStart("Start");
        t.setTourDestination("End");
        t.setTourTransportType("car");
        return t;
    }

    private TourLog makeSampleLog() {
        TourLog log = new TourLog();
        log.setComment("Nice");
        log.setRating(3);
        log.setDifficulty(2);
        log.setActualDistance(10f);
        log.setActualTime(1.5f);
        return log;
    }

    @TempDir
    File tempDir;

    private File writeTransferModelToFile(TourTransferModel model) throws Exception {
        File file = new File(tempDir, "import.json");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(mapper.writeValueAsString(model));
        }
        return file;
    }

    @Test
    void importSingleTour_success_noLogs() throws Exception {
        TourTransferModel tm = new TourTransferModel(makeSampleTour(), Collections.emptyList());
        File f = writeTransferModelToFile(tm);

        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty());

        Geocode geo = mock(Geocode.class);
        when(openRouteServiceApi.findGeocode(eq("Start"))).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findGeocode(eq("End"))).thenReturn(Optional.of(geo));

        RouteInfo ri = mock(RouteInfo.class);
        when(ri.getJsonRoute()).thenReturn("{}");
        when(ri.getDistance()).thenReturn(1000.0F);
        when(ri.getDuration()).thenReturn(3600.0F);
        when(openRouteServiceApi.findRoute(eq(geo), eq(geo), eq("car"))).thenReturn(Optional.of(ri));

        TourEntity te = new TourEntity();
        te.setName("MyTour");
        when(tourService.mapModelToEntity(any(Tour.class))).thenReturn(te);

        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty(), Optional.of(te));

        importService.importSingleTour(f.getAbsolutePath());

        ArgumentCaptor<TourEntity> cap = ArgumentCaptor.forClass(TourEntity.class);
        verify(tourRepository).save(cap.capture());
        TourEntity saved = cap.getValue();
        assertEquals(1000.0, saved.getDistance());
        assertEquals(3600.0, saved.getDuration());
        assertNotNull(saved.getGeocodeDirections());
        verify(tourLogService).computeAverageValues(te);
    }

    @Test
    void importSingleTour_success_withLogs() throws Exception {
        TourTransferModel tm = new TourTransferModel(makeSampleTour(), List.of(makeSampleLog(), makeSampleLog()));
        File f = writeTransferModelToFile(tm);

        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty());

        Geocode geo = mock(Geocode.class);
        when(openRouteServiceApi.findGeocode(anyString())).thenReturn(Optional.of(geo));

        RouteInfo ri = mock(RouteInfo.class);
        when(ri.getJsonRoute()).thenReturn("{}");
        when(ri.getDistance()).thenReturn(2000.0F);
        when(ri.getDuration()).thenReturn(7200.0F);
        when(openRouteServiceApi.findRoute(any(Geocode.class), any(Geocode.class), anyString())).thenReturn(Optional.of(ri));

        TourEntity te = new TourEntity();
        te.setName("MyTour");
        when(tourService.mapModelToEntity(any(Tour.class))).thenReturn(te);

        when(tourLogService.mapModelToEntity(any(TourLog.class))).thenAnswer(inv -> new TourLogEntity());

        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty(), Optional.of(te));

        importService.importSingleTour(f.getAbsolutePath());

        verify(tourLogService, times(2)).mapModelToEntity(any(TourLog.class));
        ArgumentCaptor<TourEntity> cap = ArgumentCaptor.forClass(TourEntity.class);
        verify(tourRepository).save(cap.capture());
        assertEquals(2, cap.getValue().getTourLogs().size());
        verify(tourLogService).computeAverageValues(te);
    }

    @Test
    void importSingleTour_fileNotFound_throwsTourImportException() {
        assertThrows(TourImportException.class, () -> importService.importSingleTour("nonexistent.json"));
    }

    @Test
    void importSingleTour_duplicateName_throwsDuplicateTourNameException() throws Exception {
        File f = writeTransferModelToFile(new TourTransferModel(makeSampleTour(), Collections.emptyList()));
        when(tourRepository.findByName("MyTour")).thenReturn(Optional.of(new TourEntity()));
        assertThrows(DuplicateTourNameException.class, () -> importService.importSingleTour(f.getAbsolutePath()));
    }

    @Test
    void importSingleTour_startGeocodeMissing_throwsGeocodingException() throws Exception {
        File f = writeTransferModelToFile(new TourTransferModel(makeSampleTour(), Collections.emptyList()));
        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty());
        when(openRouteServiceApi.findGeocode("Start")).thenReturn(Optional.empty());
        assertThrows(GeocodingException.class, () -> importService.importSingleTour(f.getAbsolutePath()));
    }

    @Test
    void importSingleTour_endGeocodeMissing_throwsGeocodingException() throws Exception {
        File f = writeTransferModelToFile(new TourTransferModel(makeSampleTour(), Collections.emptyList()));
        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty());
        Geocode geo = mock(Geocode.class);
        when(openRouteServiceApi.findGeocode("Start")).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findGeocode("End")).thenReturn(Optional.empty());
        assertThrows(GeocodingException.class, () -> importService.importSingleTour(f.getAbsolutePath()));
    }

    @Test
    void importSingleTour_routeMissing_throwsRoutingException() throws Exception {
        File f = writeTransferModelToFile(new TourTransferModel(makeSampleTour(), Collections.emptyList()));
        when(tourRepository.findByName("MyTour")).thenReturn(Optional.empty());
        Geocode geo = mock(Geocode.class);
        when(openRouteServiceApi.findGeocode(anyString())).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findRoute(eq(geo), eq(geo), eq("car"))).thenReturn(Optional.empty());
        assertThrows(RoutingException.class, () -> importService.importSingleTour(f.getAbsolutePath()));
    }
}
