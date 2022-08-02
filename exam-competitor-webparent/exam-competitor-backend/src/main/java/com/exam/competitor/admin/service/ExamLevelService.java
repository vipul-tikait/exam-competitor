package com.exam.competitor.admin.service;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.exception.ExamLevelNotFoundException;
import com.exam.competitor.admin.repo.ExamLevelRepository;
import com.exam.competitor.admin.repo.ExamRepository;


@Service
@Transactional
public class ExamLevelService {

	@Autowired  ExamLevelRepository examLevelRepository;
	@Autowired  ExamRepository examRepository;
	
	public static final int EXAM_LEVEL_PER_PAGE = 10;
	
	public List<ExamLevel> findAllExamLevelList(){
		
		return (List<ExamLevel>) examLevelRepository.findAll();
	}
	
	public ExamLevel getExamLevelByName(String name) {
		return examLevelRepository.findByName(name);
	}
	
	public List<ExamLevel> findAllExamLevels(){
		return (List<ExamLevel>) examLevelRepository.findAll();
	}
	
	public Page<ExamLevel> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, EXAM_LEVEL_PER_PAGE,sort);
		
		if (keyword != null) {
			return examLevelRepository.findAll(keyword, pageable);	
		}
		return examLevelRepository.findAll(pageable);
	}


	public ExamLevel save(ExamLevel user) {
		
		return examLevelRepository.save(user);
	}
	
	
	public boolean isExamLevelNameUnique(Integer id, String name) {
		ExamLevel userByEmail = examLevelRepository.findByName(name);
		
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


	public ExamLevel getExamLevelById(Integer id) throws ExamLevelNotFoundException {
		
		try {
			return examLevelRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new ExamLevelNotFoundException("Could not find any ExamLevel by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws ExamLevelNotFoundException {
		
		try {
			examLevelRepository.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new ExamLevelNotFoundException("Could not find any ExamLevel by ID:"+id);
		}
	}
	public List<ExamLevel> getExamLevelByExam(Integer id){
		return examLevelRepository.getExamLevelByExam(id);
	}

}
