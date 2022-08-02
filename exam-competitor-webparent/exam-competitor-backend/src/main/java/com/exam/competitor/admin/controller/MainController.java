package com.exam.competitor.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.exam.competitor.admin.common.entity.Currency;
import com.exam.competitor.admin.common.entity.setting.Setting;
import com.exam.competitor.admin.repo.CurrencyRepository;
import com.exam.competitor.admin.service.SettingService;

@Controller
public class MainController {
	
	@Autowired private SettingService service;
	
	@Autowired private CurrencyRepository currencyRepo;

	
	@GetMapping("/")
	public String viewHomePage(Model model) {
		/*
		 * List<Setting> listSettings = service.listAllSettings(); List<Currency>
		 * listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		 * 
		 * model.addAttribute("listCurrencies", listCurrencies);
		 * 
		 * for (Setting setting : listSettings) { model.addAttribute(setting.getKey(),
		 * setting.getValue()); }
		 */
	
		return "dashboard";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
			
		return "redirect:/";
	}

}
