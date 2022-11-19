package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User , Integer> {

//    PagingAndSortingRepository


//    annotation @Query sử dụng để khai báo câu query hsql cho method trong repository
//    annotation @param sử dụng để truyền tham số method vào trong câu lệnh query
    @Query("Select u from User u where u.email = :email")
    User getUserByEmail(@Param("email") String email);

     Long countById(Integer Id);
     @Query("Update User u Set u.enabled = :enabled where u.id = :id")
     @Modifying
     void updateEnabledStatus(@Param("id") Integer id , @Param("enabled") boolean enabled);
}
