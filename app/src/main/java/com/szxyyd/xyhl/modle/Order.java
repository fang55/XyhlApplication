package com.szxyyd.xyhl.modle;

import java.io.Serializable;

public class Order implements Serializable{
    private String cstpaysum;
    private String atcstpay;
    private String sex;
    private String atarrival;
    private String status;
    private String spccond;
    private String svrname;
    private String nursename;
    private String addr;
    private String id;
    private String cstid;
    private String num;
    private String ordname;
    private String codename;
	private String nurseid;
	private String mobile;
	private String icon;
	private String atsign;
	public Order() {
		super();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCstpaysum() {
		return cstpaysum;
	}

	public void setCstpaysum(String cstpaysum) {
		this.cstpaysum = cstpaysum;
	}

	public String getAtcstpay() {
		return atcstpay;
	}

	public void setAtcstpay(String atcstpay) {
		this.atcstpay = atcstpay;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAtarrival() {
		return atarrival;
	}

	public void setAtarrival(String atarrival) {
		this.atarrival = atarrival;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpccond() {
		return spccond;
	}

	public void setSpccond(String spccond) {
		this.spccond = spccond;
	}

	public String getSvrname() {
		return svrname;
	}

	public void setSvrname(String svrname) {
		this.svrname = svrname;
	}

	public String getNursename() {
		return nursename;
	}

	public void setNursename(String nursename) {
		this.nursename = nursename;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCstid() {
		return cstid;
	}

	public void setCstid(String cstid) {
		this.cstid = cstid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getOrdname() {
		return ordname;
	}

	public void setOrdname(String ordname) {
		this.ordname = ordname;
	}

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getNurseid() {
		return nurseid;
	}

	public void setNurseid(String nurseid) {
		this.nurseid = nurseid;
	}
	public String getAtsign() {
		return atsign;
	}
	public void setAtsign(String atsign) {
		this.atsign = atsign;
	}
}
