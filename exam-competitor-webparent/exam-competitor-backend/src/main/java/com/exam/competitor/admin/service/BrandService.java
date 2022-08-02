package com.exam.competitor.admin.service;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.Brand;
import com.exam.competitor.admin.exception.BrandNotFoundException;
import com.exam.competitor.admin.repo.BrandRepository;

@Service
public class BrandService {

	public static final int ROOT_BRANDS_PER_PAGE = 10;
	@Autowired
	private BrandRepository repository;

	
	public List<Brand> findAllBrandsList(){
		
		return (List<Brand>) repository.findAll();
	}
		
	public Page<Brand> listByPage(Integer pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField) ;
		
		sort = sortDir.equals("asc")? sort.ascending():sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_BRANDS_PER_PAGE,sort);
		Page<Brand> pageBrands = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			return repository.findAll(keyword, pageable);
			
		} 
		
		return repository.findAll(pageable);
	}
		
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id==null);
		
		Brand brandByName = repository.findByName(name);
		
		if(isCreatingNew) {
			if(brandByName != null) {
				return "DuplicateName";
			}
		} else {
			if (brandByName != null && brandByName.getId() != id) {
			return "DuplicateName";	
			}
		}
		return "OK";
	}
	
	public Brand saveBrand(Brand brand) {
		return repository.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException{
		try {
			return repository.findById(id).get();
		}catch (NoSuchElementException e) {
			 throw new BrandNotFoundException("Could not find any brand with ID:"+id);
		}
	}
	
	public void deleteBrand(Integer id) throws BrandNotFoundException {
		
		Long countById = repository.countById(id);
		if (countById == null || countById ==0) {
			throw new BrandNotFoundException("Could not find any brand with ID:"+id);
		}
		
		repository.deleteById(id);
	}
	
	/*
	 * public List<Category> listBrandsToExport(int pageNum, String sortDir, String
	 * kayword) {
	 * 
	 * Sort sort = Sort.by("name") ;
	 * 
	 * if (sortDir.equals("asc")) { sort = sort.ascending(); }else if
	 * (sortDir.equals("desc")) { sort = sort.descending(); }
	 * 
	 * Pageable pageable = PageRequest.of(pageNum-1, ROOT_BRANDS_PER_PAGE,sort);
	 * Page<Category> pageCategory = null;
	 * 
	 * if (kayword != null && !kayword.isEmpty()) { pageCategory =
	 * repository.search(kayword, pageable);
	 * 
	 * } else { pageCategory = repository.findListRootCategory(pageable);
	 * 
	 * } List<Category> rootCategory = pageCategory.getContent();
	 * 
	 * 
	 * if (kayword != null && !kayword.isEmpty()) { List<Category> searchResult =
	 * pageCategory.getContent(); for (Category category : searchResult) {
	 * category.setHasChildren(category.getChildren().size() > 0); }
	 * 
	 * return searchResult; } else { return
	 * listHierarchicalCategories(rootCategory,sortDir); }
	 * 
	 * }
	 */

	
}
