package at.jp.tourplanner.repository;

import at.jp.tourplanner.entity.TourEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TourRepositoryORM implements TourRepository {
    private final EntityManagerFactory entityManagerFactory;

    public TourRepositoryORM() {
        this.entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate");
    }

    @Override
    public Optional<TourEntity> find(UUID id) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
        Root<TourEntity> root = query.from(TourEntity.class);

        query.select(root).where(cb.equal(root.get("id"), id));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery(query).getSingleResultOrNull()
            );
        }

    }

    @Override
    public List<TourEntity> findAll() {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
        Root<TourEntity> root = query.from(TourEntity.class);

        query.select(root);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query).getResultList();
        }

    }
    public List<TourEntity> findByFilterTerm(String text, String type) {
        String pattern = text + "%";
        String filterAttribute = TourFilterType.fromString(type).getFieldName();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
            Root<TourEntity> root = query.from(TourEntity.class);

            query.select(root).where(
                    cb.like(root.get(filterAttribute), pattern)
            );

            return entityManager.createQuery(query).getResultList();
        }
    }

    @Override
    public Optional<TourEntity> findByName(String name) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
        Root<TourEntity> root = query.from(TourEntity.class);

        query.select(root).where(cb.equal(root.get("tourName"), name));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery(query).getSingleResultOrNull()
            );
        }
    }


    @Override
    public TourEntity save(TourEntity entity) {
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
    public TourEntity delete(TourEntity entity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            TourEntity toRemove = entityManager.find(TourEntity.class, entity.getId());
            if (toRemove != null) {
                entityManager.remove(toRemove);
            }

            transaction.commit();
            return toRemove;
        }
    }

    @Override
    public List<TourEntity> deleteAll() {
        List<TourEntity> searchTerms = findAll();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaDelete<TourEntity> query = cb.createCriteriaDelete(TourEntity.class);
        Root<TourEntity> root = query.from(TourEntity.class);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.createQuery(query).executeUpdate();
            transaction.commit();
        }

        return searchTerms;
    }
}
