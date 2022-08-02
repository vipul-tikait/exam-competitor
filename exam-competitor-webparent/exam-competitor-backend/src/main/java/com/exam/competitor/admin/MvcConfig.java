package com.exam.competitor.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.exam.competitor.admin.paging.PagingAndSortingArgumentResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {;
	
	    exposeDirectory("user-photos", registry);
	    exposeDirectory("../category-images", registry);
	    
	    exposeDirectory("../brands-images", registry);
	    exposeDirectory("../product-images", registry);
	    exposeDirectory("../site-logo", registry);
	    
	    exposeDirectory("../exams-images", registry);
	    exposeDirectory("../topic-images", registry);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}
	
	private void exposeDirectory(String pattern, ResourceHandlerRegistry registry) {
		Path path= Paths.get(pattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pattern.replace("../", "")+"/**";
		registry.addResourceHandler(logicalPath).addResourceLocations("file:"+absolutePath+"/");
	}
	
}
