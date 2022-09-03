package com.exam.competitor.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;


@Repository
public interface QuestionSetRepository extends PagingAndSortingRepository<QuestionSet, Integer> {

	
	
	@Query("select u from QuestionSet u where u.name like %?1%") 
	public Page<QuestionSet> findAll(String keyword, Pageable pageable);

	@Query("update QuestionSet u set u.enabled = ?2 where u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	public List<QuestionSet> findByExamLevel(ExamLevel examLevel);
	
}
