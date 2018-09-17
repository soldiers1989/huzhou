package com.huzhou.gjj.bean;

import java.io.Serializable;

public class LoanHistory  implements Serializable {
	public void setDate(String date) {
		this.date = date;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public void setFa_interest(String fa_interest) {
		this.fa_interest = fa_interest;
	}

	public void setHuan_date(String huan_date) {
		this.huan_date = huan_date;
	}

	private  String date;
	private  String water;
	private  String number;
	private  String explain;
	private  String balance;
	private  String principal;
	private  String interest;
	private  String fa_interest;
	private  String huan_date;

	public String getDate() {
		return date;
	}

	public String getWater() {
		return water;
	}

	public String getNumber() {
		return number;
	}

	public String getExplain() {
		return explain;
	}

	public String getBalance() {
		return balance;
	}

	public String getPrincipal() {
		return principal;
	}

	public String getInterest() {
		return interest;
	}

	public String getFa_interest() {
		return fa_interest;
	}

	public String getHuan_date() {
		return huan_date;
	}

	public LoanHistory(String date, String water, String number, String explain, String balance, String principal, String interest, String fa_interest, String huan_date) {

		this.date = date;
		this.water = water;
		this.number = number;
		this.explain = explain;
		this.balance = balance;
		this.principal = principal;
		this.interest = interest;
		this.fa_interest = fa_interest;
		this.huan_date = huan_date;
	}
}
