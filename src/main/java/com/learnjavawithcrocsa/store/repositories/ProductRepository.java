package com.learnjavawithcrocsa.store.repositories;

import com.learnjavawithcrocsa.store.dtos.ProductSummary;
import com.learnjavawithcrocsa.store.dtos.ProductSummaryDTO;
import com.learnjavawithcrocsa.store.entities.Category;
import com.learnjavawithcrocsa.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCriteriaRepository, JpaSpecificationExecutor<Product> {
    List<Product> findByName(String name);
    List<Product> findByNameLike(String name);
    List<Product> findByNameNotLike(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameNotContaining(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameEndingWith(String name);
    
    // find products whose prices are in a given range and sort by name

    @Procedure("findProductsByPrice")
    List<Product> findProducts(BigDecimal minPrice, BigDecimal maxPrice);


    @Query("select count(*) from Product p where p.price between :min and :max")
    long countProducts(@Param("min") BigDecimal minPrice, @Param("max") BigDecimal maxPrice);

    @Modifying
    @Query("update Product p set p.price=:newPrice where p.category.id=:CategoryId")
    void updatePriceByCategory(BigDecimal newPrice, Byte CategoryId);


    @Query("select p.id,p.name from Product p where p.category = :category")
    List<ProductSummaryDTO> findByCategory(@Param("category") Category category);

}