package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        Role roleAdmin = entityManager.find(Role.class , 1);
        User adminUser = new User("admin.shopme@gmail.com" , "admin" , "Luong Hoang" , "Lam");
        adminUser.addRole(roleAdmin);
        User savedUser = userRepository.save(adminUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithMultipleRole(){
        Role roleEditor = entityManager.find(Role.class , 3);
        Role roleAssistant = entityManager.find(Role.class , 5);
        User newUser = new User("A.nguyenvan@gmail.com" , "123456" , "Nguyen Van" , "A");
        newUser.addRole(roleEditor);
        newUser.addRole(roleAssistant);
        User savedUser = userRepository.save(newUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUser(){
        List<User> listUsers = (List<User>) userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));

    }

    @Test
    public void testGetUserById(){
        User user = userRepository.findById(1).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetail(){
        User user = userRepository.findById(1).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Test
    public void testUpdateUserRole(){
        User user = userRepository.findById(2).get();
        Role roleEditor = entityManager.find(Role.class , 3);
        Role roleSalesPerson = entityManager.find(Role.class , 2);

        user.getRoles().remove(roleEditor);
        user.getRoles().add(roleSalesPerson);
        userRepository.save(user);
    }

    @Test
    public void testDeleteUserById(){
        Integer deleteId = 2;
        userRepository.deleteById(deleteId);

    }

    @Test
    public void testGetUserByEmail(){
        String email = "abc@gmail.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNull();
    }
}
