
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

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.repo.MainExamTypeRepository;

@DataJpaTest

@AutoConfigureTestDatabase(replace = Replace.NONE)

@Rollback(false)
public class MainExamTypeRepositoryTests {

	@Autowired
	private MainExamTypeRepository examRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateMainExamType() {

		// MainExam me = entityManager.find(MainExam.class, entityManager);

		// Set<MainExam> meset = new HashSet<>();
		// meset.add(me);

		MainExamType met = new MainExamType();
		met.setName("CS");

		MainExamType savedUser = examRepository.save(met);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllTopics() {
		Iterable<MainExamType> listuser = examRepository.findAll();
		for (MainExamType mainExamType : listuser) {
			System.out.println("Main Exam type:"+mainExamType.getName());
			for (MainExam mainExam : mainExamType.getMainExams()) {
				System.out.println("	Main Exam:"+mainExam.getName());
				for (Exam exam : mainExam.getExams()) {
					System.out.println("		Exam:"+exam.getName());
					for (ExamLevel examLevel : exam.getExamLevels()) {
						System.out.println("		ExamLevel:"+examLevel.getName());
							
					}
				}
			}
			
		}
		
	}

}
