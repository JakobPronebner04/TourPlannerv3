import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.model.Tour;
import at.jp.tourplanner.model.TourLog;
import at.jp.tourplanner.repository.StateRepository;
import at.jp.tourplanner.service.TourLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TourLogServiceTest {
    private TourLogService tourLogService;
    private EventManager eventManager;
    private StateRepository stateRepository;
    private Tour testTour;
    private TourLog testTourLog1;
    private TourLog testTourLog2;

    @BeforeEach
    void setUp() {
        eventManager = mock(EventManager.class);
        stateRepository = mock(StateRepository.class);

        testTour = new Tour();
        testTour.setTourName("testTour");
        testTour.setTourDescription("TestDescription");
        testTour.setTourStart("TestStart");
        testTour.setTourDestination("TestDestination");
        testTour.setTourTransportType("TestTransportType");

        testTourLog1 = new TourLog();
        testTourLog1.setActualTime(23.2f);
        testTourLog1.setActualDistance(13.4f);
        testTourLog1.setRating(2);
        testTourLog1.setComment("TestComment1");

        testTourLog2 = new TourLog();
        testTourLog2.setActualTime(2.0f);
        testTourLog2.setActualDistance(11.2f);
        testTourLog2.setRating(4);
        testTourLog2.setComment("TestComment2");


        tourLogService = new TourLogService(eventManager, stateRepository);

        when(stateRepository.getSelectedTour()).thenReturn(testTour);
        when(stateRepository.getSelectedTourLog()).thenReturn(testTourLog1);

        try {
            tourLogService.add(testTourLog1);
            tourLogService.add(testTourLog2);
        } catch (IllegalAccessException e) {
            fail("Unexpected exception while adding TourLogs: " + e.getMessage());
        }
    }

    @Test
    void remove_ShouldRemoveTourLogSuccessfully() {
        System.out.println("Vor Remove:");
        for (TourLog log : tourLogService.getTourLogs()) {
            System.out.println(log.getComment());
        }

        tourLogService.remove();

        System.out.println("Nach Remove:");
        for (TourLog log : tourLogService.getTourLogs()) {
            System.out.println(log.getComment());
        }

        List<TourLog> updatedLogs = tourLogService.getTourLogs();

        assertEquals(1, updatedLogs.size());

        assertFalse(updatedLogs.contains(testTourLog1));
        assertTrue(updatedLogs.contains(testTourLog2));
    }
    @Test
    void addTourLog_ShouldThrowExceptionForEmptyTourLog() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(), StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.setComment("");
        tl.setRating(3);
        tl.setActualDistance(12.5f);
        tl.setActualTime(2);
        assertThrows(IllegalAccessException.class, () -> {
            tls.add(tl);
        });
    }
    @Test
    void addTourLog_ShouldAddNewTourlogSuccessfully() throws IllegalAccessException {
        TourLogService tls = new TourLogService(new EventManager(),StateRepository.getInstance());
        TourLog tl = new TourLog();
        tl.setComment("TestComment");
        tl.setRating(5);
        tl.setActualDistance(14.5f);
        tl.setActualTime(2.5f);

        assertDoesNotThrow(() -> tls.add(tl));
    }
}
