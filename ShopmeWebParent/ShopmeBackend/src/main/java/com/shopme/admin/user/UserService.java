package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUser(){
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public void save(User user) {
        boolean isUpdatingUser = (user.getId() != null );

        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                // khong thay doi password
                user.setPassword(existingUser.getPassword());
            } else { // thay doi password
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        // Save user
        userRepository.save(user);
    }

    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

//    check email unique
    public boolean isEmailUnique(Integer id , String email){
        User userByEmail = userRepository.getUserByEmail(email);
        boolean isCreatingNew = (id == null);
        if(isCreatingNew) {
            if(userByEmail != null) return false;
        }else {
            if(userByEmail.getId() != id) return false;
        }
        return true;
    }

    //get user by id
    public User getUser(Integer id) throws UserNotFoundException {
        try{
            return userRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not find any user with id " + id);
        }

    }

//    Delete user logic
    public void deleteUser(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if(countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with id " + id);
        }
        userRepository.deleteById(id);
    }

    // Update enable status
    public void updateUserEnableStatus(Integer id , boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }

}
