package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Country;
import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.exception.CustomerNotFoundException;
import com.exam.competitor.admin.export.CustomerCsvExporter;
import com.exam.competitor.admin.service.CustomerService;

@Controller
public class CustomerController {
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	
	@Autowired private CustomerService customerService;
	

	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return getAllCustomersListByPage(1, model, "firstName", "asc", null);
	}

	/*
	 * @GetMapping("/customers/page/{pageNum}") public String listByPage(
	 * 
	 * @PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers")
	 * PagingAndSortingHelper helper,
	 * 
	 * @PathVariable(name = "pageNum") int pageNum) {
	 * 
	 * service.listByPage(pageNum, helper);
	 * 
	 * return "customers/customers"; }
	 */
	@GetMapping("/customers/page/{pageNum}")
	public String getAllCustomersListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Customer> pageInfo = customerService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Customer> listCustomers = pageInfo.getContent();
		
		long startCount = (pageNum-1) * customerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + customerService.CUSTOMERS_PER_PAGE - 1;
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
		model.addAttribute("sortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCustomers", listCustomers);
		model.addAttribute("revSortDir", reverseSortDir);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		customerService.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Customer ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return defaultRedirectURL;
	}	
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			model.addAttribute("customer", customer);
			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;		
		}
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = customerService.get(id);
			List<Country> countries = customerService.listAllCountries();
			
			model.addAttribute("listCountries", countries);			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Edit Customer (ID: %d)", id));
			
			return "customers/customer_form";
			
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes ra) {
		customerService.save(customer);
		ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been updated successfully.");
		return defaultRedirectURL;
	}

	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			customerService.delete(id);			
			ra.addFlashAttribute("message", "The customer ID " + id + " has been deleted successfully.");
			
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/customers/export/csv/{pageNum}")
	public void exportToCsv(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword, HttpServletResponse response) throws IOException {
		
		Page<Customer> page = customerService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Customer> listCustomers = page.getContent();
		
		CustomerCsvExporter exporter = new CustomerCsvExporter();
		exporter.export(listCustomers, response);
		
	}
	
}
