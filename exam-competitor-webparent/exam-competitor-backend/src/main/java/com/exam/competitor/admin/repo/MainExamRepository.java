
package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.exam.MainExam;

@Repository
public interface MainExamRepository extends PagingAndSortingRepository<MainExam, Integer> {

	@Query("select u from MainExam u where u.name = :name")
	public MainExam findByName(@Param("name") String name);
	
	
	  @Query("SELECT b FROM MainExam b WHERE b.name LIKE %?1%") 
	  public Page<MainExam> findAll(String keyword, Pageable pageable);
	  
	  @Query("select c from MainExam c where c.name like %?1%") 
	  public Page<MainExam> search(String keyword, Pageable pageable);
	  
	  @Query("SELECT c FROM MainExam c WHERE c.mainExamType.id=:id")
	  List<MainExam> getMainExamByMainExamType(Integer id);
		
	/*
	 * // public Exam findByName(String name);
	 * 
	 * @Query("SELECT b FROM MainExam b WHERE b.name LIKE %?1%") public
	 * Page<MainExam> findAll(String keyword, Pageable pageable);
	 * 
	 * @Query("select c from MainExam c where c.name like %?1%") public Page<Topic>
	 * search(String keyword, Pageable pageable);
	 * 
	 * @Query("select NEW MainExam(b.id, b.name) from MainExam b ORDER by b.name ASC"
	 * ) public List<MainExam> findAll();
	 */

}
