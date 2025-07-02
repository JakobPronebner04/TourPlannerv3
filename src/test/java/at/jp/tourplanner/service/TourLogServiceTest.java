package at.jp.tourplanner.service;

import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.exception.TourLogNotFoundException;
import at.jp.tourplanner.exception.TourNotFoundException;
import at.jp.tourplanner.exception.ValidationException;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.repository.TourLogRepositoryORM;
import at.jp.tourplanner.repository.TourRepositoryORM;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TourLogServiceTest {

    @Mock
    private EventManager eventManager;

    @Mock
    private StateDataAccess stateDataAccess;

    @Mock
    private TourLogRepositoryORM tourLogRepository;

    @Mock
    private TourRepositoryORM tourRepository;

    @InjectMocks
    private TourLogService service;

    private at.jp.tourplanner.inputmodel.Tour stubTour;

    @BeforeEach
    void setUp() {
        stubTour = mock(at.jp.tourplanner.inputmodel.Tour.class);
        when(stubTour.getTourName()).thenReturn("TestTour");
        when(stateDataAccess.getSelectedTour()).thenReturn(stubTour);
    }

    @Test
    void testUpdateSelectedTourLog() {
        TourLog tl = new TourLog();
        service.updateSelectedTourLog(tl);
        verify(stateDataAccess).updateSelectedTourLog(tl);
    }

    @Test
    void testUpdateSelectedFilter() {
        FilterTerm ft = new FilterTerm("text", "type");
        service.updateSelectedFilter(ft);
        verify(stateDataAccess).updateTourLogFilter(ft);
        verify(eventManager).publish(Events.TOURLOGS_CHANGED, "FILTER_TOURLOGS");
    }

    @Test
    void testGetSelectedTourLog() {
        TourLog tl = new TourLog();
        when(stateDataAccess.getSelectedTourLog()).thenReturn(tl);
        assertSame(tl, service.getSelectedTourLog());
    }

    @Test
    void testGetTourLogs_noSelectedTour() {
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.empty());
        List<TourLog> result = service.getTourLogs();
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTourLogs_noFilter() {
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));
        when(stateDataAccess.getSelectedTourLogFilterTerm()).thenReturn(Optional.empty());
        TourLogEntity tle = new TourLogEntity();
        tle.setComment("c");
        tle.setRating(2);
        tle.setDifficulty(3);
        tle.setActualTime(1.5f);
        tle.setActualDistance(2.5f);
        LocalDateTime dt = LocalDateTime.now();
        tle.setDateTime(dt);
        when(tourLogRepository.findByTourId(te.getId())).thenReturn(List.of(tle));

        List<TourLog> logs = service.getTourLogs();
        assertEquals(1, logs.size());
        TourLog ml = logs.get(0);
        assertEquals(tle.getComment(), ml.getComment());
        assertEquals(tle.getRating(), ml.getRating());
        assertEquals(tle.getDifficulty(), ml.getDifficulty());
        assertEquals(tle.getActualTime(), ml.getActualTime());
        assertEquals(tle.getActualDistance(), ml.getActualDistance());
        assertEquals(tle.getDateTime(), ml.getDateTime());
    }

    @Test
    void testGetTourLogs_fullTextFilter() {
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));
        FilterTerm ft = new FilterTerm("search", "AllTourLogFields");
        when(stateDataAccess.getSelectedTourLogFilterTerm()).thenReturn(Optional.of(ft));
        TourLogEntity tle = new TourLogEntity();
        tle.setComment("match");
        tle.setRating(1);
        tle.setDifficulty(0);
        tle.setActualTime(2f);
        tle.setActualDistance(3f);
        tle.setDateTime(LocalDateTime.now());
        when(tourLogRepository.findByTourIdAndTextFullSearch(te.getId(), "search"))
                .thenReturn(List.of(tle));

        List<TourLog> logs = service.getTourLogs();
        assertEquals(1, logs.size());
        assertEquals("match", logs.get(0).getComment());
    }

    @Test
    void testGetTourLogs_filterByTerm() {
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));
        FilterTerm ft = new FilterTerm("3", "rating");
        when(stateDataAccess.getSelectedTourLogFilterTerm()).thenReturn(Optional.of(ft));
        TourLogEntity tle = new TourLogEntity();
        tle.setComment("c");
        tle.setRating(3);
        tle.setDifficulty(2);
        tle.setActualTime(4f);
        tle.setActualDistance(5f);
        tle.setDateTime(LocalDateTime.now());
        when(tourLogRepository.findByFilterTermAndTourId(te.getId(), "3", "rating"))
                .thenReturn(List.of(tle));

        List<TourLog> logs = service.getTourLogs();
        assertEquals(1, logs.size());
        assertEquals(3, logs.get(0).getRating());
    }

    @Test
    void testAdd_invalidThrows() {
        TourLog tl = new TourLog(); // blank comment
        assertThrows(ValidationException.class, () -> service.add(tl));
    }

    @Test
    void testAdd_noTourThrows() {
        TourLog tl = new TourLog();
        tl.setComment("c");
        tl.setRating(1);
        tl.setDifficulty(1);
        tl.setActualTime(1f);
        tl.setActualDistance(1f);
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.empty());
        assertThrows(TourNotFoundException.class, () -> service.add(tl));
    }

    @Test
    void testAdd_success() {
        TourLog tl = new TourLog();
        tl.setComment("c");
        tl.setRating(1);
        tl.setDifficulty(1);
        tl.setActualTime(1f);
        tl.setActualDistance(1f);
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));

        service.add(tl);

        ArgumentCaptor<TourLogEntity> capt = ArgumentCaptor.forClass(TourLogEntity.class);
        verify(tourLogRepository).save(capt.capture());
        assertEquals("c", capt.getValue().getComment());
        verify(tourRepository).save(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "ADDED_COMPUTED_VALUES");
    }

    @Test
    void testEdit_invalidThrows() {
        TourLog tl = new TourLog(); // blank
        assertThrows(ValidationException.class, () -> service.edit(tl));
    }

    @Test
    void testEdit_noLogThrows() {
        TourLog tl = new TourLog();
        tl.setComment("c");
        tl.setRating(1);
        tl.setDifficulty(1);
        tl.setActualTime(1f);
        tl.setActualDistance(1f);
        TourLog selected = new TourLog();
        selected.setDateTime(LocalDateTime.now());
        when(stateDataAccess.getSelectedTourLog()).thenReturn(selected);
        when(tourLogRepository.findByLocalDate(selected.getDateTime())).thenReturn(Optional.empty());
        assertThrows(TourNotFoundException.class, () -> service.edit(tl));
    }

    @Test
    void testEdit_success() {
        TourLog tl = new TourLog();
        tl.setComment("new");
        tl.setRating(2);
        tl.setDifficulty(2);
        tl.setActualTime(2f);
        tl.setActualDistance(2f);
        TourLog selected = new TourLog();
        LocalDateTime dt = LocalDateTime.now();
        selected.setDateTime(dt);
        when(stateDataAccess.getSelectedTourLog()).thenReturn(selected);
        TourLogEntity tle = new TourLogEntity();
        tle.setDateTime(dt);
        when(tourLogRepository.findByLocalDate(dt)).thenReturn(Optional.of(tle));
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));

        service.edit(tl);

        assertEquals("new", tle.getComment());
        verify(tourLogRepository).save(tle);
        verify(tourRepository).save(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "ADDED_COMPUTED_VALUES");
    }

    @Test
    void testRemove_noLogThrows() {
        TourLog selected = new TourLog();
        selected.setDateTime(LocalDateTime.now());
        when(stateDataAccess.getSelectedTourLog()).thenReturn(selected);
        when(tourLogRepository.findByLocalDate(selected.getDateTime())).thenReturn(Optional.empty());
        assertThrows(TourLogNotFoundException.class, service::remove);
    }

    @Test
    void testRemove_success() {
        TourLog selected = new TourLog();
        LocalDateTime dt = LocalDateTime.now();
        selected.setDateTime(dt);
        when(stateDataAccess.getSelectedTourLog()).thenReturn(selected);
        TourLogEntity tle = new TourLogEntity();
        tle.setDateTime(dt);
        tle.setId(UUID.randomUUID());
        when(tourLogRepository.findByLocalDate(dt)).thenReturn(Optional.of(tle));
        TourEntity te = new TourEntity();
        when(tourRepository.findByName("TestTour")).thenReturn(Optional.of(te));

        service.remove();

        verify(tourLogRepository).delete(tle.getId());
        verify(tourRepository).save(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "ADDED_COMPUTED_VALUES");
    }

    @Test
    void testMapEntityToModel_andBack() {
        TourLogEntity tle = new TourLogEntity();
        tle.setComment("c");
        tle.setRating(3);
        tle.setDifficulty(4);
        tle.setActualTime(5f);
        tle.setActualDistance(6f);
        LocalDateTime dt = LocalDateTime.now();
        tle.setDateTime(dt);

        TourLog ml = service.mapEntityToModel(tle);
        assertEquals("c", ml.getComment());
        assertEquals(3, ml.getRating());
        assertEquals(4, ml.getDifficulty());
        assertEquals(5f, ml.getActualTime());
        assertEquals(6f, ml.getActualDistance());
        assertEquals(dt, ml.getDateTime());

        TourLogEntity newEntity = new TourLogEntity();
        service.mapModelToEntity(newEntity, ml);
        assertEquals("c", newEntity.getComment());
        assertEquals(3, newEntity.getRating());
        assertEquals(4, newEntity.getDifficulty());
        assertEquals(5f, newEntity.getActualTime());
        assertEquals(6f, newEntity.getActualDistance());

        TourLogEntity ret = service.mapModelToEntity(ml);
        assertEquals("c", ret.getComment());
    }

    @Test
    void testComputeAverageValues_noLogs() {
        TourEntity te = new TourEntity();
        when(tourRepository.save(te)).thenReturn(te);

        service.computeAverageValues(te);

        assertEquals(0, te.getChildFriendliness());
        assertEquals(0, te.getPopularity());
        verify(tourRepository).save(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "ADDED_COMPUTED_VALUES");
    }

    @Test
    void testComputeAverageValues_withLogs() {
        TourEntity te = new TourEntity();
        TourLogEntity l1 = new TourLogEntity();
        l1.setDifficulty(1);
        l1.setActualTime(1f);
        l1.setActualDistance(1f);
        TourLogEntity l2 = new TourLogEntity();
        l2.setDifficulty(3);
        l2.setActualTime(3f);
        l2.setActualDistance(7f);
        te.setTourLogs(List.of(l1, l2));
        when(tourRepository.save(te)).thenReturn(te);

        service.computeAverageValues(te);

        assertEquals(3, te.getChildFriendliness());
        assertEquals(2, te.getPopularity());
        verify(tourRepository).save(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "ADDED_COMPUTED_VALUES");
    }
}
