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

//    Kiểm tra category name và alias có unique không
    public String checkUnique(Integer id , String name , String alias){
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = categoryRepository.findByName(name);
        Category categoryByAlias = categoryRepository.findByAlias(alias);

        //   tạo mới category
        if(isCreatingNew){
            if(categoryByName != null){
                return "DuplicateName";
            }else{
                if(categoryByAlias != null){
                    return "DuplicateAlias";
                }
            }
        } else { //  edit category
            if(categoryByName != null && categoryByName.getId() != id){
                return "DuplicateName";
            } else if (categoryByAlias != null && categoryByAlias.getId() != id){
                return "DuplicateAlias";
            }
        }
        return "OK";
    }
}
