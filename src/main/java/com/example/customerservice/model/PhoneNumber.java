package com.example.customerservice.model;

import javax.persistence.Embeddable;

@Embeddable
public class PhoneNumber {

	private String type;
	private String phoneNumber;
	private Boolean activate;
	
	public PhoneNumber() {
		super();
	}

	public PhoneNumber(String type, String phoneNumber, Boolean activate) {
		super();
		this.type = type;
		this.phoneNumber = phoneNumber;
		this.activate = activate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getActivate() {
		return activate;
	}

	public void setActivate(Boolean activate) {
		this.activate = activate;
	}

	@Override
	public String toString() {
		return "PhoneNumber [type=" + type + ", phoneNumber=" + phoneNumber + ", activate=" + activate + "]";
	}
	
	
	
}
