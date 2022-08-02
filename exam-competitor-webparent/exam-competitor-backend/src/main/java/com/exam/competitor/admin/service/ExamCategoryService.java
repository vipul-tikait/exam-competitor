/*
 * package com.exam.competitor.admin.service;
 * 
 * 
 * import java.util.List;
 * 
 * import javax.transaction.Transactional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service;
 * 
 * import com.exam.competitor.admin.common.entity.exam.ExamCategory; import
 * com.exam.competitor.admin.repo.ExamCategoryRepository;
 * 
 * @Service
 * 
 * @Transactional public class ExamCategoryService {
 * 
 * @Autowired private ExamCategoryRepository examCategoryRepository;
 * 
 * 
 * public ExamCategory addExamCategory(ExamCategory ExamCategory) { return
 * this.examCategoryRepository.save(ExamCategory); }
 * 
 * 
 * public ExamCategory updateExamCategory(ExamCategory ExamCategory) { return
 * this.examCategoryRepository.save(ExamCategory); }
 * 
 * 
 * public List<ExamCategory> getCategories() { return (List<ExamCategory>)
 * this.examCategoryRepository.findAll(); }
 * 
 * 
 * public ExamCategory getExamCategory(Integer examCategoryId) { return
 * examCategoryRepository.findById(examCategoryId).get(); }
 * 
 * 
 * public void deleteExamCategory(Integer ExamCategoryId) { ExamCategory
 * ExamCategory = new ExamCategory(); ExamCategory.setCid(ExamCategoryId);
 * this.examCategoryRepository.delete(ExamCategory); }
 * 
 * }
 */