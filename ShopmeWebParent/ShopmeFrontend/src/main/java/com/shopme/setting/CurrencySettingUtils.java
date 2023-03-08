package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingUtils;

import java.util.List;

public class CurrencySettingUtils extends SettingUtils {

	public CurrencySettingUtils(List<Setting> listSettings) {
		super(listSettings);
	}

	public String getSymbol() {
		return super.getValue("CURRENCY_SYMBOL");
	}
	
}
