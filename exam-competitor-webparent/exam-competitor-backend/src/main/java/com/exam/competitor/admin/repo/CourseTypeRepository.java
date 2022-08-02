package com.exam.competitor.admin.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.course.CourseType;


@Repository
public interface CourseTypeRepository extends PagingAndSortingRepository<CourseType, Integer> {

	@Query("select ct from CourseType ct where ct.name = :name")
	public CourseType getCouseTypeRepoByName(@Param("name") String name);

	@Query("select u from CourseType u where CONCAT(u.name) like %?1%") 
	public Page<CourseType> findAll(String keyword, Pageable pageable);
	
	public Page<CourseType> findAll(Pageable pageable);

	
	
}
