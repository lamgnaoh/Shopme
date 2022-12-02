package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final  int USERS_PER_PAGE = 4;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUser(){
        return (List<User>) userRepository.findAll();
    }

    public Page<User> listUsersByPage(int pageNum , String sortField , String sortDir , String keyword){
        // sorting theo truong field va theo asc hoac desc
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        // pagination
        Pageable pageable = PageRequest.of(pageNum - 1 , USERS_PER_PAGE , sort);
        if(keyword != null){
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
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
        return userRepository.save(user);
    }

    public User updateAccount(User userInForm){
        User user = userRepository.findById(userInForm.getId()).get();
//        if update password
        if(!userInForm.getPassword().isEmpty()){
            user.setPassword(userInForm.getPassword());
            encodePassword(user);
        }
//      if update user photos
        if(!userInForm.getPhotos().isEmpty()){
            user.setPhotos(userInForm.getPhotos());
        }
        user.setFirstName(userInForm.getFirstName());
        user.setLastName(userInForm.getLastName());
        return userRepository.save(user);
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

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }



}
