package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.exam.Exam;
@Repository
public interface ExamRepository extends PagingAndSortingRepository<Exam, Integer> {

	

	@Query("select u from Exam u where u.name = :name")
	public Exam findByName(@Param("name") String name);
	
	
	  @Query("SELECT b FROM Exam b WHERE b.name LIKE %?1%") 
	  public Page<Exam> findAll(String keyword, Pageable pageable);
	  
	  @Query("select c from Exam c where c.name like %?1%") 
	  public Page<Exam> search(String keyword, Pageable pageable);
	  
	  @Query("SELECT c FROM Exam c WHERE c.mainExam.id=:id")
	  List<Exam> getExamByMainExam(Integer id);
	
	
	//public ExamLevel findByName(String name);
	
	/*
	 * public Long countById(Integer id);
	 * 
	 * @Query("SELECT b FROM Exam b WHERE b.name LIKE %?1%") public Page<Exam>
	 * findAll(String keyword, Pageable pageable);
	 * 
	 * @Query("select c from Exam c where c.name like %?1%") public Page<Topic>
	 * search(String keyword, Pageable pageable);
	 * 
	 * @Query("select NEW Exam(b.id, b.name) from Exam b ORDER by b.name ASC")
	 * public List<Exam> findAll();
	 */
	
}
