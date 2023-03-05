package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {
 	List<CartItem> findByCustomer(Customer customer);
	
 	CartItem findByCustomerAndProduct(Customer customer, Product product);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.customer.id = :customerId AND c.product.id = :productId")
 	void updateQuantity(Integer quantity, Integer customerId, Integer productId);
	
	@Modifying
	@Query("DELETE FROM CartItem c WHERE c.customer.id = :customerId AND c.product.id = :productId")
 	void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}
