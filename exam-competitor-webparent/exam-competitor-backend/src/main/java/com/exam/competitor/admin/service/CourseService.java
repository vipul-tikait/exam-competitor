package com.exam.competitor.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.CourseType;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.exception.CourseNotFoundException;
import com.exam.competitor.admin.repo.CourseRepository;
import com.exam.competitor.admin.repo.CourseTypeRepository;

@Service
@Transactional
public class CourseService {

	@Autowired
	CourseRepository courseRepo;

	@Autowired
	CourseTypeRepository couseTypeRepo;


	public static final int COURSE_PER_PAGE = 10;

	public List<Course> findAllCourseList() {

		return (List<Course>) courseRepo.findAll();
	}
	
	public Integer getMaxCourseId() {

		return  courseRepo.getMaxId();
	}

	/*
	 * public User getUserByEmail(String email) { return
	 * userRepo.getUserByEmail(email); }
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

	public List<CourseType> findAllCourseType() {
		return (List<CourseType>) couseTypeRepo.findAll();
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
