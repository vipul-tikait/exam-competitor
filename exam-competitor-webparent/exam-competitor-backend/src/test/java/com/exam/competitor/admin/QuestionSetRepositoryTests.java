
package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.repo.QuestionSetRepository;

@DataJpaTest

@AutoConfigureTestDatabase(replace = Replace.NONE)

@Rollback(false)
public class QuestionSetRepositoryTests {

	@Autowired
	private QuestionSetRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateQuestion() {

		Question question1 = entityManager.find(Question.class, 1);
		Question question2 = entityManager.find(Question.class, 2);

		List<Question> qlist = new ArrayList<>();

		qlist.add(question1);
		qlist.add(question2);
		QuestionSet q1 = new QuestionSet();

		q1.setQuestions(qlist);
		QuestionSet savedQset = repo.save(q1);

		assertThat(savedQset.getId()).isGreaterThan(0);
	}

	/*
	 * @Test public void testListAllTopics() { Iterable<Topics> listuser =
	 * repo.findAll(); listuser.forEach(user -> System.out.println(user)); }
	 * 
	 * @Test public void testgetTopicById() { Topics userVipul =
	 * repo.findById(1).get(); System.out.println(userVipul);
	 * assertThat(userVipul).isNotNull(); }
	 */
}
