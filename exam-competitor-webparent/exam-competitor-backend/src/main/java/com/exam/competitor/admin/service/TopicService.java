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
import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.exception.TopicNotFoundException;
import com.exam.competitor.admin.repo.TopicRepository;
import com.exam.competitor.admin.topic.TopicPageInfo;


@Service
@Transactional
public class TopicService {

	@Autowired
	TopicRepository topicRepo;

	public static final int ROOT_TOPIC_PER_PAGE = 4;

	public List<Topic> listByPage(TopicPageInfo pageInfo, int  pageNum, String sortField, String sortDir, String kayword) {
		
		String sortStr = sortField==null?"name":sortField;
		Sort sort = Sort.by(sortStr);
		if (sortDir.equals("asc")) {
			sort  = sort.ascending();
		}else if (sortDir.equals("desc")) {
			sort  = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_TOPIC_PER_PAGE,sort);
		Page<Topic> pageTopic = null;
		
		if (kayword != null && !kayword.isEmpty()) {
			pageTopic = topicRepo.search(kayword, pageable);
			
		} else {
			pageTopic = topicRepo.findListRootTopic(pageable);
			
		}
		List<Topic> rootTopic = pageTopic.getContent();
		
		pageInfo.setTotalElements(pageTopic.getTotalElements());
		pageInfo.setTotalPages(pageTopic.getTotalPages());
		
		
		if (kayword != null && !kayword.isEmpty()) {
			List<Topic> searchResult = pageTopic.getContent();
			for (Topic topic : searchResult) {
				topic.setHasChildren(topic.getChildren().size() > 0);
			}
			
			return searchResult;
		} else {
			return listHierarchicalTopics(rootTopic,sortDir);
		}
		
	}

	public List<Topic> listTopicToExport(int pageNum, String sortDir, String kayword) {

		Sort sort = Sort.by("name") ;
		
		if (sortDir.equals("asc")) {
			sort  = sort.ascending();
		}else if (sortDir.equals("desc")) {
			sort  = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_TOPIC_PER_PAGE,sort);
		Page<Topic> pageTopic = null;
		if (kayword != null && !kayword.isEmpty()) {
			pageTopic = topicRepo.search(kayword, pageable);
			
		} else {
			pageTopic = topicRepo.findListRootTopic(pageable);
			
		}
		List<Topic> rootTopic = pageTopic.getContent();
		
		
		if (kayword != null && !kayword.isEmpty()) {
			List<Topic> searchResult = pageTopic.getContent();
			for (Topic topic : searchResult) {
				topic.setHasChildren(topic.getChildren().size() > 0);
			}
			
			return searchResult;
		} else {
			return listHierarchicalTopics(rootTopic,sortDir);
		}
		
	}

	private List<Topic> listHierarchicalTopics(List<Topic> rootTopics, String sortDir){
		
		List<Topic> hierarchicalTopics = new ArrayList<>();
		for (Topic root: rootTopics) {
			hierarchicalTopics.add(Topic.copyFull(root));
			Set<Topic> children = sortSubTopics(root.getChildren(),sortDir);
			for(Topic subTopic : children) {
				String name = "--"+subTopic.getName();
				hierarchicalTopics.add(Topic.copyFull(subTopic, name));
				listSubHierarchicalTopics(hierarchicalTopics, subTopic, 1, sortDir);
			}
		}
		
		return hierarchicalTopics;
	}
	private void listSubHierarchicalTopics(List<Topic> hierarchicalTopics, Topic parent,
			int subLevel, String sortDir) {
		Set<Topic> children = sortSubTopics(parent.getChildren(),sortDir);	
		int newSubLevel = subLevel+1;
		
		for (Topic subTopic : children) {
			String name = "";
			for (int i=0; i<newSubLevel;i++) {
				name += "--";
			}
			name += subTopic.getName();
			hierarchicalTopics.add(Topic.copyFull(subTopic, name));
			listSubHierarchicalTopics(hierarchicalTopics, subTopic, newSubLevel,sortDir);
		}
	}
	
	public List<Topic> listTopicUsedInForm() {
	List<Topic> listTopicUsedInForm = new ArrayList<>();	
		Iterable<Topic> topicsDB = topicRepo.findListRootTopic(Sort.by("name").ascending());
		for (Topic topic : topicsDB) {
			if (topic.getParent() == null) {
				listTopicUsedInForm.add(Topic.copyIdandName(topic));
				Set<Topic> catSet = sortSubTopics(topic.getChildren());
				for (Topic subCat : catSet) {
					String name = "--"+subCat.getName();
					listTopicUsedInForm.add(Topic.copyIdandName(subCat.getId(),name));
					listSubTopicsUsedInForm(listTopicUsedInForm,subCat,1);
				}
			}
		}
		
		return listTopicUsedInForm;	
	}
	
