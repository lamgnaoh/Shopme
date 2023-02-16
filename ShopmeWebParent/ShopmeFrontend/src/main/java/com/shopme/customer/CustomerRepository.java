package com.shopme.customer;

import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	 Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = :code")
	 Customer findByVerificationCode(String code);
	
	@Query("UPDATE Customer c SET c.enabled = true , c.verificationCode = null  WHERE c.id = :id")
	@Modifying
	 void enable(Integer id);
}
