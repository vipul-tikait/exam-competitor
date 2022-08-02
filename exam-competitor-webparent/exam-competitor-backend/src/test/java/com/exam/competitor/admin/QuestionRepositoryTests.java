package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.repo.QuestionRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionRepositoryTests {

	
	@Autowired
	private QuestionRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateQuestion() {
		
		Topic topics = entityManager.find(Topic.class, 1);
		
		Question question = new Question("Questi 3", null, null, null, false, topics);
			
		//Topics topics = new Topics("Indian Culture & Modern Indian History");
		//Topics topics = new Topics("Indian Culture & Modern Indian History");
		
		Question savedUser = repo.save(question);
		assertThat(savedUser.getId()).isGreaterThan(0);
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

