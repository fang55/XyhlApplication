package com.szxyyd.xyhl.modle;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 用户的基本信息
 * @author jq
 *
 */
public class User implements Serializable{
	private String id;
	private String icon;
	private String nickname;
	private String user;
	private String regdate;
	private String pwd;
	private String mobile;
	public User(){

	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
