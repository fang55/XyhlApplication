package com.szxyyd.xyhl.modle;

public class SvrCal {
	private String id;
	private String lvl;
	private String remark;
	private String name;
	private String svrid;
	private String type;
	private String idx;
	private String method;
	public SvrCal() {
		super();
	}
	public SvrCal(String id, String lvl, String remark, String name,
				  String svrid, String type, String idx, String method) {
		super();
		this.id = id;
		this.lvl = lvl;
		this.remark = remark;
		this.name = name;
		this.svrid = svrid;
		this.type = type;
		this.idx = idx;
		this.method = method;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLvl() {
		return lvl;
	}
	public void setLvl(String lvl) {
		this.lvl = lvl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
