package com.exam.competitor.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.exception.QuestionNotFoundException;
import com.exam.competitor.repo.QuestionRepository;

@Service
@Transactional
public class QuestionService {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	QuestionSetService questionSetService;
	
	public static final int QUESTION_PER_PAGE = 4;

	public List<Question> findAllQuestionList() {

		return (List<Question>) questionRepository.findAll();
	}
	public Integer getMaxeId() {

		return  questionRepository.getMaxId();
	}

	/*
	 * public User getUserByEmail(String email) { return
	 * userRepo.getUserByEmail(email); }
	 */

	public Page<Question> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer topicId) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, QUESTION_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty()) {
			if (topicId != null && topicId > 0) {
				String topicIdMatch = "-" + String.valueOf(topicId) + "-";
				return questionRepository.searchInTopic(topicId, topicIdMatch, keyword, pageable);
			}
			return questionRepository.findAll(keyword, pageable);
		}
		if (topicId != null && topicId > 0) {
			String topicIdMatch = "-" + String.valueOf(topicId) + "-";
			return questionRepository.findAllInTopic(topicId, topicIdMatch, pageable);
		}
		return questionRepository.findAll(pageable);
	}

	public Page<Question> listByPageForTopics(int pageNum, String sortField, String sortDir, String keyword, Set<Topic> topicSet) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum - 1, QUESTION_PER_PAGE, sort);
		
		Set<Integer> setInt = new HashSet<>();
		for (Topic topic : topicSet) {
			setInt.add(topic.getId());
		}

		/*
		 * if (keyword != null && !keyword.isEmpty()) { if (topicSet != null &&
		 * topicSet.size() > 0) { return questionRepository.searchInQuesForTopic(setInt,
		 * keyword, pageable); } //return questionRepository.findAll(keyword, pageable);
		 * 
		 * return questionRepository.findByTopicIn(setInt,pageable); }
		 */
		
		return questionRepository.findByTopicIn(topicSet,pageable);
	}
	
	public Question save(Question question) {

		return questionRepository.save(question);
	}

	public Question getQuestionById(Integer id) throws QuestionNotFoundException {

		try {
			return questionRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new QuestionNotFoundException("Could not find any question by ID:" + id);
		}
	}

	public void deleteQuestion(Integer id) throws QuestionNotFoundException {

		try {
			questionRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new QuestionNotFoundException("Could not find any question by ID:" + id);
		}
	}

	public void updateEnabledStatus(Integer id, boolean status) {
		questionRepository.updateEnabledStatus(id, status);
	}

	
	/*
	 * public List<Course> listByCourseType(Integer courseType) { return
	 * courseRepo.findAllByCouseType(courseType); }
	 */

	/*
	 * public User updateAccount(User userForm) { User userInDB =
	 * userRepo.findById(userForm.getId()).get();
	 * 
	 * if (!userForm.getPassword().isEmpty()) {
	 * userInDB.setPassword(userForm.getPassword()); encodePassword(userInDB); }
	 * 
	 * if (userForm.getPhotos() != null) { userInDB.setPhotos(userForm.getPhotos());
	 * }
	 * 
	 * userInDB.setFirstName(userForm.getFirstName());
	 * userInDB.setLastName(userForm.getLastName()); return userRepo.save(userInDB);
	 * }
	 */
	/*
	 * public boolean isEmailUnique(Integer id, String email) { User userByEmail =
	 * userRepo.getUserByEmail(email);
	 * 
	 * if (userByEmail == null) return true;
	 * 
	 * boolean isCreateNew = (id == null);
	 * 
	 * if(isCreateNew) { if (userByEmail != null) return false; }else {
	 * if(userByEmail.getId() != id) { return false; } }
	 * 
	 * return true; }
	 */

}
