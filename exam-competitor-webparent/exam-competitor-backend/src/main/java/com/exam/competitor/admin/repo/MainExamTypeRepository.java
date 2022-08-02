
package com.exam.competitor.admin.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.exam.MainExamType;

@Repository
public interface MainExamTypeRepository extends PagingAndSortingRepository<MainExamType, Integer> {

	@Query("select u from MainExamType u where u.name = :name")
	public MainExamType findByName(@Param("name") String name);

	@Query("SELECT b FROM MainExamType b WHERE b.name LIKE %?1%")
	public Page<MainExamType> findAll(String keyword, Pageable pageable);

	
	/*
	 * @Query("SELECT s.id,s.name FROM MainExamType c JOIN mainExams s WHERE c.id=:id"
	 * ) List<Object[]> getMainExamByMainExamType(Integer id);
	 */
	/*
	 * @Query("select c from MainExamType c where c.name like %?1%") public
	 * Page<MainExamType> search(String keyword, Pageable pageable);
	 */
	/*
	 * @Query("update MainExamType u set u.enabled = ?2 where u.id = ?1")
	 * 
	 * @Modifying public void updateEnabledStatus(Integer id, boolean status);
	 */
	/*
	 * @Query("select NEW MainExam(b.id, b.name) from MainExam b ORDER by b.name ASC"
	 * ) public List<MainExam> findAll();
	 */

}
