package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.exam.competitor.admin.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

	@Query("select c from Category c where c.parent.id is null")
	public List<Category> findListRootCategory(Sort sort);
	
	@Query("select c from Category c where c.parent.id is null")
	public Page<Category> findListRootCategory(Pageable pageable);
	
	public List<Category> findAll(Sort sort);
	
	public Long countById(Integer id);
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
	
	@Query("select c from Category c where c.name like %?1%") 
	public Page<Category> search(String keyword, Pageable pageable);
	
	
	@Query("update Category u set u.enabled = ?2 where u.id = ?1")
	@Modifying 
	public void updateEnabledStatus(Integer id, boolean status);
	/*
	 * @Query("select u from Category u where u.name = :name") public Category
	 * getCategoryByName(@Param("name") String name);
	 * 
	 * @Query("select u from Category u where CONCAT(u.name, ' ',u.alias) like %?1%"
	 * ) public Page<Category> findAll(String keyword, Pageable pageable);
	 * 
	 *  */	
	
}
