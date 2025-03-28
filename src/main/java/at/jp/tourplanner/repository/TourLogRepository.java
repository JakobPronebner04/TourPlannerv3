package at.jp.tourplanner.repository;
import at.jp.tourplanner.entity.TourLogEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourLogRepository {
    Optional<TourLogEntity> find(UUID id);

    List<TourLogEntity> findAll();

    TourLogEntity save(TourLogEntity entity);

    TourLogEntity delete(TourLogEntity entity);

    List<TourLogEntity> deleteAll();

    List<TourLogEntity> findByTourId(UUID tourId);

    Optional<TourLogEntity> findByLocalDate(LocalDateTime localDateTime);
}
