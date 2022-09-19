package com.exam.competitor.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exam.competitor.security.outh.CustomerOAuth2UserService;
import com.exam.competitor.security.outh.DatabaseLoginSuccessHandler;
import com.exam.competitor.security.outh.OAuth2LoginSuccessHandler;


@Configurable
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired private CustomerOAuth2UserService oAuth2UserService;
	@Autowired private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
	@Autowired PasswordEncoder passwordEncoder;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				/*
				 * .antMatchers("/account_details", "/update_account_details", "/orders/**",
				 * "/cart", "/address_book/**", "/checkout", "/place_order", "/reviews/**",
				 * "/process_paypal_order", "/write_review/**", "/post_review").authenticated()
			.	 */
		.antMatchers("/", "/find", "/login", "/signup", "/error", "/login-error","/images/**","/js/**","/webjars/**","**/**").permitAll() //All user permissions
    	
		.antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
		.antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin","Editor")

		.antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
			.hasAnyAuthority("Admin", "Editor", "Salesperson")
		.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
			.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
		.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")

		
		//.antMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
		.antMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/menus/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/setting/**").hasAuthority("Admin")
		.antMatchers("/videos/**").hasAuthority("Admin")
		.antMatchers("/customers").hasAuthority("Admin")
		//.antMatchers("/courses/**").hasAuthority("Admin")
		.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginSuccessHandler)
				.failureUrl("/login-error")      //URL when authentication fails
            	.defaultSuccessUrl("/")
				.permitAll()
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oAuth2UserService)
				.and()
				.successHandler(oauth2LoginHandler)
			.and()
			.logout().permitAll()
			.and()
			.rememberMe()
				.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
				//.tokenValiditySeconds(14 * 24 * 60 * 60)
				.tokenValiditySeconds(10)
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			;			
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**","**/**");
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder);

		return authProvider;
	}
	


}
