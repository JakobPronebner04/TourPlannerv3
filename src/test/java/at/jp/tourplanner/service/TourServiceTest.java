package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.entity.GeocodeDirectionsEntity;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.exception.DuplicateTourNameException;
import at.jp.tourplanner.exception.GeocodingException;
import at.jp.tourplanner.exception.RoutingException;
import at.jp.tourplanner.exception.TourNotFoundException;
import at.jp.tourplanner.exception.ValidationException;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.repository.TourRepositoryORM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TourServiceTest {

    @Mock
    private OpenRouteServiceApi openRouteServiceApi;

    @Mock
    private EventManager eventManager;

    @Mock
    private StateDataAccess stateDataAccess;

    @Mock
    private TourRepositoryORM tourRepository;

    @InjectMocks
    private TourService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testAdd_success() {
        Tour t = new Tour();
        t.setTourName("N");
        t.setTourDescription("Desc");
        t.setTourStart("S");
        t.setTourDestination("D");
        t.setTourTransportType("Car");
        Geocode geo = new Geocode();
        RouteInfo ri = new RouteInfo(); ri.setJsonRoute("J"); ri.setDistance(10.5f); ri.setDuration(2.5f);
        when(tourRepository.findByName("N")).thenReturn(Optional.empty());
        when(openRouteServiceApi.findGeocode("S")).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findGeocode("D")).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findRoute(geo, geo, "Car")).thenReturn(Optional.of(ri));

        service.add(t);

        ArgumentCaptor<TourEntity> capt = ArgumentCaptor.forClass(TourEntity.class);
        verify(tourRepository).save(capt.capture());
        TourEntity saved = capt.getValue();
        assertEquals(10.5f, saved.getDistance());
        assertEquals(2.5f, saved.getDuration());
        GeocodeDirectionsEntity gde = saved.getGeocodeDirections();
        assertEquals("J", gde.getJsonDirections());
        verify(eventManager).publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }

    @Test
    void testEdit_duplicateNameThrows() {
        Tour current = new Tour();
        current.setTourName("Old");
        when(stateDataAccess.getSelectedTour()).thenReturn(current);
        Tour edited = new Tour();
        edited.setTourName("New");
        edited.setTourDescription("Desc");
        edited.setTourStart("Start");
        edited.setTourDestination("Dest");
        edited.setTourTransportType("Car");
        when(tourRepository.findByName("New")).thenReturn(Optional.of(new TourEntity()));
        assertThrows(DuplicateTourNameException.class, () -> service.edit(edited));
    }

    @Test
    void testEdit_success() {
        Tour current = new Tour(); current.setTourName("Old");
        Tour edited = new Tour();
        edited.setTourName("New"); edited.setTourDescription("Desc"); edited.setTourStart("S");
        edited.setTourDestination("D"); edited.setTourTransportType("Car"); edited.setPopularity(7);
        Geocode geo = new Geocode(); RouteInfo ri = new RouteInfo(); ri.setJsonRoute("J"); ri.setDistance(5f); ri.setDuration(1f);
        TourEntity te = new TourEntity(); te.setName("Old");

        when(stateDataAccess.getSelectedTour()).thenReturn(current);
        when(openRouteServiceApi.findGeocode(anyString())).thenReturn(Optional.of(geo));
        when(openRouteServiceApi.findRoute(any(), any(), eq("Car"))).thenReturn(Optional.of(ri));
        when(tourRepository.findByName("Old")).thenReturn(Optional.of(te));

        service.edit(edited);

        assertEquals("New", te.getName());
        assertEquals("Desc", te.getDescription());
        assertEquals(5f, te.getDistance());
        assertEquals(1f, te.getDuration());
        assertEquals(7, te.getPopularity());
        assertEquals("J", te.getGeocodeDirections().getJsonDirections());
        verify(eventManager).publish(Events.TOURS_CHANGED, "UPDATED_TOUR");
    }

    @Test
    void testRemove_noTourThrows() {
        when(stateDataAccess.getSelectedTour()).thenReturn(new Tour());
        when(tourRepository.findByName(null)).thenReturn(Optional.empty());
        assertThrows(TourNotFoundException.class, service::remove);
    }

    @Test
    void testRemove_success() {
        Tour t = new Tour(); t.setTourName("X");
        TourEntity te = new TourEntity();
        when(stateDataAccess.getSelectedTour()).thenReturn(t);
        when(tourRepository.findByName("X")).thenReturn(Optional.of(te));

        service.remove();
        verify(tourRepository).delete(te);
        verify(eventManager).publish(Events.TOURS_CHANGED, "REMOVE_TOUR");
    }
}
