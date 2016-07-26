package com.szxyyd.mpxyhls.modle;

import java.io.Serializable;

/**
 * Created by jq on 2016/6/14.
 */
public class Nurse implements Serializable {
    private int id;
    private String forlang; //外语未定义
    private String sex;   //性别未定义
    private String city;  //深圳市
    private String married; //婚别未定义
    private String zodiac; //星座未定义
    private String name;
    private String anmsign; //属相未定义
    private String district; //城市未定义
    private String nation;  //民族未定义
    private String origo;  //籍贯未定义
    private String edu;
    private String forlvl; //差
    private String chact;       //性格未定义
    private String mobile;       //性格未定义
    private String status;       //正常
    private String srvnum;       //接单数
    private String srvscore;       //星级
    private String fvrnur;       //收藏数
    private String srvyears;       //0
    private String lvl;
    private String svrid;
    private String nursvrid;
    private String icon;
    private String pid;
    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNursvrid() {
        return nursvrid;
    }

    public void setNursvrid(String nursvrid) {
        this.nursvrid = nursvrid;
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
    public Nurse() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFvrnur() {
        return fvrnur;
    }

    public void setFvrnur(String fvrnur) {
        this.fvrnur = fvrnur;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getChact() {
        return chact;
    }

    public void setChact(String chact) {
        this.chact = chact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSrvnum() {
        return srvnum;
    }

    public void setSrvnum(String srvnum) {
        this.srvnum = srvnum;
    }

    public String getSrvscore() {
        return srvscore;
    }

    public void setSrvscore(String srvscore) {
        this.srvscore = srvscore;
    }

    public String getSrvyears() {
        return srvyears;
    }

    public void setSrvyears(String srvyears) {
        this.srvyears = srvyears;
    }

    public String getForlvl() {
        return forlvl;
    }

    public void setForlvl(String forlvl) {
        this.forlvl = forlvl;
    }

    public String getForlang() {
        return forlang;
    }

    public void setForlang(String forlang) {
        this.forlang = forlang;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnmsign() {
        return anmsign;
    }

    public void setAnmsign(String anmsign) {
        this.anmsign = anmsign;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getOrigo() {
        return origo;
    }

    public void setOrigo(String origo) {
        this.origo = origo;
    }
}
