package com.softlond.base.response;

public class UserInfoSimpleResponse {

	private Integer id;
	private	String completeName;

	public UserInfoSimpleResponse() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompleteName() {
		return completeName;
	}

	public void setCompleteName(String completeName) {
		this.completeName = completeName;
	}

}
