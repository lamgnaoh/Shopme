package com.shopme.admin.category;

import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listAll(Model model , @RequestParam(required = false) String sortDir) {
        if (sortDir ==  null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        List<Category> listCategories = categoryService.listAll(sortDir);
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        List<Category> listCategories = categoryService.listAll("asc");

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes ra) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {

            categoryService.save(category);
        }
        ra.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable("id") int categoryId , RedirectAttributes re, Model model){
        try {
            Category category = categoryService.getCategory(categoryId);
            System.out.println(category.toString());
            List<Category> listCategories = categoryService.listAll("asc");
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("category" , category);
            model.addAttribute("pageTitle", "Edit Category " + categoryId + " )");
            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            re.addFlashAttribute("message" , e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message",
                    "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }


}
