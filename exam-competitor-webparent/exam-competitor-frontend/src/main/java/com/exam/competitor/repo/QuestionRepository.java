package com.exam.competitor.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.course.Question;


@Repository
@Transactional
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	/*
	 * @Query("select c from Question c where CONCAT(c.name, ' ',c.description) like %?1%"
	 * ) Page<Question> findAll(String keyword, Pageable pageable);
	 */
	@Query("update Question c set c.enabled = ?2 where c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	/*
	 * @Query("update Question c set c.enabled = ?2 where c.id = ?1")
	 * 
	 * @Modifying public void updateAssignQset(Integer id, boolean status);
	 * 
	 */
	@Query("SELECT p FROM Question p WHERE p.name LIKE %?1% " 
			+ "OR p.description LIKE %?1% "
			+ "OR p.questionPdfName LIKE %?1% "
			+ "OR p.topic.name LIKE %?1%")		
	public Page<Question> findAll(String keyword, Pageable pageable);
	
	
	@Query("SELECT p FROM Question p WHERE p.topic.id = ?1 "
			+ "OR p.topic.allParentIDs LIKE %?2%")	
	public Page<Question> findAllInTopic(Integer topicId, String topicIdMatch, 
			Pageable pageable);
	
	@Query("SELECT p FROM Question p WHERE (p.topic.id = ?1 "
			+ "OR p.topic.allParentIDs LIKE %?2%) AND "
			+ "(p.name LIKE %?3% " 
			+ "OR p.name LIKE %?3% "
			+ "OR p.description LIKE %?3% "
			+ "OR p.questionPdfName  LIKE %?3% "
			+ "OR p.topic.name LIKE %?3%)")			
	public Page<Question> searchInTopic(Integer topicId, String topicIdMatch, 
			String keyword, Pageable pageable);
	
	/*
	 * @Query("(select o from Question o where o.topic.id in :ids)") public
	 * Page<Question> searchInQuesForTopic(@Param("ids") Set<Integer>
	 * topicIds,Pageable pageable);
	 * 
	 * @Query("SELECT p FROM Question p WHERE (p.topic.id in ?ids " + " AND " +
	 * "(p.name LIKE %?2% " + "OR p.name LIKE %?2% " + "OR p.description LIKE %?2% "
	 * + "OR p.pdfName  LIKE %?2% " + "OR p.topic.name LIKE %?2%)") public
	 * Page<Question> searchInQuesForTopic(@Param("ids") Set<Integer> topicIds,
	 * String keyword, Pageable pageable);
	 */
	Page<Question> findByTopicIn(Set<Topic> ids,Pageable pageable);
	
	List<Question> findByTopicIn(Set<Topic> ids);
	
	 @Query("SELECT coalesce(max(c.id), 0) FROM Question c") 
	 public Integer getMaxId();
	
}
