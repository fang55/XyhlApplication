package com.szxyyd.mpxyhl.modle;

import java.util.List;

/**
 * 首页-护理师选项
 * @author jq
 *
 */
public class NurseType {
	private List<SvrBean> svr;
	public List<SvrBean> getSvr() {
		return svr;
	}
	public void setSvr(List<SvrBean> svr) {
		this.svr = svr;
	}
	public static class SvrBean {
		private int id;
		private String status;
		private String defprice;
		private String defcity;
		private String name;
		private String deflvl;
		private String catid;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
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
	}

}
