package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer> {
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = :countryId AND sr.state = :state")
	ShippingRate findByCountryAndState(Integer countryId, String state);
	
	@Query("UPDATE ShippingRate sr SET sr.codSupported = :enabled WHERE sr.id = :id")
	@Modifying
	void updateCODSupport(Integer id, boolean enabled);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %:keyword% OR sr.state LIKE %:keyword% OR sr.id = :keyword")
	Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	Long countById(Integer id);
}
