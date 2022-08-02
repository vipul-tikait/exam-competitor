package com.exam.competitor.admin.service;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.MainExamTypeNotFoundException;
import com.exam.competitor.admin.repo.MainExamRepository;
import com.exam.competitor.admin.repo.MainExamTypeRepository;


@Service
@Transactional
public class MainExamTypeService {

	@Autowired  MainExamTypeRepository mainExamTypeRepository;
	@Autowired MainExamRepository mainExamRepository;
	
	public static final int MAIN_EXAM_TYPE_PER_PAGE = 10;
	
	public List<MainExamType> findAllMainExamTypeList(){
		
		return (List<MainExamType>) mainExamTypeRepository.findAll();
	}
	
	public MainExamType getMainExamTypeByName(String name) {
		return mainExamTypeRepository.findByName(name);
	}
	
	public List<MainExam> findAllMainExams(){
		return (List<MainExam>) mainExamRepository.findAll();
	}
	
	public Page<MainExamType> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, MAIN_EXAM_TYPE_PER_PAGE,sort);
		
		if (keyword != null) {
			return mainExamTypeRepository.findAll(keyword, pageable);	
		}
		return mainExamTypeRepository.findAll(pageable);
	}


	public MainExamType save(MainExamType user) {
		
		return mainExamTypeRepository.save(user);
	}
	
	
	public boolean isMainExamTypeNameUnique(Integer id, String name) {
		MainExamType userByEmail = mainExamTypeRepository.findByName(name);
		
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


	public MainExamType getMainExamTypeById(Integer id) throws MainExamTypeNotFoundException {
		
		try {
			return mainExamTypeRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new MainExamTypeNotFoundException("Could not find any MainExamType by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws MainExamTypeNotFoundException {
		
		try {
			mainExamTypeRepository.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new MainExamTypeNotFoundException("Could not find any MainExamType by ID:"+id);
		}
	}
	
	/*
	 * public List<Object[]> getMainExamByMainExamType(Integer id){ return
	 * mainExamTypeRepository.getMainExamByMainExamType(id); }
	 */
	
	
}
