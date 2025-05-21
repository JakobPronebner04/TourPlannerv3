package at.jp.tourplanner.service;

import at.jp.tourplanner.dto.Geocode;
import at.jp.tourplanner.dto.RouteInfo;
import at.jp.tourplanner.entity.GeocodeDirectionsEntity;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import at.jp.tourplanner.exception.DuplicateTourNameException;
import at.jp.tourplanner.exception.GeocodingException;
import at.jp.tourplanner.exception.RoutingException;
import at.jp.tourplanner.exception.TourImportException;
import at.jp.tourplanner.inputmodel.Tour;
import at.jp.tourplanner.inputmodel.TourLog;
import at.jp.tourplanner.repository.TourLogRepository;
import at.jp.tourplanner.repository.TourLogRepositoryORM;
import at.jp.tourplanner.repository.TourRepository;
import at.jp.tourplanner.repository.TourRepositoryORM;
import at.jp.tourplanner.service.importexport.TourImportExport;
import at.jp.tourplanner.utils.PropertyValidator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ImportService {

    private final ObjectMapper objectMapper;
    private final TourRepository tourRepository;

    private final OpenRouteServiceApi openRouteServiceApi;
    private final TourService tourService;
    private final TourLogService tourLogService;
    public ImportService(TourService tourService, TourLogService tourLogService,TourRepositoryORM tourRepository, OpenRouteServiceApi openRouteServiceApi) {
        this.tourRepository = tourRepository;
        this.tourLogService = tourLogService;
        this.tourService = tourService;
        this.openRouteServiceApi = openRouteServiceApi;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void importSingleTour(String path){
        File inputFile = new File(path);
        TourImportExport importData;
        try {
            importData = objectMapper.readValue(inputFile, TourImportExport.class);
        } catch (IOException e) {
            throw new TourImportException("Issue while importing file: " + path);
        }


        Tour importedTour = importData.getTour();
        PropertyValidator.validateOrThrow(importedTour);

        if(tourRepository.findByName(importedTour.getTourName()).isPresent()) {
            throw new DuplicateTourNameException("Tour name already exists!");
        }

        Optional<Geocode> geocodeStart = openRouteServiceApi.findGeocode(importedTour.getTourStart());
        geocodeStart.orElseThrow(()->new GeocodingException("Start destination not found"));

        Optional<Geocode> geocodeEnd = openRouteServiceApi.findGeocode(importedTour.getTourDestination());
        geocodeEnd.orElseThrow(()->new GeocodingException("End destination not found"));

        Optional<RouteInfo> routInfo= openRouteServiceApi.findRoute(
                geocodeStart.get(),
                geocodeEnd.get(),
                importedTour.getTourTransportType());
        routInfo.orElseThrow(()->new RoutingException("Route not found or distance limit exceeded!"));

        GeocodeDirectionsEntity gde = new GeocodeDirectionsEntity();
        gde.setJsonDirections(routInfo.get().getJsonRoute());

        TourEntity te = tourService.mapModelToEntity(importedTour);
        te.setDistance(routInfo.get().getDistance());
        te.setDuration(routInfo.get().getDuration());

        te.setGeocodeDirections(gde);
        if (!importData.getTourLogs().isEmpty()) {
            importData.getTourLogs().forEach(PropertyValidator::validateOrThrow);
            List<TourLogEntity> tles = importData.getTourLogs().stream()
                    .map(tourLog -> {
                        TourLogEntity entity = tourLogService.mapModelToEntity(tourLog);
                        entity.setTour(te);
                        return entity;
                    })
                    .toList();
            te.setTourLogs(tles);
        }

        tourRepository.save(te);
        tourLogService.computeAverageValues(tourRepository.findByName(te.getName()).get());
    }
}
