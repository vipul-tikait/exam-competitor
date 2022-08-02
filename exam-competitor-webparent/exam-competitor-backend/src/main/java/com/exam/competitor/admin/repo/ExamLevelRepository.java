package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;

@Repository
public interface ExamLevelRepository extends PagingAndSortingRepository<ExamLevel, Integer> {

	@Query("select u from ExamLevel u where u.name = :name")
	public ExamLevel findByName(@Param("name") String name);

	@Query("SELECT b FROM ExamLevel b WHERE b.name LIKE %?1%")
	public Page<ExamLevel> findAll(String keyword, Pageable pageable);

	@Query("select c from ExamLevel c where c.name like %?1%")
	public Page<ExamLevel> search(String keyword, Pageable pageable);
	
	@Query("SELECT c FROM ExamLevel c WHERE c.exam.id=:id")
	List<ExamLevel> getExamLevelByExam(Integer id);
	

}
