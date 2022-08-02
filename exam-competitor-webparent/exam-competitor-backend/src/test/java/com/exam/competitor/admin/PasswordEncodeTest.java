package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncodeTest {
	
	@Test
	public void testEncodePassword() {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String rawpassword = "new2022";
		String encodedPassword = bCryptPasswordEncoder.encode(rawpassword);
		boolean match = bCryptPasswordEncoder.matches(rawpassword, encodedPassword);
		System.out.println(match);
		System.out.println(encodedPassword);
		assertThat(match);
		
	}

}
