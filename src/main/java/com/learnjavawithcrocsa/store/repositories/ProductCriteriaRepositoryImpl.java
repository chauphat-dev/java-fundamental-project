package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.entities.Category;
import com.learnjavawithcrocsa.store.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Product> findProductByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            // name like %name%
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }
        if (minPrice != null) {
            // price >= minPrice
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            // price <= maxPrice
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Product> findProductByCriteria(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(builder.equal(root.get("category").get("name"), name));
        }

        query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        return em.createQuery(query).getResultList();
    }
}
