package at.jp.tourplanner.repository;

import at.jp.tourplanner.entity.TourEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourRepository {
    Optional<TourEntity> find(UUID id);

    List<TourEntity> findAll();

    Optional<TourEntity>findByName(String name);

    TourEntity save(TourEntity entity);

    TourEntity delete(TourEntity entity);

    List<TourEntity> deleteAll();
}
