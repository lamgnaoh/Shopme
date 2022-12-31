package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 2;

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        List<Category> rootCategories = categoryRepository.findRootCategories(sort);

        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORIES_PER_PAGE, sort);

        Page<Category> pageCategories = categoryRepository.findRootCategories(pageable);
        List<Category> rootCategories = pageCategories.getContent();

        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        return listHierarchicalCategories(rootCategories, sortDir);
    }

//    list category theo cây
    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(rootCategory);

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                hierarchicalCategories.add(subCategory);

                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel, String sortDir) {
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            hierarchicalCategories.add(subCategory);

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
        }

    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    // sắp xep category con
    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
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

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    // delete category
    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }

}
