
package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.repo.MainExamRepository;

@DataJpaTest

@AutoConfigureTestDatabase(replace = Replace.NONE)

@Rollback(false)
public class MainExamRepositoryTests {

	@Autowired
	private MainExamRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateMainExam() {

		MainExamType mExamType = entityManager.find(MainExamType.class, 1);
		MainExam mExam = new MainExam();
		mExam.setName("PSC");
		//mExam.setMainExamType(mExamType);
		
		MainExam savedUser = repo.save(mExam);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	/*
	 * @Test public void testListAllTopics() { Iterable<Topic> listuser =
	 * repo.findAll(); listuser.forEach(user -> System.out.println(user)); }
	 * 
	 * @Test public void testgetTopicById() { Topic userVipul =
	 * repo.findById(1).get(); System.out.println(userVipul);
	 * assertThat(userVipul).isNotNull(); }
	 */

}
