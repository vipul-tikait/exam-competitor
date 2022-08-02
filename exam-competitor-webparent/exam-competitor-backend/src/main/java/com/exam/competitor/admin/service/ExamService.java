package com.exam.competitor.admin.service;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.exception.ExamNotFoundException;
import com.exam.competitor.admin.repo.ExamRepository;


@Service
@Transactional
public class ExamService {

	@Autowired  ExamRepository examRepository;
	
	public static final int EXAM_PER_PAGE = 10;
	
	public List<Exam> findAllExamList(){
		
		return (List<Exam>) examRepository.findAll();
	}
	
	public Exam getExamByName(String name) {
		return examRepository.findByName(name);
	}
	
	public List<Exam> findAllExams(){
		return (List<Exam>) examRepository.findAll();
	}
	
	public Page<Exam> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, EXAM_PER_PAGE,sort);
		
		if (keyword != null) {
			return examRepository.findAll(keyword, pageable);	
		}
		return examRepository.findAll(pageable);
	}


	public Exam save(Exam user) {
		
		return examRepository.save(user);
	}
	
	
	public boolean isExamNameUnique(Integer id, String name) {
		Exam userByEmail = examRepository.findByName(name);
		
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


	public Exam getExamById(Integer id) throws ExamNotFoundException {
		
		try {
			return examRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new ExamNotFoundException("Could not find any Exam by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws ExamNotFoundException {
		
		try {
			examRepository.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new ExamNotFoundException("Could not find any Exam by ID:"+id);
		}
	}
	public List<Exam> getExamByMainExam(Integer id){
		return examRepository.getExamByMainExam(id);
	}
}
