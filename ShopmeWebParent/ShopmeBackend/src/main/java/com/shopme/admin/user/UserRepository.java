package com.shopme.admin.user;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends SearchRepository<User, Integer> {

//    PagingAndSortingRepository


//    annotation @Query sử dụng để khai báo câu query hsql cho method trong repository
//    annotation @param sử dụng để truyền tham số method vào trong câu lệnh query
    @Query("Select u from User u where u.email = :email")
    User getUserByEmail(@Param("email") String email);

    @Query("Select u from User u where concat(u.id , ' ' , u.email , ' ' , u.firstName , ' ' , u.lastName) like %:keyword% ")
     Page<User> findAll(@Param("keyword")String keyword , Pageable pageable);

     Long countById(Integer Id);
     @Query("Update User u Set u.enabled = :enabled where u.id = :id")
     @Modifying
     void updateEnabledStatus(@Param("id") Integer id , @Param("enabled") boolean enabled);
}
