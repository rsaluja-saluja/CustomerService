package com.example.customerservice.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ActiveNumber {

	@NotEmpty(message = "Please provide Phone Number type")
	private String type;

	@NotNull(message = "Please provide Phone Number Activate status(true/false)")
	private Boolean activate;

	public ActiveNumber() {
		super();
	}

	public ActiveNumber(String type, Boolean activate) {
		super();
		this.type = type;
		this.activate = activate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getActivate() {
		return activate;
	}

	public void setActivate(Boolean activate) {
		this.activate = activate;
	}

	@Override
	public String toString() {
		return "ActiveNumber [type=" + type + ", activate=" + activate + "]";
	}

}
