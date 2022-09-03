package com.exam.competitor.service;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.exception.QuestionSetNotFoundException;
import com.exam.competitor.repo.QuestionSetRepository;
import com.exam.competitor.repo.RoleRepository;


@Service
@Transactional
public class QuestionSetService {

	@Autowired
	QuestionSetRepository questionSetRepository;
	
	@Autowired
	RoleRepository roleRepo;
	
	public static final int QUESTIONSET_PER_PAGE = 2;
	
	public List<QuestionSet> findAllQuestionSetList(){
		
		return (List<QuestionSet>) questionSetRepository.findAll();
	}
	

	
	public Page<QuestionSet> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, QUESTIONSET_PER_PAGE,sort);
		
		if (keyword != null) {
			return questionSetRepository.findAll(keyword, pageable);	
		}
		return questionSetRepository.findAll(pageable);
	}


	public QuestionSet save(QuestionSet questionSet) {
			
		return questionSetRepository.save(questionSet);
	}
	
	
	public boolean isIdUnique(Integer id) {
		QuestionSet userByEmail = questionSetRepository.findById(id).get();
		
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


	public QuestionSet getQuestionSetById(Integer id) throws QuestionSetNotFoundException {
		
		try {
			return questionSetRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new QuestionSetNotFoundException("Could not find any questionSet by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws QuestionSetNotFoundException {
		
		try {
			questionSetRepository.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new QuestionSetNotFoundException("Could not find any questionSet by ID:"+id);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		questionSetRepository.updateEnabledStatus(id, status);
	}
	
	public long getQsetNextCount() {
		return questionSetRepository.count();
	}
	
	public List<QuestionSet> findQuestionSetByExamLevel(ExamLevel examLevel){
		return questionSetRepository.findByExamLevel(examLevel);
	}
}
