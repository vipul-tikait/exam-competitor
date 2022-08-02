package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.exam.competitor.admin.common.entity.Brand;
import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.exception.BrandNotFoundException;
import com.exam.competitor.admin.export.BrandCsvExpoter;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.service.BrandService;
import com.exam.competitor.admin.service.CategoryService;

@Controller
public class BrandController {

	@Autowired
	private BrandService examService;
	
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		return listByPage(1, "name", "asc",model, null);
	}
 	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			Model model,
			@Param("keyword") String keyword) {
	
		if (sortDir == null || sortDir.isEmpty() ||sortDir.equalsIgnoreCase("null") ) {
			sortDir = "asc";
		}
		
		Page<Brand> pageInfo = examService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Brand> listBrands = pageInfo.getContent();
		
		long startCount = (pageNum-1) * examService.ROOT_BRANDS_PER_PAGE + 1;
		long endCount = startCount + examService.ROOT_BRANDS_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc")?"desc":"asc";
		
		model.addAttribute("totalPage", pageInfo.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		return "brands/brands";
	}

	@GetMapping("/brands/new")
	public String newBrands(Model model) {
		
		List<Category> listCategory = categoryService.listCategoryUsedInForm();
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create new Brand");
		return "brands/brand_form";
	}
	@PostMapping("/brands/save")
	public String saveBrands(Brand brand, @RequestParam("brandFileImage") MultipartFile file, RedirectAttributes ra) throws IOException {
		
		if (file != null && !(file.isEmpty())) {
		
			String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll("\\s","");
			brand.setLogo(fileName);
			
			Brand savedBrand = examService.saveBrand(brand);
			
			String uploadDir = "../brands-images/"+savedBrand.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		}else {
			examService.saveBrand(brand);	
		}
		
		ra.addFlashAttribute("message", "Brands has been added successfuly!");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Brand brand = examService.get(id); 
			
			List<Category> listCategories = categoryService.listCategoryUsedInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategory", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID - " + id + ")");
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}

	}
	
	@GetMapping("/brands/delete/{id}")
	public String deletBrands(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			examService.deleteBrand(id);
			String brandDir = "../brand-images/"+id;
			FileUploadUtil.removeDir(brandDir);
			redirectAttributes.addFlashAttribute("message",
					"Ths Brand with id " + id + " has been deleted successfully.");
			
		} catch (BrandNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";

	}

	@GetMapping("/brands/export/csv/{pageNum}")
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

		Page<Brand> pageInfo = examService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Brand> listBrands = pageInfo.getContent();
		
		BrandCsvExpoter exporter = new BrandCsvExpoter();
		exporter.export(listBrands, httpServletResponse);

	} 


	
	
}
