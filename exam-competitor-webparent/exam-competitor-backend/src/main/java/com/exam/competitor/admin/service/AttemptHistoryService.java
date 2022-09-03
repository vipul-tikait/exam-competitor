package com.exam.competitor.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.exception.AttemptHistoryNotFoundException;
import com.exam.competitor.admin.repo.AttemptHistoryRepository;

@Service
@Transactional
public class AttemptHistoryService {

	@Autowired
	AttemptHistoryRepository attemptHistoryRepository;

	public static final int COURSE_PER_PAGE = 10;

	public List<AttemptHistory> findAllAttemptHistoryList() {

		return (List<AttemptHistory>) attemptHistoryRepository.findAll();
	}
	
	/*
	 * public Integer getMaxCourseId() {
	 * 
	 * return attemptHistoryRepository.getMaxId(); }
	 */

		
	public Page<AttemptHistory> listByPage(int paneNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum - 1, COURSE_PER_PAGE, sort);

		if (keyword != null) {
			return attemptHistoryRepository.findAll(keyword, pageable);
		}
		return attemptHistoryRepository.findAll(pageable);
	}
	
	public Page<AttemptHistory> listByPage(int paneNum, String sortField, String sortDir, String keyword, User user) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum - 1, COURSE_PER_PAGE, sort);

		if (keyword != null) {
			return attemptHistoryRepository.findAllOfUser(keyword, pageable, user);
		}
		return attemptHistoryRepository.findAllOfUser(pageable,user);
	}

	public AttemptHistory save(AttemptHistory attemptHistory) {

		return attemptHistoryRepository.save(attemptHistory);
	}

	public AttemptHistory getCourseById(Integer id) throws AttemptHistoryNotFoundException {

		try {
			return attemptHistoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new AttemptHistoryNotFoundException("Could not find any Attempt by ID:" + id);
		}
	}
	
	
	public void deleteAttemptHistory(Integer id) throws AttemptHistoryNotFoundException {

		try {
			attemptHistoryRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new AttemptHistoryNotFoundException("Could not find any attempt by ID:" + id);
		}
	}
	
	public Optional<AttemptHistory> getAttemptById(Integer id) {
		return attemptHistoryRepository.findById(id);
		
	}
	public void updateEnabledStatus(Integer id, boolean status) {
		attemptHistoryRepository.updateEnabledStatus(id, status);
	}
	
	public void updateAttemptStatus(Integer id, String status) {
		attemptHistoryRepository.updateAttemptStatus(id, status);
	}
	
	
	public Set<AttemptHistory> getByCustomerAndCourse(Customer customer, Course course) {
		return attemptHistoryRepository.findByCustomerAndCourse(customer, course);
	}

	
}
