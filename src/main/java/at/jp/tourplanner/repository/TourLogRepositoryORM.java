package at.jp.tourplanner.repository;
import at.jp.tourplanner.entity.TourEntity;
import at.jp.tourplanner.entity.TourLogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TourLogRepositoryORM implements TourLogRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourLogRepositoryORM() {
        this.entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate");
    }

    @Override
    public Optional<TourLogEntity> find(UUID id) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogEntity> query = cb.createQuery(TourLogEntity.class);
        Root<TourLogEntity> root = query.from(TourLogEntity.class);

        query.select(root).where(cb.equal(root.get("id"), id));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery(query).getSingleResultOrNull()
            );
        }
    }

    @Override
    public List<TourLogEntity> findAll() {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogEntity> query = cb.createQuery(TourLogEntity.class);
        Root<TourLogEntity> root = query.from(TourLogEntity.class);

        query.select(root);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query).getResultList();
        }

    }

    @Override
    public TourLogEntity save(TourLogEntity entity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            if (find(entity.getId()).isEmpty()) {
                entityManager.persist(entity);
            } else {
                entity = entityManager.merge(entity);
            }
            transaction.commit();
            return entity;
        }
    }
    @Override
    public TourLogEntity delete(UUID id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            TourLogEntity log = em.find(TourLogEntity.class, id);
            if (log != null) {
                TourEntity tour = log.getTour();
                tour.getTourLogs().remove(log);
                em.merge(tour);
            }

            tx.commit();
            return log;
        }
    }



    @Override
    public List<TourLogEntity> deleteAll() {
        List<TourLogEntity> searchTerms = findAll();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaDelete<TourLogEntity> query = cb.createCriteriaDelete(TourLogEntity.class);
        Root<TourLogEntity> root = query.from(TourLogEntity.class);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.createQuery(query).executeUpdate();
            transaction.commit();
        }

        return searchTerms;
    }
    @Override
    public List<TourLogEntity> findByTourId(UUID tourId) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogEntity> query = cb.createQuery(TourLogEntity.class);
        Root<TourLogEntity> root = query.from(TourLogEntity.class);

        query.select(root).where(cb.equal(root.get("tour").get("id"), tourId));

        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.createQuery(query).getResultList();
        }
    }

    @Override
    public List<TourLogEntity> findByFilterTermAndTourId(UUID tourId, String text, String type) {
        String filterAttribute = TourLogFilterType.fromString(type).getFieldName();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourLogEntity> query = cb.createQuery(TourLogEntity.class);
            Root<TourLogEntity> root = query.from(TourLogEntity.class);

            Predicate tourIdPredicate = cb.equal(root.get("tour").get("id"), tourId);
            Predicate filterPredicate;

            switch (filterAttribute) {
                case "actualTime":
                case "actualDistance":
                    try {
                        Float floatValue = Float.parseFloat(text);
                        filterPredicate = cb.equal(root.get(filterAttribute), floatValue);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Text muss eine gültige Zahl für " + filterAttribute + " sein.");
                    }
                    break;

                case "rating":
                    case "difficulty":
                    try {
                        Integer intValue = Integer.parseInt(text);
                        filterPredicate = cb.equal(root.get(filterAttribute), intValue);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Text muss eine gültige Ganzzahl für 'rating' sein.");
                    }
                    break;

                default:
                    String pattern = text + "%";
                    filterPredicate = cb.like(root.get(filterAttribute), pattern);
                    break;
            }

            query.select(root).where(cb.and(tourIdPredicate, filterPredicate));

            return entityManager.createQuery(query).getResultList();
        }
    }



    @Override
    public Optional<TourLogEntity> findByLocalDate(LocalDateTime localDateTime) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLogEntity> query = cb.createQuery(TourLogEntity.class);
        Root<TourLogEntity> root = query.from(TourLogEntity.class);

        query.select(root).where(cb.equal(root.get("dateTime"), localDateTime));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery(query).getSingleResultOrNull()
            );
        }
    }

}
