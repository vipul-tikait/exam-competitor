package com.exam.competitor.admin.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.setting.Setting;
import com.exam.competitor.admin.common.entity.setting.SettingCategory;
import com.exam.competitor.admin.repo.SettingRepository;
import com.exam.competitor.admin.setting.GeneralSettingBag;

@Service
public class SettingService {
	@Autowired private SettingRepository repo;
	
	public List<Setting> listAllSettings() {
		return (List<Setting>) repo.findAll();
	}
	
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);
		/*
		 * List<Setting> mailServerSettings =
		 * repo.findByCategory(SettingCategory.MAIL_SERVER); List<Setting>
		 * mailTemplateSettings = repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
		 */
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		/*
		 * settings.addAll(mailServerSettings); settings.addAll(mailTemplateSettings);
		 */
		return new GeneralSettingBag(settings);
	}
	
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}
	
	public List<Setting> getMailServerSettings() {
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getMailTemplateSettings() {
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}	
	
	public List<Setting> getCurrencySettings() {
		return repo.findByCategory(SettingCategory.CURRENCY);
	}
	
	public List<Setting> getPaymentSettings() {
		return repo.findByCategory(SettingCategory.PAYMENT);
	}	

	
}