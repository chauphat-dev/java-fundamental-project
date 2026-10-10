package com.learnjavawithcrocsa.store.repositories.specifications;

import com.learnjavawithcrocsa.store.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpec {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
    public static Specification<Product> hasPriceGreaterThanOrEqualTo(BigDecimal price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }
    public static Specification<Product> hasPriceLessThanOrEqualTo(BigDecimal price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> isCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("name"), category);
    }
}
