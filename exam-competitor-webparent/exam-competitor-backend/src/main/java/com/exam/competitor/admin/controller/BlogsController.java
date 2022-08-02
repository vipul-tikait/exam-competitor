package com.exam.competitor.admin.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.blog.Post;
import com.exam.competitor.admin.service.BlogService;
import com.exam.competitor.admin.service.PostService;
import com.exam.competitor.admin.service.UserService;

@Controller
public class BlogsController {

	@Autowired private PostService postService;
	@Autowired private UserService userService;

	@GetMapping("/post/all")
	public String getAllBlogList(Model model) {
		
		
		
		List<Post> posts = this.postService.getAll();
	    model.addAttribute("posts", posts);
		//model.addAttribute("blogList", blogService.listAll());
		return "blogs/posts";
		//return getAllBlogListByPage(1, model, "firstName", "asc", null);//"users";
		
		
	}
	
	@GetMapping("/post/mine")
	public String getAllBlogListMine(Model model, Principal principal) {
		
		
		System.out.println("principal ID:"+principal.getName());
		
		User uer = userService.getUserByEmail(principal.getName());
		
		List<Post> posts = this.postService.findAutherPost(uer.getId());
	    model.addAttribute("posts", posts);
		//model.addAttribute("blogList", blogService.listAll());
		return "blogs/posts";
		//return getAllBlogListByPage(1, model, "firstName", "asc", null);//"users";
		
		
	}

	  @GetMapping("/post/{id}")
	    public String getPost(@PathVariable Integer id, Model model, Principal principal) {

	        // Just curious  what if we get username from Principal instead of SecurityContext
	        String authUsername = "anonymousUser";
	        if (principal != null) {
	            authUsername = principal.getName();
	        }
	        
	        // the end of curiosity //

//	        // get username of current logged in session user
//	        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();

	        // find post by id
	        Optional<Post> optionalPost = this.postService.getById(id);
	        // if post exist put it in model
	        if (optionalPost.isPresent()) {
	            Post post = optionalPost.get();
	            model.addAttribute("post", post);
	            // Check if current logged in user is owner and let view template know to take according actions
	            if (authUsername.equals(post.getUser().getEmail())) {
	                model.addAttribute("isOwner", true);
	            }
	            return "blogs/post";
	        } else {
	            return "404";
	        }
	    }
	
	  @GetMapping("/post/delete/{id}")
	    public String deletePost(@PathVariable Integer id, Model model, Principal principal,Authentication authentication,
	    		HttpServletRequest request) {

	        // Just curious  what if we get username from Principal instead of SecurityContext
	        String authUsername = "anonymousUser";
	        if (principal != null) {
	            authUsername = principal.getName();
	        }
	        List<Post> optionalPost = new ArrayList<>();
	        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Admin")) && (request.getRequestURI().equalsIgnoreCase("post/all"))) {
	        	 optionalPost = this.postService.getAll();
	  	       
			} else {
				 optionalPost = this.postService.findAutherPost(id);
			       
			}
	        
	        if (!optionalPost.isEmpty()) {
	            
	            this.postService.delete(id);
	            User uer = userService.getUserByEmail(principal.getName());
				
				List<Post> posts = this.postService.findAutherPost(uer.getId());
			    model.addAttribute("posts", posts);
				//model.addAttribute("blogList", blogService.listAll());
				return "redirect:/";
	            
	        } else {
	            return "404";
	        }
	        
	        
			
