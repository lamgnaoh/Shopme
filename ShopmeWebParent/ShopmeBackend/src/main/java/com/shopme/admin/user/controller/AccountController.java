package com.shopme.admin.user.controller;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetailAccount(@AuthenticationPrincipal ShopmeUserDetails loggedUser , Model model){
//        @AuthenticationPrincipal trả về 1 object implement UserDetails , hiện tại đã được authenticate (user đang login hiện tại)
        String email = loggedUser.getUsername(); // lấy ra email của user đã login hiện tại
        User user = userService.getByEmail(email);
        model.addAttribute("user" , user);
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateDetailAccount(User user , RedirectAttributes redirectAttributes
            ,@AuthenticationPrincipal ShopmeUserDetails loggedUser
            , @RequestParam("image") MultipartFile multipartFile) throws IOException {
//        RedirectAttribute được dùng để truyền các giá trị , tham số khi thực hiện redirect
        System.out.println(user);
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User updatedUser = userService.updateAccount(user);
            String uploadDir = "src/main/resources/user-photos/" + updatedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        } else {
            if(user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message" , "Your account details have update successfully");
        return "redirect:/account";
    }
}
