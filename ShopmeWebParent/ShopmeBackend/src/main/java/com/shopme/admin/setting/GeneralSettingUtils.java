package com.shopme.admin.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingUtils;

import java.util.List;

public class GeneralSettingUtils extends SettingUtils {

	public GeneralSettingUtils(List<Setting> listSettings) {
		super(listSettings);
	}

	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}
	
	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
}