			//return getAllBlogListByPage(1, model, "firstName", "asc", null);//"users";
	    }
	/*
	 * @GetMapping("/blogs/page/{pageNum}") public String
	 * getAllBlogListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
	 * 
	 * @Param("sortField") String sortField, @Param("sortDir") String sortDir,
	 * 
	 * @Param("keyword") String keyword) {
	 * 
	 * Page<User> page = blogService.listByPage(pageNum,sortField,sortDir,keyword);
	 * List<User> lstUser = page.getContent();
	 * 
	 * long startCount = (pageNum-1) * userService.USER_PER_PAGE + 1; long endCount
	 * = startCount + userService.USER_PER_PAGE - 1; if (endCount >
	 * page.getTotalElements()) { endCount = page.getTotalElements(); }
	 * 
	 * String revSortDir = sortDir.equals("asc")?"dsc":"asc";
	 * model.addAttribute("totalPage", page.getTotalPages());
	 * model.addAttribute("currentPage", pageNum); model.addAttribute("startCount",
	 * startCount); model.addAttribute("endCount", endCount);
	 * model.addAttribute("totalCount", page.getTotalElements());
	 * model.addAttribute("userList", lstUser); model.addAttribute("sortField",
	 * sortField); model.addAttribute("sortDir", sortDir);
	 * model.addAttribute("revSortDir", revSortDir); model.addAttribute("keyword",
	 * keyword);
	 * 
	 * return "blogs/blogs"; }
	 */
	
	//@Autowired private CurrencyRepository currencyRepo;
	
	/*
	 * @GetMapping("/blogs") public String listAll(Model model) { List<Setting>
	 * listSettings = service.listAllSettings(); List<Currency> listCurrencies =
	 * currencyRepo.findAllByOrderByNameAsc();
	 * 
	 * model.addAttribute("listCurrencies", listCurrencies);
	 * 
	 * for (Setting setting : listSettings) { model.addAttribute(setting.getKey(),
	 * setting.getValue()); }
	 * 
	 * // model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
	 * 
	 * return "blogs/settings"; }
	 */
	
	/*
	 * @PostMapping("/blogs/save_general") public String
	 * saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
	 * HttpServletRequest request, RedirectAttributes ra) throws IOException {
	 * GeneralSettingBag settingBag = service.getGeneralSettings();
	 * 
	 * saveSiteLogo(multipartFile, settingBag); saveCurrencySymbol(request,
	 * settingBag);
	 * 
	 * updateSettingValuesFromForm(request, settingBag.list());
	 * 
	 * ra.addFlashAttribute("message", "General settings have been saved.");
	 * 
	 * return "redirect:/blogs"; }
	 */
	
	/*
	 * private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag
	 * settingBag) throws IOException { if (!multipartFile.isEmpty()) { String
	 * fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); String
	 * value = "/site-logo/" + fileName; settingBag.updateSiteLogo(value);
	 * 
	 * String uploadDir = "../site-logo"; FileUploadUtil.cleanDir(uploadDir);
	 * FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	 * 
	 * // AmazonS3Util.removeFolder(uploadDir); ///
	 * AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
	 * } }
	 */
	
	/*
	 * private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag
	 * settingBag) { Integer currencyId =
	 * Integer.parseInt(request.getParameter("CURRENCY_ID")); Optional<Currency>
	 * findByIdResult = currencyRepo.findById(currencyId);
	 * 
	 * if (findByIdResult.isPresent()) { Currency currency = findByIdResult.get();
	 * settingBag.updateCurrencySymbol(currency.getSymbol()); } }
	 * 
	 * private void updateSettingValuesFromForm(HttpServletRequest request,
	 * List<Setting> listSettings) { for (Setting setting : listSettings) { String
	 * value = request.getParameter(setting.getKey()); if (value != null) {
	 * setting.setValue(value); } }
	 * 
	 * service.saveAll(listSettings); }
	 * 
	 * @PostMapping("/blogs/save_mail_server") public String
	 * saveMailServerSetttings(HttpServletRequest request, RedirectAttributes ra) {
	 * List<Setting> mailServerSettings = service.getMailServerSettings();
	 * updateSettingValuesFromForm(request, mailServerSettings);
	 * 
	 * ra.addFlashAttribute("message", "Mail server settings have been saved");
	 * 
	 * return "redirect:/blogs#mailServer"; }
	 * 
	 * @PostMapping("/blogs/save_mail_templates") public String
	 * saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes ra)
	 * { List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
	 * updateSettingValuesFromForm(request, mailTemplateSettings);
	 * 
	 * ra.addFlashAttribute("message", "Mail template settings have been saved");
	 * 
	 * return "redirect:/blogs#mailTemplates"; }
	 * 
	 * @PostMapping("/blogs/save_payment") public String
	 * savePaymentSetttings(HttpServletRequest request, RedirectAttributes ra) {
	 * List<Setting> paymentSettings = service.getPaymentSettings();
	 * updateSettingValuesFromForm(request, paymentSettings);
	 * 
	 * ra.addFlashAttribute("message", "Payment settings have been saved");
	 * 
	 * return "redirect:/blogs#payment"; }
	 */

}
	
