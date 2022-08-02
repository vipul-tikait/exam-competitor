package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.category.CategoryPageInfo;
import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.exception.CategoryNotFoundException;
import com.exam.competitor.admin.export.CategoryCsvExpoter;
import com.exam.competitor.admin.export.CategoryExcelExpoter;
import com.exam.competitor.admin.export.CategoryPdfExpoter;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.service.CategoryService;

@Controller
public class CategoryController {

	@Autowired
	CategoryService catService;

	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, "id","asc" ,model, null);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			Model model,
			@Param("keyword") String keyword) {

		if (sortDir == null || sortDir.isEmpty() ) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = catService.listByPage(pageInfo, pageNum, sortField,sortDir, keyword);
		String reverseSortDir = sortDir.equals("asc")?"desc":"asc";
		
		
		long startCount = (pageNum-1) * catService.ROOT_CATEGORY_PER_PAGE + 1;
		long endCount = startCount + catService.ROOT_CATEGORY_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		model.addAttribute("totalPage", pageInfo.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
	//	return listByPage(1, model, "name", "asc", null);//"users";
	//	return listByPage(pageNum, reverseSortDir, model);
		return "categories/categories";
				 //return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String createNewUser(Model model) {

		List<Category> listCategory = catService.listCategoryUsedInForm();
		Category category = new Category();
		category.setEnabled(true);
		model.addAttribute("category", category);
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("pageTitle", "Create New Category");
		return "categories/category_form";
	}	
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("catFileImage") MultipartFile file, RedirectAttributes ra) throws IOException {
		
		if (!file.isEmpty()) {
			
		String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll("\\s","");
		category.setImage(fileName);
		
		Category savedCategory = catService.save(category);
		
		String uploadDir = "../category-images/"+savedCategory.getId();
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, file);
		}else {
			catService.save(category);
		}
		ra.addFlashAttribute("message", "Category has been added successfuly!");
		return "redirect:/categories";
	}
		
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@Param("sortDir") String sortDir,@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			if (sortDir == null || sortDir.isEmpty() ) {
				sortDir = "asc";
			}
			
			List<Category> listCategories = catService.listCategoryUsedInForm();
			Category category = catService.getCategoryById(id);
			model.addAttribute("category", category);
			model.addAttribute("listCategory", listCategories);
			model.addAttribute("pageTitle", "Edit Category (ID - " + id + ")");
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}

	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String enableCategoryStatus(@PathVariable(name = "id") Integer id,
			@PathVariable("status") Boolean status, Model model, RedirectAttributes redirectAttributes) {

		catService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The Category id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/categories";

	}
	

	@GetMapping("/categories/delete/{id}")
	public String deletCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			catService.deleteCategory(id);
			String categoryDir = "../category-images/"+id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message",
					"Ths Category with id " + id + " has been deleted successfully.");
			
		} catch (CategoryNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";

	}
	
	@GetMapping("/categories/export/csv/{pageNum}")
	public void exportToCsv(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {
		
		if (sortDir == null || sortDir.isEmpty() ) {
			sortDir = "asc";
		}else {
			sortDir = sortDir=="asc"?"desc" :"asc";
		}
		
		List<Category> listCategories = catService.listCategoryToExport(pageNum, sortDir, keyword);
		
		CategoryCsvExpoter exporter = new CategoryCsvExpoter();
		exporter.export(listCategories, httpServletResponse);

	} 

	@GetMapping("/categories/export/pdf/{pageNum}")
	public void exportToPdf(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {
		
		if (sortDir == null || sortDir.isEmpty() ) {
			sortDir = "asc";
		}else {
			sortDir = sortDir=="asc"?"desc" :"asc";
		}
		
		List<Category> listCategories = catService.listCategoryToExport(pageNum, sortDir, keyword);
	
		CategoryPdfExpoter exporter = new CategoryPdfExpoter();
		exporter.export(listCategories, httpServletResponse);

	} 
	
	@GetMapping("/categories/export/excel/{pageNum}")
	public void exportToExcel(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {
		
		if (sortDir == null || sortDir.isEmpty() ) {
			sortDir = "asc";
		}else {
			sortDir = sortDir=="asc"?"desc" :"asc";
		}
		
		List<Category> listCategories = catService.listCategoryToExport(pageNum, sortDir, keyword);
		
		CategoryExcelExpoter exporter = new CategoryExcelExpoter();
		exporter.export(listCategories, httpServletResponse);

	} 


	
		
	/*
	 * Page<Category> page = catService.listByPage(pageNum, sortDir, keyword);
	 * List<Category> lstCategories = page.getContent();
	 * 
	 * long startCount = (pageNum - 1) * catService.USER_PER_PAGE + 1; long endCount
	 * = startCount + catService.USER_PER_PAGE - 1; if (endCount >
	 * page.getTotalElements()) { endCount = page.getTotalElements(); }
	 * 
	 * String revSortDir = sortDir.equals("asc") ? "dsc" : "asc";
	 * model.addAttribute("totalPage", page.getTotalPages());
	 * model.addAttribute("currentPage", pageNum); model.addAttribute("startCount",
	 * startCount); model.addAttribute("endCount", endCount);
	 * model.addAttribute("totalCount", page.getTotalElements());
	 * model.addAttribute("listCategories", catService.findAllCategoryList());
	 * model.addAttribute("sortField", sortField); model.addAttribute("sortDir",
	 * sortDir); model.addAttribute("revSortDir", revSortDir);
	 * model.addAttribute("keyword", keyword);
	 * 
	 * return "categories/categories";
	 	}
*/
	/*
	 * @PostMapping("/categories/save") public String savecategory(Category
	 * category, RedirectAttributes redirectAttributes,
	 * 
	 * @RequestParam("catFileImage") MultipartFile multipartFile) throws IOException
	 * {
	 * 
	 * if (!(multipartFile.isEmpty())) { String fileName =
	 * org.springframework.util.StringUtils.cleanPath(multipartFile.
	 * getOriginalFilename()) .replaceAll("\\s", "");
	 * 
	 * category.setImage(fileName);
	 * 
	 * Category savedCategory = catService.save(category);
	 * 
	 * String uploadDir = "../categories-images/" + savedCategory.getId();
	 * FileUploadUtil.cleanDir(uploadDir); FileUploadUtil.saveFile(uploadDir,
	 * fileName, multipartFile); } else { if (category.getImage() == null ||
	 * category.getImage().isEmpty()) category.setImage(null);
	 * catService.save(category); }
	 * 
	 * redirectAttributes.addFlashAttribute("message",
	 * "Category has been saved successfully.");
	 * 
	 * // String emailFirstPart = user.getEmail().split("@")[0]; // return
	 * "redirect:/categories/page/1?sortField=firstName&sortDir=asc&keyword=" +
	 * emailFirstPart; return "redirect:/categories"; }
	 */
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @GetMapping("/users/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = catService.findAllUserList(); UserExcelExpoter exporter
	 * = new UserExcelExpoter(); exporter.export(listAll, httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/pdf") public void exportToPdf(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = catService.findAllUserList(); UserPdfExpoter exporter =
	 * new UserPdfExpoter(); exporter.export(listAll, httpServletResponse);
	 * 
	 * }
	 */

}
