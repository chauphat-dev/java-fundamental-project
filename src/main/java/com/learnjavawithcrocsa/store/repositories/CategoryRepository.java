package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}