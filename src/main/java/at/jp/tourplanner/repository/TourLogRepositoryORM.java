package at.jp.tourplanner.repository;
import at.jp.tourplanner.entity.TourLogEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
    public TourLogEntity delete(TourLogEntity entity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            TourLogEntity toRemove = entityManager.find(TourLogEntity.class, entity.getId());
            if (toRemove != null) {
                entityManager.remove(toRemove);
            }

            transaction.commit();
            return toRemove;
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
