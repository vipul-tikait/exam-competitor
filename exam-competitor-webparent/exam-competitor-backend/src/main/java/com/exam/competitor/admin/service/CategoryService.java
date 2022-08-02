package com.exam.competitor.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.category.CategoryPageInfo;
import com.exam.competitor.admin.common.entity.Category;
import com.exam.competitor.admin.exception.CategoryNotFoundException;
import com.exam.competitor.admin.repo.CategoryRepository;


@Service
@Transactional
public class CategoryService {

	@Autowired
	CategoryRepository catRepo;

	public static final int ROOT_CATEGORY_PER_PAGE = 4;

	public List<Category> listByPage(CategoryPageInfo pageInfo, int  pageNum, String sortField, String sortDir, String kayword) {

		String sortStr = sortField==null?"name":sortField;
		Sort sort = Sort.by(sortStr);
		if (sortDir.equals("asc")) {
			sort  = sort.ascending();
		}else if (sortDir.equals("desc")) {
			sort  = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_CATEGORY_PER_PAGE,sort);
		Page<Category> pageCategory = null;
		
		if (kayword != null && !kayword.isEmpty()) {
			pageCategory = catRepo.search(kayword, pageable);
			
		} else {
			pageCategory = catRepo.findListRootCategory(pageable);
			
		}
		List<Category> rootCategory = pageCategory.getContent();
		
		pageInfo.setTotalElements(pageCategory.getTotalElements());
		pageInfo.setTotalPages(pageCategory.getTotalPages());
		
		
		if (kayword != null && !kayword.isEmpty()) {
			List<Category> searchResult = pageCategory.getContent();
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			
			return searchResult;
		} else {
			return listHierarchicalCategories(rootCategory,sortDir);
		}
		
	}

	public List<Category> listCategoryToExport(int pageNum, String sortDir, String kayword) {

		Sort sort = Sort.by("name") ;
		
		if (sortDir.equals("asc")) {
			sort  = sort.ascending();
		}else if (sortDir.equals("desc")) {
			sort  = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_CATEGORY_PER_PAGE,sort);
		Page<Category> pageCategory = null;
		
		if (kayword != null && !kayword.isEmpty()) {
			pageCategory = catRepo.search(kayword, pageable);
			
		} else {
			pageCategory = catRepo.findListRootCategory(pageable);
			
		}
		List<Category> rootCategory = pageCategory.getContent();
		
		
		if (kayword != null && !kayword.isEmpty()) {
			List<Category> searchResult = pageCategory.getContent();
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			}
			
			return searchResult;
		} else {
			return listHierarchicalCategories(rootCategory,sortDir);
		}
		
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
		
