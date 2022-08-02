package com.exam.competitor.admin;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.repo.CategoryRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testCreateRootCategory() {
		
		Category category = new Category("Computers");
		Category savedCategory = categoryRepository.save(category);
		
	    assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(3);
		Category laptop = new Category("Memory", parent);
		Category savedCategoty =categoryRepository.save(laptop);
		
	    assertThat(savedCategoty.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategories() {
		Category comp = categoryRepository.findById(1).get();
		System.out.println(comp.getName());
		Set<Category> children = comp.getChildren();
		for (Category category : children) {
			System.out.println("--"+category.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
		
	}
	
	@Test
	public void getCategoryHirarchi() {
		
		Iterable<Category> catgories = categoryRepository.findAll();
		for (Category category : catgories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());
				Set<Category> catSet = category.getChildren();
				for (Category cat : catSet) {
					System.out.println("--"+cat.getName());
					printCategory(cat,1);
				}
			}
		}
		
		
	}
	private void printCategory(Category parent, int subLevel) {
		int newSub = subLevel+1;
		
		Set<Category> categories = parent.getChildren();
		for (Category subCategory : categories) {
			for (int i=0; i<newSub;i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printCategory(subCategory, subLevel);
		}
		
	
	}
	
	@Test
	public void listRootCategory() {
		
	List<Category> listRootCategory = categoryRepository.findListRootCategory(Sort.by("name").ascending());
	listRootCategory.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testByName() {
		
		String name = "Computers";
		Category category = categoryRepository.findByName(name);
		
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
		
	}
	
	@Test
	public void testByAlias() {
		
		String name = "Books";
		Category category = categoryRepository.findByAlias(name);
		
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
		
	}
	
}
