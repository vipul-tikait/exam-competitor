package com.exam.competitor.admin.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.exam.competitor.admin.common.entity.Topic;


public interface TopicRepository extends PagingAndSortingRepository<Topic, Integer> {

	@Query("select c from Topic c where c.parent.id is null")
	public List<Topic> findListRootTopic(Sort sort);
	
	/*
	 * @Query("select s.topics from ExamLevel s join s.topics where s.id = :id ")
	 * public List<Topic> findListRootTopicAssignToExmLevel(Sort sort, Integer id);
	 */
	@Query("select c from Topic c where c.parent.id is null")
	public Page<Topic> findListRootTopic(Pageable pageable);
	
	public List<Topic> findAll(Sort sort);
	
	public Long countById(Integer id);
	
	public Topic findByName(String name);
	
	public Topic findByAlias(String alias);
	
	@Query("select c from Topic c where c.name like %?1%") 
	public Page<Topic> search(String keyword, Pageable pageable);
	
	
	@Query("update Topic u set u.enabled = ?2 where u.id = ?1")
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
