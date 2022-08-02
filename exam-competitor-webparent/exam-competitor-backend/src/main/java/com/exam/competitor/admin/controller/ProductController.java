package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Brand;
import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.common.entity.product.Product;
import com.exam.competitor.admin.exception.ProductNotFoundException;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.paging.PagingAndSortingHelper;
import com.exam.competitor.admin.paging.PagingAndSortingParam;
import com.exam.competitor.admin.product.ProductSaveHelper;
import com.exam.competitor.admin.security.ZoopkanUserDetails;
import com.exam.competitor.admin.service.BrandService;
import com.exam.competitor.admin.service.CategoryService;
import com.exam.competitor.admin.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=id&sortDir=asc&categoryId=0";

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;// ultRedirectURLlistByPage(1, model, "name", "asc", null,0);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, @Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId) {

		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
		List<Product> lstProduct = page.getContent();
		List<Category> listCategories = categoryService.listCategoryUsedInForm();

		long startCount = (pageNum - 1) * productService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + productService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "dsc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("listProducts", lstProduct);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		if (categoryId != null)
			model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);

		return "products/products";

	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.findAllBrandsList();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ZoopkanUserDetails loggedUser) throws IOException {

		/*
		 * mainImageName(mainImageMultipart, product);
		 * extraImagesNames(extraImageMultiparts, product);
		 * productDetails(detailNames,detailValues,product);
		 * setExistingExtraImageNames(imageIDs,imageNames,product);
		 */
		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "The product has been saved successfully.");
				return defaultRedirectURL;
			}
		}

		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		/*
		 * saveUploadedImages(mainImageMultipart,extraImageMultiparts,savedProduct);
		 * deleteExtraImagesWeredRemovedOnForm(product);
		 */

		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);

		ra.addFlashAttribute("message", "Product has been added successfuly!");

		return defaultRedirectURL;
	}

	/*
	 * private void productDetails(String[] detailNames, String[] detailValues,
	 * Product product) {
	 * 
	 * if (detailNames == null || detailNames.length == 0) return;
	 * 
	 * for (int count = 0; count < detailNames.length; count++) { String name =
	 * detailNames[count]; String value = detailValues[count];
	 * 
	 * if (!name.isEmpty() && !value.isEmpty()) { product.addDetail(name, value); }
	 * } }
	 * 
	 * private void saveUploadedImages(MultipartFile mainImageMultipart,
	 * MultipartFile[] extraImageMultiparts, Product savedProduct) throws
	 * IOException {
	 * 
	 * if (!mainImageMultipart.isEmpty()) { String fileName =
	 * StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
	 * savedProduct.setMainImage(fileName); String uploadDir = "../product-images/"
	 * + savedProduct.getId(); FileUploadUtil.cleanDir(uploadDir);
	 * FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
	 * 
	 * }
	 * 
	 * if (extraImageMultiparts.length > 0 ) { String uploadDir =
	 * "../product-images/" + savedProduct.getId()+"/extras";
	 * 
	 * for (MultipartFile multipartFile : extraImageMultiparts) { if
	 * (!multipartFile.isEmpty()) {
	 * 
	 * String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	 * FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); } } }
	 * 
	 * 
	 * }
	 * 
	 * private void mainImageName(MultipartFile mainImageMultipart, Product product)
	 * { if (!mainImageMultipart.isEmpty()) { String fileName =
	 * StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
	 * product.setMainImage(fileName); } }
	 * 
	 * private void extraImagesNames(MultipartFile[] extraImageMultiparts, Product
	 * product) {
	 * 
	 * if (extraImageMultiparts.length > 0 ) { for (MultipartFile multipartFile :
	 * extraImageMultiparts) { if (!multipartFile.isEmpty()) { String fileName =
	 * StringUtils.cleanPath(multipartFile.getOriginalFilename());
	 * product.addExtraImage(fileName); } } } }
	 * 
	 * private void setExistingExtraImageNames(String[] imageIDs, String[]
	 * imageNames, Product product) { if (imageIDs == null || imageIDs.length == 0)
	 * return;
	 * 
	 * Set<ProductImage> images = new HashSet<>();
	 * 
	 * for (int count = 0; count < imageIDs.length; count++) { Integer id =
	 * Integer.parseInt(imageIDs[count]); String name = imageNames[count];
	 * 
	 * 
	 * images.add(new ProductImage(id, name, product)); }
	 * 
	 * product.setImages(images);
	 * 
	 * }
	 * 
	 * public void deleteExtraImagesWeredRemovedOnForm(Product product) throws
	 * IOException { String extraImageDir = "../product-images/" + product.getId() +
	 * "/extras"; Path dirPath = Paths.get(extraImageDir);
	 * 
	 * Files.list(dirPath).forEach(file -> { String fileName =
	 * file.toFile().getName();
	 * 
	 * if (!product.containsImageName(fileName)) { try { Files.delete(file);
	 * 
	 * System.out.println("Deleted extra image: " + fileName); } catch (IOException
	 * e) { System.out.println("Deleted extra image: " + fileName);
	 * e.printStackTrace(); } } });
	 * 
	 * }
	 * 
	 */

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return defaultRedirectURL;
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return defaultRedirectURL;
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			@AuthenticationPrincipal ZoopkanUserDetails loggedUser) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.findAllBrandsList();
			Integer numberOfExistingExtraImages = product.getImages().size();

			boolean isReadOnlyForSalesperson = false;

			if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if (loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesperson = true;
				}
			}
			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return defaultRedirectURL;
		}
	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);

			return "products/product_detail_modal";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return defaultRedirectURL;
		}
	}

}
