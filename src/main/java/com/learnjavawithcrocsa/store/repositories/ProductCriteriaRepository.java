package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.entities.Category;
import com.learnjavawithcrocsa.store.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findProductByCriteria(String name);
}
