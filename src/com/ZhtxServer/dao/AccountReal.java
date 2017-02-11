package com.ZhtxServer.dao;

public class AccountReal {
    private int id;
    private String class_1,class_2,num,proid,debt;
    private java.sql.Date date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClass_1() {
		return class_1;
	}
	public void setClass_1(String class_1) {
		this.class_1 = class_1;
	}
	public String getClass_2() {
		return class_2;
	}
	public void setClass_2(String class_2) {
		this.class_2 = class_2;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	public String getDebt() {
		return debt;
	}
	public void setDebt(String debt) {
		this.debt = debt;
	}
	public java.sql.Date getDate() {
		return date;
	}
	public void setDate(java.sql.Date date) {
		this.date = date;
	}
}
