package com.shopme.common.entity;

import com.shopme.common.entity.Setting;

import java.util.List;

public class SettingUtils {
	private List<Setting> listSettings;

	public SettingUtils(List<Setting> listSettings) {
		this.listSettings = listSettings;
	}
	
	public Setting get(String key) {
		int index = listSettings.indexOf(new Setting(key));
		if (index >= 0) {
			return listSettings.get(index);
		}
		
		return null;
	}
	
	public String getValue(String key) {
		Setting setting = get(key);
		if (setting != null) {
			return setting.getValue();
		}
		
		return null;
	}
	
	public void update(String key, String value) {
		Setting setting = get(key);
		if (setting != null && value != null) {
			setting.setValue(value);
		}
	}
	
	public List<Setting> list() {
		return listSettings;
	}
	
}
