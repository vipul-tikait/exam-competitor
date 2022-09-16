package com.exam.competitor;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ExamCompetitorFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamCompetitorFrontendApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*	@Bean
	  public ServletWebServerFactory servletContainer() {
	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
	      @Override
	      protected void postProcessContext(Context context) {
	    	SecurityConstraint securityConstraint = new SecurityConstraint();
	        securityConstraint.setUserConstraint("CONFIDENTIAL");
	        SecurityCollection collection = new SecurityCollection();
	        collection.addPattern("/*");
	        securityConstraint.addCollection(collection);
	        context.addConstraint(securityConstraint);
	      }
	    };
	    tomcat.addAdditionalTomcatConnectors(getHttpConnector());
	    return tomcat;
	  }
*/
		/*
		 * private Connector getHttpConnector() { Connector connector = new
		 * Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		 * connector.setScheme("http"); connector.setPort(8090);
		 * connector.setSecure(false); connector.setRedirectPort(18090); return
		 * connector; }
		 */

}
