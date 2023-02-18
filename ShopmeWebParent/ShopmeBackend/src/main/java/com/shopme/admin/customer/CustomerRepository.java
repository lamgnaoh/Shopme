package com.shopme.admin.customer;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends SearchRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, ' ', "
			+ " c.city, ' ', c.state, ' ', c.country.name , ' ' , c.postalCode ) LIKE %:keyword%  ")
	Page<Customer> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Customer c SET c.enabled = :enabled WHERE c.id = :id")
	@Modifying
	void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	Customer findByEmail(String email);
	
	Long countById(Integer id);
}
