package com.exam.competitor.admin.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.CourseType;
import com.exam.competitor.admin.exception.CourseNotFoundException;
import com.exam.competitor.admin.exception.CourseTypeNotFoundException;
import com.exam.competitor.admin.repo.CourseRepository;
import com.exam.competitor.admin.repo.CourseTypeRepository;

@Service
@Transactional
public class CourseTypeService {

	
	@Autowired
	CourseTypeRepository couseTypeRepo;


	public static final int COURSETYPE_PER_PAGE = 5;

	public List<CourseType> findAllCourseTypeList() {

		return (List<CourseType>) couseTypeRepo.findAll();
	}
	
	public CourseType getCouseTypeRepoByName(String name) {
		return couseTypeRepo.getCouseTypeRepoByName(name);
	}
	
	public boolean checkDuplicateCourseType(Integer id, String name) {
		CourseType userByEmail = couseTypeRepo.getCouseTypeRepoByName(name);
		
		if (userByEmail == null)  return true;
		
		boolean isCreateNew = (id == null);
		
		if(isCreateNew) {
			if (userByEmail != null) return false;
		}else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}

	public Page<CourseType> listByPage(int paneNum, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum - 1, COURSETYPE_PER_PAGE, sort);

		if (keyword != null) {
			return couseTypeRepo.findAll(keyword, pageable);
		}
		return couseTypeRepo.findAll(pageable);
	}

	public List<CourseType> findAllCourseType() {
		return (List<CourseType>) couseTypeRepo.findAll();
	}

	public CourseType save(CourseType course) {

		return couseTypeRepo.save(course);
	}

	public CourseType getCourseTypeById(Integer id) throws CourseTypeNotFoundException {

		try {
			return couseTypeRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CourseTypeNotFoundException("Could not find any course type by ID:" + id);
		}
	}
	
	
	public void deleteCourse(Integer id) throws CourseTypeNotFoundException {

		try {
			couseTypeRepo.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new CourseTypeNotFoundException("Could not find any course type by ID:" + id);
		}
	}
	
	
	
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
