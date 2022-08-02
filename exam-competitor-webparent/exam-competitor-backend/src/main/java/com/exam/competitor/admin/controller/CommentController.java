package com.exam.competitor.admin.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.blog.Comment;
import com.exam.competitor.admin.common.entity.blog.Post;
import com.exam.competitor.admin.service.CommentService;
import com.exam.competitor.admin.service.PostService;
import com.exam.competitor.admin.service.UserService;

@Controller
public class CommentController {

	@Autowired private PostService postService;
	@Autowired private CommentService commentService;
	@Autowired private UserService userService;


    @GetMapping("/comment/edit/{id}")
    public String showComment(@PathVariable Long id, Model model, Principal principal) {

        // Just curious  what if we get username from Principal instead of SecurityContext
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }
        // the end of curiosity //

//        // get username of current logged in session user
//        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // find user by username
       // Optional<User> optionalBlogUser = this.blogUserService.getUserByEmail(authUsername);
        // find post by idkk
        Optional<Comment> postOptional = this.commentService.get(id);
        // if both optionals is present set user and post to a new comment and put former in the model
        if (postOptional.isPresent()) {
            Comment comment = new Comment();
            //.setPost(postOptional.get());
            //comment.setUser(postOptional.get().getUser());
            model.addAttribute("comment", comment);
            System.err.println("GET comment/{id}: " + comment + "/" + id); // for testing debugging purposes
            return "blogs/commentForm";
        } else {
            System.err.println("Could not find a post by id: " + id + " or user by logged in username: " + authUsername); // for testing debugging purposes
            return "error";
        }
    }
    
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id, Model model, Principal principal) {

        // Just curious  what if we get username from Principal instead of SecurityContext
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }
        // the end of curiosity //

//        // get username of current logged in session user
//        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        // find user by username
       // Optional<User> optionalBlogUser = this.blogUserService.getUserByEmail(authUsername);
        // find post by idkk
        commentService.delete(id);
        
    	
		System.out.println("principal ID:"+principal.getName());
		
		User uer = userService.getUserByEmail(principal.getName());
		
		List<Post> posts = this.postService.findAutherPost(uer.getId());
	    model.addAttribute("posts", posts);
		//model.addAttribute("blogList", blogService.listAll());
		return "blogs/posts";
		//return getAllBlogListByPage(1, model, "firstName", "asc", null);//"users";
	    
    }

    @PostMapping("/comment")
    public String validateComment(@ModelAttribute Comment comment, BindingResult bindingResult, SessionStatus sessionStatus) {
        System.err.println("POST comment: " + comment); // for testing debugging purposes
        if (bindingResult.hasErrors()) {
            System.err.println("Comment did not validate");
            return "commentForm";
        } else {
            this.commentService.save(comment);
            System.err.println("SAVE comment: " + comment); // for testing debugging purposes
            sessionStatus.setComplete();
            return "redirect:/post/" + comment.getPost().getId();
        }
    }

}
