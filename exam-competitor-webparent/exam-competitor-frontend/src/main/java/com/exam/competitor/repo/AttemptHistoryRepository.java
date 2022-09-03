package com.exam.competitor.repo;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.admin.common.entity.course.Course;


@Repository
public interface AttemptHistoryRepository extends PagingAndSortingRepository<AttemptHistory, Integer> {


	@Query("update AttemptHistory c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	

	@Query("select c from AttemptHistory c where CONCAT(c.description) like %?1%")
	Page<AttemptHistory> findAll(String keyword, Pageable pageable);
	
	
	/*
	 * @Query("select u from QuestionSet u where u.name like %?1%") public
	 * Page<QuestionSet> findAll(String keyword, Pageable pageable);
	 * 
	 * @Query("update QuestionSet u set u.enabled = ?2 where u.id = ?1")
	 * 
	 * @Modifying public void updateEnabledStatus(Integer id, boolean status);
	 */	
		
	public Set<AttemptHistory> findByCustomerAndCourse(Customer customer, Course course);
	
	
	
}
