package com.shopme.admin.product;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = :enabled WHERE p.id = :id")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    Long countById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% "
            + "OR p.shortDescription LIKE %:keyword% "
            + "OR p.fullDescription LIKE %:keyword% "
            + "OR p.brand.name LIKE %:keyword% "
            + "OR p.category.name LIKE %:keyword%")
    Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId "
            + "OR p.category.allParentIDs LIKE %:categoryParentId%")
    Page<Product> findAllInCategory(Integer categoryId, String categoryParentId,
                                           Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = :categoryId "
            + "OR p.category.allParentIDs LIKE %:categoryParentId%) AND "
            + "(p.name LIKE %:keyword% "
            + "OR p.brand.name LIKE %:keyword% "
            + "OR p.category.name LIKE %:keyword%)")
    Page<Product> searchInCategory(Integer categoryId, String categoryParentId, String keyword, Pageable pageable);
}
