package com.exam.competitor.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.exception.CourseNotFoundException;
import com.exam.competitor.repo.CourseRepository;

@Service
@Transactional
public class CourseService {

	@Autowired
	CourseRepository courseRepo;

	public static final int COURSE_PER_PAGE = 10;

	public List<Course> findAllCourseList() {

		return (List<Course>) courseRepo.findAll();
	}
	
	/*
	 * public Integer getMaxCourseId() {
	 * 
	 * return courseRepo.getMaxId(); }
	 */

		
	public Page<Course> listByPage(int paneNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum - 1, COURSE_PER_PAGE, sort);

		if (keyword != null) {
			return courseRepo.findAll(keyword, pageable);
		}
		return courseRepo.findAll(pageable);
	}

	public Course save(Course course) {

		return courseRepo.save(course);
	}

	public Course getCourseById(Integer id) throws CourseNotFoundException {

		try {
			return courseRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CourseNotFoundException("Could not find any course by ID:" + id);
		}
	}
	
	
	public void deleteCourse(Integer id) throws CourseNotFoundException {

		try {
			courseRepo.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new CourseNotFoundException("Could not find any course by ID:" + id);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		courseRepo.updateEnabledStatus(id, status);
	}
	
	public Set<Course> getCoursesByCustId(Set<Customer> cust) {
		return courseRepo.findByCustomersIn(cust);
	}

	
}
