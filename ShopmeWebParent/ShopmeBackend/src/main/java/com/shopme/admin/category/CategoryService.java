package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> listAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategory(int categoryId) throws CategoryNotFoundException {
        try{
            return categoryRepository.findById(categoryId).get();
        }catch (NoSuchElementException ex){
            throw new CategoryNotFoundException("Could not find any category with id " + categoryId);
        }
    }
}
