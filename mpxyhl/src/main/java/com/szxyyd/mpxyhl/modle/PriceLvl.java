package com.szxyyd.mpxyhl.modle;

import java.io.Serializable;

/**
 * Created by jq on 2016/6/12.
 */
public class PriceLvl implements Serializable{
    private int price;
    private String content;
    private String id;
    private String lvl;
    private String name;
    private String dscid;
    private String svrid;
    private String lvlid;
    private String city;
    private String idx;
    private String remark;
    private String type;
    private String method;
    public PriceLvl() {
        super();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDscid() {
        return dscid;
    }

    public void setDscid(String dscid) {
        this.dscid = dscid;
    }

    public String getSvrid() {
        return svrid;
    }

    public void setSvrid(String svrid) {
        this.svrid = svrid;
    }

    public String getLvlid() {
        return lvlid;
    }

    public void setLvlid(String lvlid) {
        this.lvlid = lvlid;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
