package com.exam.competitor.admin.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.course.Course;


@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {

	@Query("select c from Course c where CONCAT(c.name, ' ',c.description) like %?1%")
	Page<Course> findAll(String keyword, Pageable pageable);
	
	@Query("update Course c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	@Query("update Course c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateAssignQset(Integer id, boolean status);
	
	
	  @Query("SELECT coalesce(max(c.id), 0) FROM Course c") public Integer
	  getMaxId();
	 
	
}
