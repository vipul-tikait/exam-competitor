package com.exam.competitor.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.exam.competitor.admin.common.entity.blog.Post;
import com.exam.competitor.category.CategoryService;
import com.exam.competitor.service.PostService;

@Controller
public class MainController {

//@Autowired private CategoryService categoryService;
@Autowired private PostService postService;
	
	@GetMapping("/")
	public String viewHomePage(Model model, Principal principal) {
		//List<Category> listCategories = categoryService.listNoChildrenCategories();
		//model.addAttribute("listCategories", listCategories);
		List<Post> posts = this.postService.getAll();
		/*
		 * if (!posts.isEmpty()) { Optional<Post> first = posts.stream().findFirst();
		 * Post postfirst = first.get(); model.addAttribute("latestPost", postfirst);
		 * System.out.println("FIRST:"+postfirst.getTitle()); posts.remove(postfirst); }
		 */
		//<!-- 			<div th:utext="${#strings.abbreviate(latestPost.body,200)}"></div> -->
		//<p th:utext="${#strings.length(latestPost.body)>1600 ? #strings.substring(latestPost.body,0,1600) + '...':latestPost.body} "></p>
		System.out.println("principal:"+principal);
		//model.addAttribute("posts", posts);
	    model.addAttribute("posts", posts);
		return "blogs/posts";
	}
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login2";
		}
		
		return "redirect:/";
	}	
	
}