	/*
	 * public List<Topic> listTopicAssignedToExamLevel(Set<Topic> topList) {
	 * List<Topic> listTopicUsedInForm = new ArrayList<>(); //Iterable<Topic>
	 * topicsDB =
	 * topicRepo.findListRootTopicAssignToExmLevel(Sort.by("name").ascending(),
	 * exmLevelId);
	 * 
	 * for (Topic topic : topList) { if (topic.getParent() == null) {
	 * listTopicUsedInForm.add(Topic.copyIdandName(topic)); Set<Topic> catSet =
	 * sortSubTopics(topic.getChildren()); for (Topic subCat : catSet) { String name
	 * = "--" + subCat.getName();
	 * listTopicUsedInForm.add(Topic.copyIdandName(subCat.getId(), name));
	 * listSubTopicsUsedInForm(listTopicUsedInForm, subCat, 1); } } }
	 * 
	 * return listHierarchicalTopics(listTopicUsedInForm, "name"); }
	 */	private void listSubTopicsUsedInForm(List<Topic> listTopicUsedInForm,Topic parent, int subLevel) {
		int newSub = subLevel+1;
		
		Set<Topic> topics = sortSubTopics(parent.getChildren());
		for (Topic subTopic : topics) {
			String name = "";
			for (int i=0; i<newSub;i++) {
				name += "--";
			}
			name += subTopic.getName();
			listTopicUsedInForm.add(Topic.copyIdandName(subTopic.getId(),name));
			listSubTopicsUsedInForm(listTopicUsedInForm,subTopic, subLevel);
		}
		
	
	}

	public Topic save(Topic topic) {
		Topic parent = topic.getParent();
		if (parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId()) + "-";
			topic.setAllParentIDs(allParentIds);
		}
		if (topic != null && null != topic.getAlias()) {
			topic.setAlias(topic.getAlias().toLowerCase().replaceAll(" ", "_"));		
		}
	
		return topicRepo.save(topic);
	}

	public Topic getTopicById(Integer id) throws TopicNotFoundException {

	try {
			return topicRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new TopicNotFoundException("Could not find any topic by ID:" + id);
		}
	}
	
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id==null || alias==null);
		
		Topic topicByName = topicRepo.findByName(name);
		
		if(isCreatingNew) {
			if(topicByName != null) {
				return "DuplicateName";
			}else {
				Topic topicAlias = topicRepo.findByAlias(alias);
				if (topicAlias != null) {
					return "DuplicateAlias";
				}
			}
		} else {
			if (topicByName != null && topicByName.getId() != id) {
			return "DuplicateName";	
			}
			Topic topicByAlias = topicRepo.findByAlias(alias);
			if (topicByAlias != null && topicByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		
		return "OK";
	}
	private Set<Topic> sortSubTopics(Set<Topic> children){
		return sortSubTopics(children,"asc");
	}
	private Set<Topic> sortSubTopics(Set<Topic> children, String sortDir){
		SortedSet<Topic> sortedChildren = new TreeSet<>(new Comparator<Topic>() {

			@Override
			public int compare(Topic cat1, Topic cat2) {
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
		topicRepo.updateEnabledStatus(id, status);
	}		 
	
	public void deleteTopic(Integer id) throws TopicNotFoundException {
		Long countById = topicRepo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new TopicNotFoundException("Could not find any topic by ID:" + id);
		}
		topicRepo.deleteById(id);
	}
	
	public List<Topic> getAllTopicForExport(){
	List<Topic> listTopic = new ArrayList<>();	
		Iterable<Topic> catgoriesDB = topicRepo.findAll(Sort.by("name").ascending());
		for (Topic topic : catgoriesDB) {
			if (topic.getParent() == null) {
				listTopic.add(Topic.copyFull(topic));
				Set<Topic> catSet = sortSubTopics(topic.getChildren());
				for (Topic subCat : catSet) {
					String name = "--"+subCat.getName();
					listTopic.add(Topic.copyFull(subCat));
					listSubTopicsUsedInForm(listTopic,subCat,1);
				}
			}
		}
		
		return listTopic;	
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
