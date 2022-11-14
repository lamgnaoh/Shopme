package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> listUsers = userService.listAllUser();
        model.addAttribute("listUsers" , listUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        User user = new User();
        user.setEnabled(true);
        List<Role> listRoles = userService.listAllRoles();
        model.addAttribute("user" , user);
        model.addAttribute("listRoles" , listRoles);
        model.addAttribute("pageTitle" , "Create new User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user , RedirectAttributes redirectAttributes){
//        RedirectAttribute được dùng để truyền các giá trị , tham số khi thực hiện redirect
        System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message" , "The user has been create successfully");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id , Model model , RedirectAttributes redirectAttributes){
        try {
            User user = userService.getUser(id);
            List<Role> listRoles = userService.listAllRoles();
            model.addAttribute("listRoles" , listRoles);
            model.addAttribute("user" , user);
            model.addAttribute("pageTitle" , "Edit User ( ID: " + id + " )");
            return "user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message" , e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id , Model model,RedirectAttributes redirectAttributes ){
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message" , "The user with id " + id + " has been deleted succesfully");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message" , e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id , @PathVariable("status") boolean enabled , RedirectAttributes redirectAttributes){
        userService.updateUserEnableStatus(id , enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user with id " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message" , message);
        return "redirect:/users";
    }

}
