package com.exam.competitor.admin.security;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exam.competitor.admin.service.ZoopkanUserDetailsService;

@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new ZoopkanUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;	
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
		.antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin","Editor")

		.antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
			.hasAnyAuthority("Admin", "Editor", "Salesperson")
		.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
		.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")

		.antMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
		.antMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/menus/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/setting/**").hasAuthority("Admin")
		.antMatchers("/videos/**").hasAuthority("Admin")
		.antMatchers("/question-sets/**").hasAuthority("Admin")
		.antMatchers("/examLevels/**").hasAuthority("Admin")
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.permitAll()
		.and().logout().permitAll()
		.and().rememberMe()
		.key("AbcdeFGhijikl_1234567890")
		.tokenValiditySeconds(24*7*60*60);
	}

	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**",
				"/img/**","/scss/**","/css/**","/vendor/**","/js/**","**/**");
	}

	
}
