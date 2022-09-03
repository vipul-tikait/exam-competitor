package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.Brand;
import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.repo.BrandRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

	@Autowired
	private BrandRepository repository;
	
	@Test
	public void testCreateBrand1() {
		
		Category laptop = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptop);
		
		Brand savedBrand = repository.save(acer);
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getName()).isEqualTo("Acer");
	}

	@Test
	public void testCreateBrand2() {
		
		Category cellPhones = new Category(4);
		Category tablets = new Category(7);
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellPhones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repository.save(apple);
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getName()).isEqualTo("Apple");
	}

	@Test
	public void testCreateBrand3() {
		
		Brand samsung = new Brand("Samsung");
		
		samsung.getCategories().add(new Category(24));
		samsung.getCategories().add(new Category(29));
		
		Brand savedBrand = repository.save(samsung);
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getName()).isEqualTo("Samsung");
	}

	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repository.findAll();
		brands.forEach(System.out::println);
		
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = repository.findById(1).get();
		assertThat(brand.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateBrand() {
		String newName  = "Samsung Electronics";
		Brand samsung = repository.findById(3).get();
			samsung.setName(newName);
			
		Brand savedBrand = repository.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	 @Test
	 public void testDelete() {
		 Integer id = 2;
		 repository.deleteById(id);
		 
		 Optional<Brand> result = repository.findById(id);
		 
		 assertThat(result.isPresent());
	 }
}
