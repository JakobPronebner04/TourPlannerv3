package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.entity.GeocodeDirectionsEntity;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.event.EventManager;
import at.jp.tourplanner.event.Events;
import at.jp.tourplanner.exception.*;
import at.jp.tourplanner.inputmodel.FilterTerm;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.dataaccess.StateDataAccess;
import at.jp.tourplanner.repository.TourRepositoryORM;
import at.jp.tourplanner.utils.PropertyValidator;

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
        Optional<FilterTerm> filterTerm = stateDataAccess.getSelectedTourFilterTerm();

        if (filterTerm.isEmpty()) {
            return tourRepository.findAll().stream().map(this::mapEntityToModel).toList();
        }

        if (filterTerm.get().getType().equals("AllTourFields")) {
            return tourRepository.findByFilterTermFullText(filterTerm.get().getText()).stream().map(this::mapEntityToModel).toList();
        }

        return tourRepository.findByFilterTerm(filterTerm.get().getText(), filterTerm.get().getType()).stream().map(this::mapEntityToModel).toList();
    }

    public void updateSelectedTour(Tour t) {
        stateDataAccess.updateSelectedTour(t);
    }

    public void updateSelectedFilter(FilterTerm filterTerm) {
        stateDataAccess.updateTourFilter(filterTerm);
        eventManager.publish(Events.TOURS_CHANGED, "FILTER_TOURS");
    }

    public Tour getSelectedTour() {
        return stateDataAccess.getSelectedTour();
    }

    public void add(Tour t) {

        PropertyValidator.validateOrThrow(t);

        if (tourRepository.findByName(t.getTourName()).isPresent()) {
            throw new DuplicateTourNameException("Tour name already exists");
        }

        Geocode geocodeStart = openRouteServiceApi.findGeocode(t.getTourStart())
                .orElseThrow(() -> new GeocodingException("Start destination not found"));

        Geocode geocodeEnd = openRouteServiceApi.findGeocode(t.getTourDestination())
                .orElseThrow(() -> new GeocodingException("End destination not found"));

        RouteInfo routeInfo = openRouteServiceApi.findRoute(geocodeStart, geocodeEnd, t.getTourTransportType())
                .orElseThrow(() -> new RoutingException("Route not found or distance limit exceeded"));

        GeocodeDirectionsEntity gde = new GeocodeDirectionsEntity();
        gde.setJsonDirections(routeInfo.getJsonRoute());

        TourEntity te = mapModelToEntity(t);
        te.setDistance(routeInfo.getDistance());
        te.setDuration(routeInfo.getDuration());
        te.setGeocodeDirections(gde);

        tourRepository.save(te);
        eventManager.publish(Events.TOURS_CHANGED, "NEW_TOUR");
    }

    public void edit(Tour editedTour) {
        try {
            PropertyValidator.validateOrThrow(editedTour);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Tourdaten sind ungültig: " + e.getMessage());
        }

        String currentName = stateDataAccess.getSelectedTour().getTourName();

        if (!editedTour.getTourName().equals(currentName)
                && tourRepository.findByName(editedTour.getTourName()).isPresent()) {
            throw new DuplicateTourNameException("Tour name already exists");
        }

        Geocode geocodeStart = openRouteServiceApi.findGeocode(editedTour.getTourStart())
                .orElseThrow(() -> new GeocodingException("Start destination not found"));

        Geocode geocodeEnd = openRouteServiceApi.findGeocode(editedTour.getTourDestination())
                .orElseThrow(() -> new GeocodingException("End destination not found"));

        RouteInfo routeInfo = openRouteServiceApi.findRoute(geocodeStart, geocodeEnd, editedTour.getTourTransportType())
                .orElseThrow(() -> new RoutingException("Route not found or distance limit exceeded"));

        GeocodeDirectionsEntity gde = new GeocodeDirectionsEntity();
        gde.setJsonDirections(routeInfo.getJsonRoute());

        TourEntity te = tourRepository.findByName(currentName).orElseThrow();

        te.setName(editedTour.getTourName());
        te.setDescription(editedTour.getTourDescription());
        te.setStart(editedTour.getTourStart());
        te.setDestination(editedTour.getTourDestination());
        te.setTransportType(editedTour.getTourTransportType());
        te.setDuration(routeInfo.getDuration());
        te.setDistance(routeInfo.getDistance());
        te.setPopularity(editedTour.getPopularity());
        te.setGeocodeDirections(gde);

        tourRepository.save(te);
        eventManager.publish(Events.TOURS_CHANGED, "UPDATED_TOUR");
    }

    public void remove() {
        Optional<TourEntity> te = tourRepository.findByName(stateDataAccess.getSelectedTour().getTourName());
        if(te.isEmpty()) throw new TourNotFoundException("Tour not found!");
        tourRepository.delete(te.get());
        eventManager.publish(Events.TOURS_CHANGED, "REMOVE_TOUR");
    }

    public TourEntity mapModelToEntity(Tour t) {
        TourEntity te = new TourEntity();
        te.setName(t.getTourName());
        te.setDescription(t.getTourDescription());
        te.setStart(t.getTourStart());
        te.setDestination(t.getTourDestination());
        te.setTransportType(t.getTourTransportType());
        te.setPopularity(t.getPopularity());
        te.setChildFriendliness(t.getChildFriendliness());
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
        t.setPopularity(entity.getPopularity());
        t.setChildFriendliness(entity.getChildFriendliness());
        return t;
    }
}
