/*
 * package com.exam.competitor.admin.service;
 * 
 * import java.util.HashSet; import java.util.List; import java.util.Set;
 * 
 * import javax.transaction.Transactional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service;
 * 
 * import com.exam.competitor.admin.common.entity.exam.ExamCategory; import
 * com.exam.competitor.admin.common.entity.exam.Quiz; import
 * com.exam.competitor.admin.repo.QuizRepository;
 * 
 * @Service
 * 
 * @Transactional public class QuizService {
 * 
 * @Autowired private QuizRepository quizRepository;
 * 
 * 
 * public Quiz addQuiz(Quiz quiz) { return this.quizRepository.save(quiz); }
 * 
 * 
 * public Quiz updateQuiz(Quiz quiz) { return this.quizRepository.save(quiz); }
 * 
 * 
 * public List<Quiz> getQuizzes() { return (List<Quiz>)
 * this.quizRepository.findAll(); }
 * 
 * 
 * public Quiz getQuiz(Integer quizId) { return
 * this.quizRepository.findById(quizId).get(); }
 * 
 * 
 * public void deleteQuiz(Integer quizId) {
 * this.quizRepository.deleteById(quizId); }
 * 
 * 
 * public List<Quiz> getQuizzesOfCategory(ExamCategory category) { return
 * this.quizRepository.findByExamCategory(category); }
 * 
 * 
 * //get active quizzes
 * 
 * 
 * public List<Quiz> getActiveQuizzes() { return
 * this.quizRepository.findByActive(true); }
 * 
 * 
 * public List<Quiz> getActiveQuizzesOfCategory(ExamCategory c) { return
 * this.quizRepository.findByExamCategoryAndActive(c, true); }
 * 
 * }
 */