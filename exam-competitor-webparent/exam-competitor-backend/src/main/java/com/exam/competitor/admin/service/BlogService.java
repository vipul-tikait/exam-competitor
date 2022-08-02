package com.exam.competitor.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BlogService {
	/*
	 * public static final int BLOG_PER_PAGE = 5;
	 * 
	 * @Autowired private BlogRepository repo;
	 * 
	 * public List<Blog> listAll() { return (List<Blog>) repo.findAll(); }
	 * 
	 * 
	 * public String checkUnique(Integer id, String name) { boolean isCreatingNew =
	 * (id == null || id == 0); Blog blogByName = repo.findByName(name);
	 * 
	 * if (isCreatingNew) { if (blogByName != null) return "DuplicateName"; } else {
	 * if (blogByName != null && blogByName.getId() != id) { return "DuplicateName";
	 * } } return "OK"; }
	 * 
	 * public Blog save(Blog blog) { if (blog.getId() == null) {
	 * blog.setCreatedTime(new Date()); }
	 * 
	 * if (blog.getTitle() == null || blog.getTitle().isEmpty()) {
	 * blog.setAlias(defaultAlias); } else {
	 * blog.setAlias(blog.getAlias().replaceAll(" ", "-")); }
	 * 
	 * product.setUpdatedTime(new Date());
	 * 
	 * Product updatedProduct = repo.save(product);
	 * //repo.updateReviewCountAndAverageRating(updatedProduct.getId());
	 * 
	 * return updatedProduct; }
	 * 
	 * public void saveProductPrice(Product productInForm) { Product productInDB =
	 * repo.findById(productInForm.getId()).get();
	 * productInDB.setCost(productInForm.getCost());
	 * productInDB.setPrice(productInForm.getPrice());
	 * productInDB.setDiscountPercent(productInForm.getDiscountPercent());
	 * 
	 * repo.save(productInDB); }
	 * 
	 * public void updateProductEnabledStatus(Integer id, boolean enabled) {
	 * repo.updateEnabledStatus(id, enabled); }
	 * 
	 * public void delete(Integer id) throws ProductNotFoundException { Long
	 * countById = repo.countById(id);
	 * 
	 * if (countById == null || countById == 0) { throw new
	 * ProductNotFoundException("Could not find any product with ID " + id); }
	 * 
	 * repo.deleteById(id); }
	 * 
	 * public Product get(Integer id) throws ProductNotFoundException { try { return
	 * repo.findById(id).get(); } catch (NoSuchElementException ex) { throw new
	 * ProductNotFoundException("Could not find any product with ID " + id); } }
	 * 
	 * public Page<Product> listByPage(int pageNum, String sortField, String
	 * sortDir, String keyword, Integer categoryId) { // TODO Auto-generated method
	 * stub Sort sort = Sort.by(sortField);
	 * 
	 * sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	 * org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNum-1,
	 * PRODUCTS_PER_PAGE,sort);
	 * 
	 * if (keyword != null && !keyword.isEmpty()) { if (categoryId != null &&
	 * categoryId > 0) { String categoryIdMatch = "-" + String.valueOf(categoryId) +
	 * "-"; return repo.searchInCategory(categoryId, categoryIdMatch, keyword,
	 * pageable); } return repo.findAll(keyword, pageable); } if (categoryId != null
	 * && categoryId > 0) { String categoryIdMatch = "-" +
	 * String.valueOf(categoryId) + "-"; return repo.findAllInCategory(categoryId,
	 * categoryIdMatch, pageable); }
	 * 
	 * return repo.findAll(pageable);
	 * 
	 * }
	 * 
	 * public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer
	 * categoryId) { Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE,
	 * pageNum); String keyword = helper.getKeyword(); Page<Product> page = null;
	 * 
	 * if (keyword != null && !keyword.isEmpty()) { if (categoryId != null &&
	 * categoryId > 0) { String categoryIdMatch = "-" + String.valueOf(categoryId) +
	 * "-"; page = repo.searchInCategory(categoryId, categoryIdMatch, keyword,
	 * pageable); } else { page = repo.findAll(keyword, pageable); } } else { if
	 * (categoryId != null && categoryId > 0) { String categoryIdMatch = "-" +
	 * String.valueOf(categoryId) + "-"; page = repo.findAllInCategory(categoryId,
	 * categoryIdMatch, pageable); } else { page = repo.findAll(pageable); } }
	 * 
	 * helper.updateModelAttributes(pageNum, page); }
	 * 
	 */}
