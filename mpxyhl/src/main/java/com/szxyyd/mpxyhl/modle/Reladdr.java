package com.szxyyd.mpxyhl.modle;

import java.io.Serializable;

public class Reladdr implements Serializable{
	private int id;
	private String unt;
	private String cstid;
	private String app;
	private String ifdef;
	private String addr;
	private String town;
	private String district;
	private String mobile;
	private String name;
	public Reladdr() {
		super();
	}
	public Reladdr(int id, String unt, String cstid, String app, String ifdef,
				   String addr, String town, String district, String mobile,
				   String name) {
		super();
		this.id = id;
		this.unt = unt;
		this.cstid = cstid;
		this.app = app;
		this.ifdef = ifdef;
		this.addr = addr;
		this.town = town;
		this.district = district;
		this.mobile = mobile;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUnt() {
		return unt;
	}
	public void setUnt(String unt) {
		this.unt = unt;
	}
	public String getCstid() {
		return cstid;
	}
	public void setCstid(String cstid) {
		this.cstid = cstid;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getIfdef() {
		return ifdef;
	}
	public void setIfdef(String ifdef) {
		this.ifdef = ifdef;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
      
}
