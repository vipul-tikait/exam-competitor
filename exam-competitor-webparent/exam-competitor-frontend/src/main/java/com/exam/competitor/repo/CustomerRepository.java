package com.exam.competitor.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.exam.competitor.admin.common.entity.AuthenticationType;
import com.exam.competitor.admin.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);

	@Query("SELECT c FROM Customer c WHERE c.id = ?1")
	public Optional<Customer> findById(Integer id);

	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);

	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);

	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAuthenticationType(Integer customerId, AuthenticationType type);

	public Customer findByResetPasswordToken(String token);
	
	 

}
