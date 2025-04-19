package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.entity.GeocodeDirectionsEntity;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.repository.TourRepositoryORM;
import at.jp.tourplanner.utils.PropertyValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourService {

    private final EventManager eventManager;
    private final StateDataAccess stateDataAccess;
    private final TourRepositoryORM tourRepository;
    private final OpenRouteServiceApi openRouteServiceApi;

    public TourService(OpenRouteServiceApi openRouteServiceApi, EventManager eventManager, StateDataAccess stateDataAccess, TourRepositoryORM tourRepository) {
        this.openRouteServiceApi = openRouteServiceApi;
        this.eventManager = eventManager;
        this.stateDataAccess = stateDataAccess;
        this.tourRepository = tourRepository;
    }
    public List<Tour> getTours() {

        Optional<FilterTerm> filterTerm =  this.stateDataAccess.getSelectedFilterTerm();
        if(filterTerm.isEmpty() || filterTerm.get().getText().isEmpty())
        {
            return this.tourRepository.findAll().stream().map(this::mapEntityToModel).toList();
        }
        return this.tourRepository.findByFilterTerm(filterTerm.get().getText(),filterTerm.get().getType()).stream().map(this::mapEntityToModel).toList();
    }

    public void updateSelectedTour(Tour t) {
        stateDataAccess.updateSelectedTour(t);
    }
    public void updateSelectedFilter(FilterTerm filterTerm) {
        stateDataAccess.updateFilter(filterTerm);
        eventManager.publish(Events.TOURS_CHANGED, "FILTER_TOURS");
    }
    public Tour getSelectedTour()
    {
        return stateDataAccess.getSelectedTour();
    }

    public void add(Tour t) {
        PropertyValidator.validateOrThrow(t);

        if(tourRepository.findByName(t.getTourName()).isPresent()) {
            throw new RuntimeException("Tour name already exists");
        }

        Optional<Geocode> geocodeStart = openRouteServiceApi.findGeocode(t.getTourStart());
        geocodeStart.orElseThrow(()->new RuntimeException("Start destination not found"));

        Optional<Geocode> geocodeEnd = openRouteServiceApi.findGeocode(t.getTourDestination());
        geocodeEnd.orElseThrow(()->new RuntimeException("End destination not found"));

        Optional<RouteInfo> routInfo= openRouteServiceApi.findRoute(
                geocodeStart.get(),
                geocodeEnd.get(),
                t.getTourTransportType());
        routInfo.orElseThrow(()->new RuntimeException("Route not found or distance limit exceeded!"));

        GeocodeDirectionsEntity gde = new GeocodeDirectionsEntity();
        gde.setJsonDirections(routInfo.get().getJsonRoute());

        TourEntity te = mapModelToEntity(t);
        te.setDistance(routInfo.get().getDistance());
        te.setDuration(routInfo.get().getDuration());

        te.setGeocodeDirections(gde);

        tourRepository.save(te);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }
    public void edit(Tour editedTour) {
        PropertyValidator.validateOrThrow(editedTour);

        if(!editedTour.getTourName().equals(stateDataAccess.getSelectedTour().getTourName())
                && tourRepository.findByName(editedTour.getTourName()).isPresent()) {
            throw new RuntimeException("Tour name already exists");
        }

        Optional<Geocode> geocodeStart = openRouteServiceApi.findGeocode(editedTour.getTourStart());
        geocodeStart.orElseThrow(()->new RuntimeException("Start destination not found"));

        Optional<Geocode> geocodeEnd = openRouteServiceApi.findGeocode(editedTour.getTourDestination());
        geocodeEnd.orElseThrow(()->new RuntimeException("End destination not found"));

        Optional<RouteInfo> routInfo = openRouteServiceApi.findRoute(geocodeStart.get(), geocodeEnd.get(),editedTour.getTourTransportType());
        routInfo.orElseThrow(()->new RuntimeException("Route not found or distance limit exceeded!"));

        GeocodeDirectionsEntity gde = new GeocodeDirectionsEntity();
        gde.setJsonDirections(routInfo.get().getJsonRoute());

        TourEntity te = tourRepository.findByName(this.stateDataAccess.getSelectedTour().getTourName()).get();

        te.setName(editedTour.getTourName());
        te.setDescription(editedTour.getTourDescription());
        te.setStart(editedTour.getTourStart());
        te.setDestination(editedTour.getTourDestination());
        te.setTransportType(editedTour.getTourTransportType());
        te.setDuration(routInfo.get().getDuration());
        te.setDistance(routInfo.get().getDistance());

        te.setGeocodeDirections(gde);

        tourRepository.save(te);
        eventManager.publish(Events.TOURS_CHANGED, "UPDATED_TOUR");
    }
    public void remove() {
        Optional<TourEntity> te = this.tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if(te.isEmpty()) {
            return;
        }
        this.tourRepository.delete(te.get());
        eventManager.publish(Events.TOURS_CHANGED, "REMOVE_TOUR");
    }

    private TourEntity mapModelToEntity(Tour t)
    {
        TourEntity te = new TourEntity();
        te.setName(t.getTourName());
        te.setDescription(t.getTourDescription());
        te.setStart(t.getTourStart());
        te.setDestination(t.getTourDestination());
        te.setTransportType(t.getTourTransportType());
        return te;
    }

    private Tour mapEntityToModel(TourEntity entity) {
        Tour t = new Tour();
        t.setTourName(entity.getName());
        t.setTourDescription(entity.getDescription());
        t.setTourStart(entity.getStart());
        t.setTourDestination(entity.getDestination());
        t.setTourTransportType(entity.getTransportType());
        t.setTourDistance(entity.getFormattedDistance());
        t.setTourDuration(entity.getFormattedDuration());
        return t;
    }

    public List<Geocode> getRouteGeocodes()
    {
        Optional<TourEntity> tour = tourRepository.findByName(this.stateDataAccess.getSelectedTour().getTourName());
        if(tour.isEmpty()) {
            return new ArrayList<>();
        }
        String jsonRoute = tour.get().getGeocodeDirections().getJsonDirections();
        List<Geocode> geocodes = openRouteServiceApi.getRouteCoordinatesFromJson(jsonRoute);
        if(geocodes.isEmpty()) {
            throw new RuntimeException("Route not found");
        }
        return geocodes;
    }
}
