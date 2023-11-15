package com.softlond.base.response;

import com.softlond.base.entity.InformacionUsuario;

public class UserDetailResponse {

	private InformacionUsuario userAux;
	private Integer idActivatedPlan;
	private String nameActivatedPlan;
	private Integer idActivatedProfile;
	private String nameActivatedProfile;
	private String nameCreator;

	public UserDetailResponse() {
		super();
	}

	public UserDetailResponse(InformacionUsuario userAux, Integer idActivatedPlan, String nameActivatedPlan,
			Integer idActivatedProfile, String nameActivatedProfile, String nameCreator) {
		super();
		this.userAux = userAux;
		this.idActivatedPlan = idActivatedPlan;
		this.nameActivatedPlan = nameActivatedPlan;
		this.idActivatedProfile = idActivatedProfile;
		this.nameActivatedProfile = nameActivatedProfile;
		this.nameCreator = nameCreator;
	}

	public Integer getIdActivatedProfile() {
		return idActivatedProfile;
	}

	public void setIdActivatedProfile(Integer idActivatedProfile) {
		this.idActivatedProfile = idActivatedProfile;
	}

	public String getNameActivatedProfile() {
		return nameActivatedProfile;
	}

	public void setNameActivatedProfile(String nameActivatedProfile) {
		this.nameActivatedProfile = nameActivatedProfile;
	}

	public InformacionUsuario getUserAux() {
		return userAux;
	}

	public void setUserAux(InformacionUsuario userAux) {
		this.userAux = userAux;
	}

	public Integer getIdActivatedPlan() {
		return idActivatedPlan;
	}

	public void setIdActivatedPlan(Integer idActivatedPlan) {
		this.idActivatedPlan = idActivatedPlan;
	}

	public String getNameActivatedPlan() {
		return nameActivatedPlan;
	}

	public void setNameActivatedPlan(String nameActivatedPlan) {
		this.nameActivatedPlan = nameActivatedPlan;
	}

	public String getNameCreator() {
		return nameCreator;
	}

	public void setNameCreator(String nameCreator) {
		this.nameCreator = nameCreator;
	}

}
