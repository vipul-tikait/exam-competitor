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
import com.exam.competitor.admin.repo.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
	
		Role roleAdmin = entityManager.find(Role.class,1);
		User userNM = new User("vipul@Gmail.com","vipul2020","Vipul", "Kumar");
		userNM.addRole(roleAdmin);
		
		User savedUser = repo.save(userNM);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithManyRole() {
	
		User userNM = new User("Rahul@Gmail.com","rahul2020","Rahul", "Kumar");
		userNM.addRole(new Role(3));
		userNM.addRole(new Role(5));
		
		User savedUser = repo.save(userNM);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listuser = repo.findAll();
		listuser.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testgetUserById() {
		User userVipul = repo.findById(1).get();
		System.out.println(userVipul);
		assertThat(userVipul).isNotNull();
	}
	
	@Test
	public void testGetUserByEmail() {
		
		User user = repo.getUserByEmail("abcd@gmail.com");
		
		assertThat(user).isNotNull();
		
	}
	
	@Test
	public void testCountById() {
		Integer id = 100;
		
		Long count = repo.countById(id);
		assertThat(count).isNotNull().isGreaterThan(0);
	}
}

