package com.szxyyd.xyhl.modle;

import java.io.Serializable;

public class NurseList implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String sex;
	private String birth;
	private String city;
	private int srvscore; //星数
	private String name;
	private String edu;
	private String nation;
	private String married;
	private String svrid;
	private String lvl;
	private String nursvrid;
	private String srvyears;
	private String icon;
	private String price;
	private String star;
	private String content;
	private String srvnum;
	private String spcty; //特长
	private String distance; //距离
	private String atpub;
	public NurseList() {
		super();
	}

	public String getSrvnum() {
		return srvnum;
	}

	public String getSpcty() {
		return spcty;
	}

	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public void setSpcty(String spcty) {
		this.spcty = spcty;
	}

	public void setSrvnum(String srvnum) {
		this.srvnum = srvnum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getSrvscore() {
		return srvscore;
	}

	public void setSrvscore(int srvscore) {
		this.srvscore = srvscore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public String getSvrid() {
		return svrid;
	}

	public void setSvrid(String svrid) {
		this.svrid = svrid;
	}

	public String getLvl() {
		return lvl;
	}

	public void setLvl(String lvl) {
		this.lvl = lvl;
	}

	public String getNursvrid() {
		return nursvrid;
	}

	public void setNursvrid(String nursvrid) {
		this.nursvrid = nursvrid;
	}

	public String getSrvyears() {
		return srvyears;
	}

	public void setSrvyears(String srvyears) {
		this.srvyears = srvyears;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAtpub() {
		return atpub;
	}

	public void setAtpub(String atpub) {
		this.atpub = atpub;
	}
}
