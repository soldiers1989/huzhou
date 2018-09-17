package com.huzhou.gjj.bean;

import java.io.Serializable;

public class Account  implements Serializable {
	private String id;
	private String name;
	private String type;
	private String number;

	public Account(String id, String name, String type, String number,
			String date, String sign, String incurred, String balance) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.number = number;
		this.date = date;
		this.sign = sign;
		this.balance = balance;
		this.incurred = incurred;
	}

	private String date;
	private String sign;
	private String balance;
	private String incurred;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getIncurred() {
		return incurred;
	}

	public void setIncurred(String incurred) {
		this.incurred = incurred;
	}

}
