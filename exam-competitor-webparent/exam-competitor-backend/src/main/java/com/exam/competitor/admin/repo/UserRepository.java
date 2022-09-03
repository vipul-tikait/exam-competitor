package com.exam.competitor.admin.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

	@Query("select u from User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	public void deleteById(Integer id);
	
	@Query("update User u set u.enabled = ?2 where u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
	@Query("select u from User u where CONCAT(u.email, ' ',u.firstName, ' ', u.lastName) like %?1%") 
	public Page<User> findAll(String keyword, Pageable pageable);
	
	List<User> findByRoles(Role role);
}
