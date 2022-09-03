package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.repo.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreatFirstRole() {
		Role roleAdmin = new Role("Moderator", "Manage courses");
		Role savedRole = repo.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
		
		
	}

	/*
	 * @Test public void testCretRestRole() { Role role1 = new Role("Salesperson",
	 * "Manage product, price"); Role role2 = new Role("Editor",
	 * "Manage Categories, Brands"); Role role3 = new Role("Shipper",
	 * "View products, orders, update order status"); Role role4 = new
	 * Role("Assistant", "Manage Question answer");
	 * repo.saveAll(List.of(role1,role2,role3, role4));
	 * 
	 * }
	 */
}
