package com.szxyyd.xyhl.modle;

public class Level {
	private int id;
	private String lvl;
	private String name;
	private String svrid;
	private String idx;

	public Level() {
		super();
	}
	public Level(int id, String lvl, String name, String svrid, String idx) {
		super();
		this.id = id;
		this.lvl = lvl;
		this.name = name;
		this.svrid = svrid;
		this.idx = idx;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLvl() {
		return lvl;
	}
	public void setLvl(String lvl) {
		this.lvl = lvl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSvrid() {
		return svrid;
	}
	public void setSvrid(String svrid) {
		this.svrid = svrid;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}


}
