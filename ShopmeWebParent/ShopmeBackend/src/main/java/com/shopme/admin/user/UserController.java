package com.shopme.admin.user;

import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(1,model);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum , Model model){
        Page<User> page = userService.listUsersByPage(pageNum);
        List<User> listUsers = page.getContent();
        long startCount = (long) (pageNum - 1) * UserService.USERS_PER_PAGE +1;
        long endCount =   startCount + UserService.USERS_PER_PAGE -1 ;
        if(endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

//        System.out.println(startCount);
//        System.out.println(endCount);
//        System.out.println(page.getTotalElements());
//        System.out.println(page.getTotalPages());
//        System.out.println(pageNum);

        model.addAttribute("startCount" , startCount);
        model.addAttribute("endCount" , endCount);
        model.addAttribute("totalItems" , page.getTotalElements());
        model.addAttribute("totalPages" , page.getTotalPages());
        model.addAttribute("currentPage" , pageNum);
        model.addAttribute("listUsers" ,listUsers);

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
    public String saveUser(User user , RedirectAttributes redirectAttributes , @RequestParam("image") MultipartFile multipartFile) throws IOException {
//        RedirectAttribute được dùng để truyền các giá trị , tham số khi thực hiện redirect
        System.out.println(user);
        if(!multipartFile.isEmpty()){
            System.out.println(multipartFile.getOriginalFilename());
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "src/main/resources/user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
                if(user.getPhotos().isEmpty()){
                    user.setPhotos(null);
                }
                userService.save(user);
        }

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
