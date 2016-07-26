package com.szxyyd.xyhl.modle;
/**
 * 护理师选项
 * @author jq
 *
 */
public class NurseType {
	 private int id;
     private String app;
     private String status;
     private String unit;
     private String defprice;
     private String defcity;
     private String name;
     private String deflvl;
     private String catid;
     private String defprcover;
	public NurseType() {
		super();
	}
	public NurseType(int id, String app, String status,
			String unit, String defprice, String defcity, String name,
			String deflvl, String catid, String defprcover) {
		super();
		this.id = id;
		this.app = app;
		this.status = status;
		this.unit = unit;
		this.defprice = defprice;
		this.defcity = defcity;
		this.name = name;
		this.deflvl = deflvl;
		this.catid = catid;
		this.defprcover = defprcover;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDefprice() {
		return defprice;
	}
	public void setDefprice(String defprice) {
		this.defprice = defprice;
	}
	public String getDefcity() {
		return defcity;
	}
	public void setDefcity(String defcity) {
		this.defcity = defcity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeflvl() {
		return deflvl;
	}
	public void setDeflvl(String deflvl) {
		this.deflvl = deflvl;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getDefprcover() {
		return defprcover;
	}
	public void setDefprcover(String defprcover) {
		this.defprcover = defprcover;
	}
}
