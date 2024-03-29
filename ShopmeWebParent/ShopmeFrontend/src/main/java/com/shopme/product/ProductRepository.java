package com.shopme.product;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

//	list product by category id or in sub category of the selected category
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = :categoryId OR p.category.allParentIDs LIKE %:categoryIdMatch%)"
			+ " ORDER BY p.name ASC")
	Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

	Product findByAlias(String alias);

	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
			+ " MATCH(name, short_description, full_description) AGAINST (:keyword)",
			nativeQuery = true)
	Page<Product> search(String keyword, Pageable pageable);
}
