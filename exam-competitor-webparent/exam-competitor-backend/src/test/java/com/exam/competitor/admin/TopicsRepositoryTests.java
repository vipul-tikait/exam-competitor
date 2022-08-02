package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.repo.TopicRepository;
import com.exam.competitor.admin.common.entity.Topic;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TopicsRepositoryTests {

	
	@Autowired
	private TopicRepository repo;
	
	/*
	 * @Autowired private TestEntityManager entityManager;
	 */	
	@Test
	public void testCreateTopic() {
			
		Topic topics = new Topic("Indian Culture & Modern Indian History");
		//Topics topics = new Topics("Indian Culture & Modern Indian History");
		
		Topic savedUser = repo.save(topics);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testListAllTopics() {
		Iterable<Topic> listuser = repo.findAll();
		listuser.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testgetTopicById() {
		Topic userVipul = repo.findById(1).get();
		System.out.println(userVipul);
		assertThat(userVipul).isNotNull();
	}
	
	
	
}

