package at.jp.tourplanner.repository;

import at.jp.tourplanner.entity.TourEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;

import java.util.*;

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
    @Override
    public List<TourEntity> findByFilterTerm(String text, String type) {
        String filterAttribute = TourFilterType.fromString(type).getFieldName();

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
            Root<TourEntity> root = query.from(TourEntity.class);

            Predicate filterPredicate;

            switch (filterAttribute) {
                case "popularity":
                case "childFriendliness":
                    try {
                        Integer intValue = Integer.parseInt(text);
                        filterPredicate = cb.equal(root.get(filterAttribute), intValue);
                    } catch (NumberFormatException e) {
                        return Collections.emptyList();
                    }
                    break;

                default:
                    String pattern = text + "%";
                    filterPredicate = cb.like(root.get(filterAttribute), pattern);
                    break;
            }

            query.select(root).where(filterPredicate);
            return entityManager.createQuery(query).getResultList();
        }
    }

    @Override
    public List<TourEntity> findByFilterTermFullText(String text) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TourEntity> query = cb.createQuery(TourEntity.class);
            Root<TourEntity> root = query.from(TourEntity.class);

            List<Predicate> predicates = new ArrayList<>();

            try {
                Integer intValue = Integer.parseInt(text);
                predicates.add(cb.equal(root.get("popularity"), intValue));
                predicates.add(cb.equal(root.get("childFriendliness"), intValue));
            } catch (NumberFormatException ignored) {}

            String pattern = "%" + text + "%";
            predicates.add(cb.like(root.get("tourName"), pattern));
            predicates.add(cb.like(root.get("tourDescription"), pattern));
            predicates.add(cb.like(root.get("tourStart"), pattern));
            predicates.add(cb.like(root.get("tourDestination"), pattern));
            predicates.add(cb.like(root.get("tourTransportType"), pattern));
            predicates.add(cb.like(root.get("formattedDistance"), pattern));
            predicates.add(cb.like(root.get("formattedDuration"), pattern));

            Predicate fullSearchPredicate = cb.or(predicates.toArray(new Predicate[0]));

            query.select(root).where(fullSearchPredicate);

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
