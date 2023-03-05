package com.shopme.admin.order;

import com.shopme.common.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.firstName LIKE %:keyword% OR"
            + " o.lastName LIKE %:keyword% OR o.phoneNumber LIKE %:keyword% OR"
            + " o.addressLine1 LIKE %:keyword% OR o.addressLine2 LIKE %:keyword% OR"
            + " o.postalCode LIKE %:keyword% OR o.city LIKE %:keyword% OR"
            + " o.state LIKE %:keyword% OR o.country LIKE %:keyword% OR"
            + " o.paymentMethod LIKE %:keyword% OR o.status LIKE %:keyword% OR"
            + " o.customer.firstName LIKE %:keyword% OR"
            + " o.customer.lastName LIKE %:keyword%")
    Page<Order> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);
}
