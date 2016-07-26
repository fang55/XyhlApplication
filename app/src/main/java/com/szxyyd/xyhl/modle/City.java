package com.szxyyd.xyhl.modle;

public class City {
   private String id;
   private String iid;
   private String name;
public City() {
	super();
}
public City(String id, String iid, String name) {
	super();
	this.id = id;
	this.iid = iid;
	this.name = name;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getIid() {
	return iid;
}
public void setIid(String iid) {
	this.iid = iid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
   
}