		List<Category> hierarchicalCategories = new ArrayList<>();
		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);
			for(Category subCategory : children) {
				String name = "--"+subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}
		
		return hierarchicalCategories;
	}
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent,
			int subLevel, String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(),sortDir);	
		int newSubLevel = subLevel+1;
		
		for (Category subCategory : children) {
			String name = "";
			for (int i=0; i<newSubLevel;i++) {
				name += "--";
			}
			name += subCategory.getName();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel,sortDir);
		}
	}
	
	public List<Category> listCategoryUsedInForm() {
	List<Category> listCategoryUsedInForm = new ArrayList<>();	
		Iterable<Category> catgoriesDB = catRepo.findListRootCategory(Sort.by("name").ascending());
		for (Category category : catgoriesDB) {
			if (category.getParent() == null) {
				listCategoryUsedInForm.add(Category.copyIdandName(category));
				Set<Category> catSet = sortSubCategories(category.getChildren());
				for (Category subCat : catSet) {
					String name = "--"+subCat.getName();
					listCategoryUsedInForm.add(Category.copyIdandName(subCat.getId(),name));
					listSubCategoriesUsedInForm(listCategoryUsedInForm,subCat,1);
				}
			}
		}
		
		return listCategoryUsedInForm;	
	}
	private void listSubCategoriesUsedInForm(List<Category> listCategoryUsedInForm,Category parent, int subLevel) {
		int newSub = subLevel+1;
		
		Set<Category> categories = sortSubCategories(parent.getChildren());
		for (Category subCategory : categories) {
			String name = "";
			for (int i=0; i<newSub;i++) {
				name += "--";
			}
			name += subCategory.getName();
			listCategoryUsedInForm.add(Category.copyIdandName(subCategory.getId(),name));
			listSubCategoriesUsedInForm(listCategoryUsedInForm,subCategory, subLevel);
		}
		
	
	}

	public Category save(Category category) {
		Category parent = category.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIDs(allParentIds);
		}
		
		return catRepo.save(category);
	}

	public Category getCategoryById(Integer id) throws CategoryNotFoundException {

	try {
			return catRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find any category by ID:" + id);
		}
	}
	
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id==null || alias==null);
		
		Category categoryByName = catRepo.findByName(name);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicateName";
			}else {
				Category categoryAlias = catRepo.findByAlias(alias);
				if (categoryAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if (categoryByName != null && categoryByName.getId() != id) {
			return "DuplicateName";	
			}
			Category categoryByAlias = catRepo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		
		return "OK";
	}
	private Set<Category> sortSubCategories(Set<Category> children){
		return sortSubCategories(children,"asc");
	}
	private Set<Category> sortSubCategories(Set<Category> children, String sortDir){
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});
		
		sortedChildren.addAll(children);
		return sortedChildren;
	}

	/*
	 * public Page<Category> listByPage(int paneNum, String sortField, String
	 * sortDir, String keyword) {
	 * 
	 * Sort sort = Sort.by(sortField);
	 * 
	 * sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	 * org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum -
	 * 1, USER_PER_PAGE, sort);
	 * 
	 * if (keyword != null) { return catRepo.findAll(keyword, pageable); } return
	 * catRepo.findAll(pageable); }
	 */
	public void updateEnabledStatus(Integer id, boolean status) {
		catRepo.updateEnabledStatus(id, status);
	}		 
	
	public void deleteCategory(Integer id) throws CategoryNotFoundException {
		Long countById = catRepo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any category by ID:" + id);
		}
		catRepo.deleteById(id);
	}
	
	public List<Category> getAllCategoryForExport(){
	List<Category> listCategory = new ArrayList<>();	
		Iterable<Category> catgoriesDB = catRepo.findAll(Sort.by("name").ascending());
		for (Category category : catgoriesDB) {
			if (category.getParent() == null) {
				listCategory.add(Category.copyFull(category));
				Set<Category> catSet = sortSubCategories(category.getChildren());
				for (Category subCat : catSet) {
					String name = "--"+subCat.getName();
					listCategory.add(Category.copyFull(subCat));
					listSubCategoriesUsedInForm(listCategory,subCat,1);
				}
			}
		}
		
		return listCategory;	
	}
	
	/*
	 * public Category getCategoryByName(String name) { return
	 * catRepo.getCategoryByName(name); }
	 * 
	 * public List<Role> findAllRolles() { return (List<Role>) roleRepo.findAll(); }
	 * 
	 * 
	 * public Category save(Category category) {
	 * 
	 * return catRepo.save(category); }
	 * 
	 * 
	 * public User updateAccount(Category catForm) { Category categoreInDB =
	 * catRepo.findById(catForm.getId()).get();
	 * 
	 * if (!catForm.getName().isEmpty()) {
	 * categoreInDB.setPassword(catForm.getName());
	 * 
	 * }
	 * 
	 * if (catForm.getImage() != null) { categoreInDB.setPhotos(catForm.getName());
	 * }
	 * 
	 * categoreInDB.setFirstName(userForm.getFirstName());
	 * categoreInDB.setLastName(userForm.getLastName()); return
	 * catRepo.save(userInDB); }
	 * 
	 * 
	 * 
	 * public boolean isEmailUnique(Integer id, String email) { User userByEmail =
	 * userRepo.getUserByEmail(email);
	 * 
	 * if (userByEmail == null) return true;
	 * 
	 * boolean isCreateNew = (id == null);
	 * 
	 * if (isCreateNew) { if (userByEmail != null) return false; } else { if
	 * (userByEmail.getId() != id) { return false; } }
	 * 
	 * return true; }
	 * 
	 * 
	 * 
	 * 
	 */
}
