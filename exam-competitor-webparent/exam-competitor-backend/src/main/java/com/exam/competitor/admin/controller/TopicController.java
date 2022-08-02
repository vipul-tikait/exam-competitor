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
import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.exception.TopicNotFoundException;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.export.TopicCsvExpoter;
import com.exam.competitor.admin.export.TopicExcelExpoter;
import com.exam.competitor.admin.export.TopicPdfExpoter;
import com.exam.competitor.admin.service.TopicService;
import com.exam.competitor.admin.topic.TopicPageInfo;

@Controller
public class TopicController {

	@Autowired
	TopicService topicService;

	@GetMapping("/topics")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, "id", "asc", model, null);
	}

	@GetMapping("/topics/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortField") String sortField,
			@Param("sortDir") String sortDir, Model model, @Param("keyword") String keyword) {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		TopicPageInfo pageInfo = new TopicPageInfo();
		List<Topic> listTopics = topicService.listByPage(pageInfo, pageNum, sortField, sortDir, keyword);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		long startCount = (pageNum - 1) * topicService.ROOT_TOPIC_PER_PAGE + 1;
		long endCount = startCount + topicService.ROOT_TOPIC_PER_PAGE - 1;
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
		model.addAttribute("listTopics", listTopics);
		model.addAttribute("reverseSortDir", reverseSortDir);

		// return listByPage(1, model, "name", "asc", null);//"users";
		// return listByPage(pageNum, reverseSortDir, model);
		return "topics/topics";
		// return "categories/topics";
	}

	@GetMapping("/topics/new")
	public String createNewTopic(Model model) {

		List<Topic> listTopic = topicService.listTopicUsedInForm();
		Topic topic = new Topic();
		topic.setEnabled(true);
		model.addAttribute("topic", topic);
		model.addAttribute("listTopic", listTopic);
		model.addAttribute("pageTitle", "Create New Topic");
		return "topics/topic_form";
	}

	@PostMapping("/topics/save")
	public String saveTopic(Topic topic, @RequestParam("topicFileImage") MultipartFile file, RedirectAttributes ra)
			throws IOException {

		if (!file.isEmpty()) {

			String fileName = StringUtils.cleanPath(file.getOriginalFilename()).replaceAll("\\s", "");
			topic.setImage(fileName);

			Topic savedTopic = topicService.save(topic);

			String uploadDir = "../topic-images/" + savedTopic.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		} else {
			topicService.save(topic);
		}
		ra.addFlashAttribute("message", "Topic has been added successfuly!");
		return "redirect:/topics";
	}

	@GetMapping("/topics/edit/{id}")
	public String editTopic(@Param("sortDir") String sortDir, @PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			if (sortDir == null || sortDir.isEmpty()) {
				sortDir = "asc";
			}

			List<Topic> listTopics = topicService.listTopicUsedInForm();
			Topic topic = topicService.getTopicById(id);
			model.addAttribute("topic", topic);
			model.addAttribute("listTopics", listTopics);
			model.addAttribute("pageTitle", "Edit Topic (ID - " + id + ")");
			return "topics/topic_form";
		} catch (TopicNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/topics";
		}

	}

	@GetMapping("/topics/{id}/enabled/{status}")
	public String enableTopicStatus(@PathVariable(name = "id") Integer id, @PathVariable("status") Boolean status,
			Model model, RedirectAttributes redirectAttributes) {

		topicService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The Category id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/topics";

	}

	@GetMapping("/topics/delete/{id}")
	public String deletTopic(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			topicService.deleteTopic(id);
			String topicDir = "../topic-images/" + id;
			FileUploadUtil.removeDir(topicDir);
			redirectAttributes.addFlashAttribute("message",
					"Ths Topic with id " + id + " has been deleted successfully.");

		} catch (TopicNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/topics";

	}

	@GetMapping("/topics/export/csv/{pageNum}")
	public void exportToCsv(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		} else {
			sortDir = sortDir == "asc" ? "desc" : "asc";
		}

		List<Topic> listTopics = topicService.listTopicToExport(pageNum, sortDir, keyword);

		TopicCsvExpoter exporter = new TopicCsvExpoter();
		exporter.export(listTopics, httpServletResponse);

	}

	@GetMapping("/topics/export/pdf/{pageNum}")
	public void exportToPdf(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		} else {
			sortDir = sortDir == "asc" ? "desc" : "asc";
		}

		List<Topic> listTopics = topicService.listTopicToExport(pageNum, sortDir, keyword);

		TopicPdfExpoter exporter = new TopicPdfExpoter();
		exporter.export(listTopics, httpServletResponse);

	}

	@GetMapping("/topics/export/excel/{pageNum}")
	public void exportToExcel(@PathVariable(name = "pageNum") Integer pageNum, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, @Param("sortField") String sortField,
			HttpServletResponse httpServletResponse) throws IOException {

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		} else {
			sortDir = sortDir == "asc" ? "desc" : "asc";
		}

		List<Topic> listTopics = topicService.listTopicToExport(pageNum, sortDir, keyword);

		TopicExcelExpoter exporter = new TopicExcelExpoter();
		exporter.export(listTopics, httpServletResponse);

	}

	/*
	 * Page<Topic> page = catService.listByPage(pageNum, sortDir, keyword);
	 * List<Topic> lstCategories = page.getContent();
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
	 * return "categories/topics"; }
	 */
	/*
	 * @PostMapping("/topics/save") public String savecategory(Category category,
	 * RedirectAttributes redirectAttributes,
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
	 * String uploadDir = "../topics-images/" + savedCategory.getId();
	 * FileUploadUtil.cleanDir(uploadDir); FileUploadUtil.saveFile(uploadDir,
	 * fileName, multipartFile); } else { if (category.getImage() == null ||
	 * category.getImage().isEmpty()) category.setImage(null);
	 * catService.save(category); }
	 * 
	 * redirectAttributes.addFlashAttribute("message",
	 * "Category has been saved successfully.");
	 * 
	 * // String emailFirstPart = user.getEmail().split("@")[0]; // return
	 * "redirect:/topics/page/1?sortField=firstName&sortDir=asc&keyword=" +
	 * emailFirstPart; return "redirect:/topics"; }
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
