package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.repo.ExamLevelRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ExamLevelRepositoryTests {

	@Autowired  private ExamLevelRepository examLevelRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateExamLevel() {
		
		Exam e1 = entityManager.find(Exam.class, 1);
		
		Exam e2 = entityManager.find(Exam.class, 2);
		
		  ExamLevel el1= new ExamLevel(); 
		  el1.setName("Mains");
		 // el1.setExam(e1);
		  
		  
		  ExamLevel savedEl1 =  examLevelRepository.save(el1);
		  assertThat(savedEl1.getId()).isGreaterThan(0); 
	}
	
	 

}
