package com.szxyyd.mpxyhl.modle;

import java.io.Serializable;
import java.util.List;

/**
 * 用户的基本信息
 * @author jq
 *
 */
public class User implements Serializable{
	/**
	 * unt : 心悦云端信息技术有限公司
	 * app : 母婴护理交易云系统
	 * icon : 15712956260.png
	 * objtype : 客户
	 * objid : 6
	 * status : 正常
	 * nickname : xy_15712956260
	 * roleid : 心悦妈妈
	 * loginnum : 816
	 * lastip : 120.236.31.162
	 * id : 80001917
	 * pwd : 202CB962AC59075B964B07152D234B70
	 * lasttime : 1469414123
	 * regdate : 1466684265
	 * usr : 15712956260
	 * qq : .
	 * mobile : 15712956260
	 */
	private List<UsrBean> usr;
	public List<UsrBean> getUsr() {
		return usr;
	}
	public void setUsr(List<UsrBean> usr) {
		this.usr = usr;
	}
	public static class UsrBean {
		private String icon;
		private String objtype;
		private String objid;
		private String status;
		private String nickname;
		private String roleid;
		private String loginnum;
		private String lastip;
		private String id;
		private String pwd;
		private String lasttime;
		private String regdate;
		private String usr;
		private String qq;
		private String mobile;
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getObjtype() {
			return objtype;
		}
		public void setObjtype(String objtype) {
			this.objtype = objtype;
		}
		public String getObjid() {
			return objid;
		}

		public void setObjid(String objid) {
			this.objid = objid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getRoleid() {
			return roleid;
		}

		public void setRoleid(String roleid) {
			this.roleid = roleid;
		}

		public String getLoginnum() {
			return loginnum;
		}

		public void setLoginnum(String loginnum) {
			this.loginnum = loginnum;
		}

		public String getLastip() {
			return lastip;
		}

		public void setLastip(String lastip) {
			this.lastip = lastip;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getLasttime() {
			return lasttime;
		}

		public void setLasttime(String lasttime) {
			this.lasttime = lasttime;
		}

		public String getRegdate() {
			return regdate;
		}

		public void setRegdate(String regdate) {
			this.regdate = regdate;
		}

		public String getUsr() {
			return usr;
		}

		public void setUsr(String usr) {
			this.usr = usr;
		}

		public String getQq() {
			return qq;
		}

		public void setQq(String qq) {
			this.qq = qq;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
	}
}
