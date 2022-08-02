
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
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.repo.ExamRepository;

@DataJpaTest

@AutoConfigureTestDatabase(replace = Replace.NONE)

@Rollback(false)
public class ExamRepositoryTests {

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateExam() {

		/*
		 * MainExam mX = entityManager.find(MainExam.class, 1);
		 * 
		 * 
		 * Exam exam1 = new Exam(); exam1.setName("UPSC"); exam1.setMainExams(mX);
		 * 
		 * Exam exam2 = new Exam(); exam2.setName("MPSC"); exam2.setMainExams(mX);
		 * 
		 * Exam savedUser1 = examRepository.save(exam1); Exam savedUser =
		 * examRepository.save(exam2); assertThat(savedUser.getId()).isGreaterThan(0);
		 */
	}

	
}
