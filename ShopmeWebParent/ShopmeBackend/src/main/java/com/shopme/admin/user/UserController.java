package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

}
