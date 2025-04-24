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

    TourLogEntity delete(UUID id);

    List<TourLogEntity> deleteAll();

    List<TourLogEntity> findByTourId(UUID tourId);
    List<TourLogEntity> findByFilterTermAndTourId(UUID tourId,String text,String type);

    Optional<TourLogEntity> findByLocalDate(LocalDateTime localDateTime);
}
