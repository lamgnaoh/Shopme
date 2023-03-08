package com.shopme.setting;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingUtils;

import java.util.List;

public class PaymentSettingUtils extends SettingUtils {

	public PaymentSettingUtils(List<Setting> listSettings) {
		super(listSettings);
	}

	public String getURL() {
		return super.getValue("PAYPAL_API_BASE_URL");
	}
	
	public String getClientID() {
		return super.getValue("PAYPAL_API_CLIENT_ID");
	}
	
	public String getClientSecret() {
		return super.getValue("PAYPAL_API_CLIENT_SECRET");
	}
}
