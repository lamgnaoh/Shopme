package com.shopme.setting;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
	@Autowired
	private SettingRepository settingRepo;
	@Autowired
	private CurrencyRepository currencyRepo;
	

	public List<Setting> getGeneralSettings() {
		return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	public EmailSettingUtils getEmailSettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingUtils(settings);
	}
	public CurrencySettingUtils getCurrencySettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingUtils(settings);
	}

	public PaymentSettingUtils getPaymentSettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingUtils(settings);
	}

	public String getCurrencyCode() {
		Setting setting = settingRepo.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepo.findById(currencyId).get();

		return currency.getCode();
	}

	
}
