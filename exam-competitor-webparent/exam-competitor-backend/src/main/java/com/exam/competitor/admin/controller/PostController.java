package com.exam.competitor.admin.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.blog.Post;
import com.exam.competitor.admin.service.PostService;
import com.exam.competitor.admin.service.UserService;

@Controller
@SessionAttributes("post")
public class PostController {

	@Autowired private  PostService postService;
	@Autowired private  UserService userService;

   
	/*
	 * @GetMapping("/post/{id}") public String getPost(@PathVariable Long id, Model
	 * model, Principal principal) {
	 * 
	 * // Just curious what if we get username from Principal instead of
	 * SecurityContext String authUsername = "anonymousUser"; if (principal != null)
	 * { authUsername = principal.getName(); }
	 * 
	 * // the end of curiosity //
	 * 
	 * // // get username of current logged in session user // String authUsername =
	 * SecurityContextHolder.getContext().getAuthentication().getName();
	 * 
	 * // find post by id Optional<Post> optionalPost =
	 * this.postService.getById(id); // if post exist put it in model if
	 * (optionalPost.isPresent()) { Post post = optionalPost.get();
	 * model.addAttribute("post", post); // Check if current logged in user is owner
	 * and let view template know to take according actions if
	 * (authUsername.equals(post.getUser().getEmail())) {
	 * model.addAttribute("isOwner", true); } return "blogs/post"; } else { return
	 * "404"; } }
	 */
	/*
	 * @GetMapping("/createNewPost") public String createNewPost(Model model,
	 * Principal principal) {
	 * 
	 * // Just curious what if we get username from Principal instead of
	 * SecurityContext String authUsername = "anonymousUser"; if (principal != null)
	 * { authUsername = principal.getName(); } // the end of curiosity //
	 * 
	 * // // get username of current logged in session user // String authUsername =
	 * SecurityContextHolder.getContext().getAuthentication().getName();
	 * 
	 * // find user by username User optionalBlogUser =
	 * this.userService.getUserByEmail(authUsername); // set user to post and put
	 * former in model if (optionalBlogUser !=null ) { Post post = new Post();
	 * post.setUser(optionalBlogUser); model.addAttribute("post", post); return
	 * "postForm"; } else { return "error"; } }
	 */
	@PostMapping("/post/save")
	public String savePost(Post post, Principal principal) {
		System.err.println("POST post: " + post); // for testing debugging purposes
		/*
		 * if (bindingResult.hasErrors()) { System.err.println("Post did not validate");
		 * return "postForm"; }
		 */ // Save post if all good 
		
		User user  = userService.getUserByEmail(principal.getName());
		post.setUser(user);
		this.postService.save(post);
		System.err.println("SAVE post: " + post); // for testing debugging purposes
		//sessionStatus.setComplete();
		return "redirect:/post/" + post.getId();
	} 

	@GetMapping("/post/new")
	public String createNewPost(Model model) {
		/*
		 * if (bindingResult.hasErrors()) { System.err.println("Post did not validate");
		 * return "postForm"; }
		 */ // Save post if all good 
		//sessionStatus.setComplete();
		/*
		 * System.out.println("New Post"); Post post = new Post();
		 * model.addAttribute("post", post); model.addAttribute("pageTitle",
		 * "Create New Post");
		 */
		Post post = new Post();
		 model.addAttribute("post", post);
		return "blogs/postForm";
	} 

    @GetMapping("editPost/{id}")
    public String editPost(@PathVariable Integer id, Model model, Principal principal) {
        // Just curious  what if we get username from Principal instead of SecurityContext
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }
        // the end of curiosity //

//        // get username of current logged in session user
//        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // find post by id
        Optional<Post> optionalPost = this.postService.getById(id);
        // Check if current logged in user is an owner and so has the right for modifications to happen
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Check if current logged in user is owner
            if (authUsername.equals(post.getUser().getEmail())) {
                model.addAttribute("post", post);
                System.err.println("EDIT post: " + post); // for testing debugging purposes
                return "blogs/postForm";
            } else {
                System.err.println("Current User has no permissions to edit anything on post by id: " + id); // for testing debugging purposes
                return "403";
            }
        } else {
            System.err.println("Could not find a post by id: " + id); // for testing debugging purposes
            return "error";
        }
    }

	/*
	 * @GetMapping("/deletePost/{id}") public String deletePost(@PathVariable
	 * Integer id, Principal principal) {
	 * 
	 * // Just curious what if we get username from Principal instead of
	 * SecurityContext String authUsername = "anonymousUser"; if (principal != null)
	 * { authUsername = principal.getName(); } // the end of curiosity //
	 * 
	 * // // get username of current logged in session user // String authUsername =
	 * SecurityContextHolder.getContext().getAuthentication().getName();
	 * 
	 * // find post by id Optional<Post> optionalPost =
	 * this.postService.getById(id); // Check if current logged in user is an owner
	 * and so has the right for modifications to happen if
	 * (optionalPost.isPresent()) { Post post = optionalPost.get(); // Check if
	 * current logged in user is owner if
	 * (authUsername.equals(post.getUser().getEmail())) { // if so then it is safe
	 * to remove post from database this.postService.delete(id);
	 * System.err.println("DELETED post: " + post); // for testing debugging
	 * purposes return "redirect:/"; } else { System.err.
	 * println("Current User has no permissions to edit anything on post by id: " +
	 * id); // for testing debugging purposes return "403"; } } else {
	 * System.err.println("Could not find a post by id: " + id); // for testing
	 * debugging purposes return "error"; } }
	 */

}
