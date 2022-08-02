package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.exam.competitor.admin.common.entity.Brand;
import com.exam.competitor.admin.common.entity.Category;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

	public Brand findByName(String name);
	
	public Long countById(Integer id);

	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);
	
	@Query("select c from Brand c where c.name like %?1%") 
	public Page<Category> search(String keyword, Pageable pageable);
	
	@Query("select NEW Brand(b.id, b.name) from Brand b ORDER by b.name ASC") 
	public List<Brand> findAll();
	
}
