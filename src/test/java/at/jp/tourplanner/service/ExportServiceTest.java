package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.exception.TourNotFoundException;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.repository.TourRepositoryORM;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    private TourRepositoryORM repo;
    private StateDataAccess stateDataAccess;
    private ExportService exportService;

    private final Path singleDir = Paths.get("singletours");
    private final Path jsonDir = Paths.get("jsonExportedTours");

    @BeforeEach
    void setUp() throws IOException {
        repo = mock(TourRepositoryORM.class);
        stateDataAccess = mock(StateDataAccess.class);
        exportService = new ExportService(repo, stateDataAccess);
        if (Files.exists(singleDir)) {
            Files.walk(singleDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
        if (Files.exists(jsonDir)) {
            Files.walk(jsonDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(singleDir)) {
            Files.walk(singleDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
        if (Files.exists(jsonDir)) {
            Files.walk(jsonDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
    }

    @Test
    void exportSingleTourAsPDF_tourNotFound_throws() {
        Tour sel = mock(Tour.class);
        when(sel.getTourName()).thenReturn("Foo");
        when(stateDataAccess.getSelectedTour()).thenReturn(sel);
        when(repo.findByName("Foo")).thenReturn(Optional.empty());
        assertThrows(TourNotFoundException.class, () ->
                exportService.exportSingleTourAsPDF(
                        new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)
                )
        );
    }

    @Test
    void exportSingleTourAsJSON_tourNotFound_throws() {
        Tour sel = mock(Tour.class);
        when(sel.getTourName()).thenReturn("Bar");
        when(stateDataAccess.getSelectedTour()).thenReturn(sel);
        when(repo.findByName("Bar")).thenReturn(Optional.empty());
        assertThrows(TourNotFoundException.class, () ->
                exportService.exportSingleTourAsJSON()
        );
    }

    @Test
    void exportStatisticalSummaryReport_noTours_throws() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        assertThrows(TourNotFoundException.class, () ->
                exportService.exportStatisticalSummaryReport()
        );
    }

    @Test
    void exportStatisticalSummaryReport_withTours_noException() throws IOException {
        TourEntity tour = mock(TourEntity.class);
        when(tour.getName()).thenReturn("T1");
        when(tour.getTourLogs()).thenReturn(Collections.emptyList());
        when(repo.findAll()).thenReturn(List.of(tour));
        assertDoesNotThrow(() ->
                exportService.exportStatisticalSummaryReport()
        );
    }

    @Test
    void exportSingleTourAsPDF_success_writesFile() throws IOException {
        String name = "MyTour_" + UUID.randomUUID();
        Tour sel = mock(Tour.class);
        when(sel.getTourName()).thenReturn(name);
        when(stateDataAccess.getSelectedTour()).thenReturn(sel);

        TourEntity entity = mock(TourEntity.class);
        when(entity.getName()).thenReturn(name);
        when(entity.getDescription()).thenReturn("Desc");
        when(entity.getStart()).thenReturn("A");
        when(entity.getDestination()).thenReturn("B");
        when(entity.getTransportType()).thenReturn("Car");
        when(entity.getDistance()).thenReturn((float) 10000L);
        when(entity.getDuration()).thenReturn((float) 3600L);
        when(entity.getTourLogs()).thenReturn(Collections.emptyList());
        when(repo.findByName(name)).thenReturn(Optional.of(entity));

        exportService.exportSingleTourAsPDF(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));

        assertTrue(Files.isDirectory(singleDir));
        List<Path> pdfs = Files.list(singleDir)
                .filter(p -> p.toString().endsWith(".pdf"))
                .collect(Collectors.toList());
        assertEquals(1, pdfs.size());
    }

    @Test
    void exportSingleTourAsJSON_success_writesFile() throws IOException {
        String name = "JsonTour_" + UUID.randomUUID();
        Tour sel = mock(Tour.class);
        when(sel.getTourName()).thenReturn(name);
        when(stateDataAccess.getSelectedTour()).thenReturn(sel);

        TourEntity entity = mock(TourEntity.class);
        when(entity.getName()).thenReturn(name);
        when(entity.getDescription()).thenReturn("Desc");
        when(entity.getStart()).thenReturn("A");
        when(entity.getDestination()).thenReturn("B");
        when(entity.getTransportType()).thenReturn("Walk");
        when(entity.getTourLogs()).thenReturn(Collections.emptyList());
        when(repo.findByName(name)).thenReturn(Optional.of(entity));

        exportService.exportSingleTourAsJSON();

        assertTrue(Files.isDirectory(jsonDir));
        List<Path> jsons = Files.list(jsonDir)
                .filter(p -> p.toString().endsWith(".json"))
                .collect(Collectors.toList());
        assertEquals(1, jsons.size());
    }
}
