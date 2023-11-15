package com.softlond.base.response;

import java.util.List;

import com.softlond.base.entity.Menu;
import com.softlond.base.entity.Modulo;

public class JsonModuleMenuModel {

	Modulo module;
	List<Menu> listMenu;

	public JsonModuleMenuModel(Modulo module, List<Menu> listMenu) {
		super();
		this.module = module;
		this.listMenu = listMenu;
	}

	public JsonModuleMenuModel() {
		super();
	}

	public Modulo getModule() {
		return module;
	}

	public void setModule(Modulo module) {
		this.module = module;
	}

	public List<Menu> getListMenu() {
		return listMenu;
	}

	public void setListMenu(List<Menu> listMenu) {
		this.listMenu = listMenu;
	}
	
	
}
