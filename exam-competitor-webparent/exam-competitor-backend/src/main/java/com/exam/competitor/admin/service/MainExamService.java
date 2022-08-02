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
import com.exam.competitor.admin.exception.MainExamNotFoundException;
import com.exam.competitor.admin.repo.MainExamRepository;


@Service
@Transactional
public class MainExamService {

	@Autowired  MainExamRepository mainExamRepository;
	
	public static final int MAIN_EXAM_TYPE_PER_PAGE = 10;
	
	public List<MainExam> findAllMainExamList(){
		
		return (List<MainExam>) mainExamRepository.findAll();
	}
	
	public MainExam getMainExamByName(String name) {
		return mainExamRepository.findByName(name);
	}
	
	public List<MainExam> findAllMainExams(){
		return (List<MainExam>) mainExamRepository.findAll();
	}
	
	public Page<MainExam> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, MAIN_EXAM_TYPE_PER_PAGE,sort);
		
		if (keyword != null) {
			return mainExamRepository.findAll(keyword, pageable);	
		}
		return mainExamRepository.findAll(pageable);
	}


	public MainExam save(MainExam user) {
		
		return mainExamRepository.save(user);
	}
	
	
	public boolean isMainExamNameUnique(Integer id, String name) {
		MainExam userByEmail = mainExamRepository.findByName(name);
		
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


	public MainExam getMainExamById(Integer id) throws MainExamNotFoundException {
		
		try {
			return mainExamRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new MainExamNotFoundException("Could not find any MainExam by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws MainExamNotFoundException {
		
		try {
			mainExamRepository.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new MainExamNotFoundException("Could not find any MainExam by ID:"+id);
		}
	}

	public List<MainExam> getMainExamByMainExamType(Integer id){
		return mainExamRepository.getMainExamByMainExamType(id);
	}
}
