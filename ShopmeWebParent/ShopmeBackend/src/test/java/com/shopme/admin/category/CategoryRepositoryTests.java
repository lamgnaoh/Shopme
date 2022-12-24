package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // run test with the real database not in-memory database
@Rollback(false) // keep the data in database after the test
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory() {
        Category parent = new Category(7);
        Category memory = new Category("Iphones", parent);
        Category savedCategory = categoryRepository.save(memory);
        assertThat(savedCategory.getId()).isGreaterThan(0);


    }

    @Test
    public void testGetCategory() {
        Category category = categoryRepository.findById(2).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testFindByName() {
        String name = "Computers";
        Category category = categoryRepository.findByName(name);
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }
    @Test
    public void testFindByAlias() {
        String alias = "Computers";
        Category category = categoryRepository.findByAlias(alias);
        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }

}

