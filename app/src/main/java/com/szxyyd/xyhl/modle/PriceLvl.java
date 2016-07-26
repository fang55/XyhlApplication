package com.szxyyd.xyhl.modle;

import java.io.Serializable;

/**
 * Created by jq on 2016/6/12.
 */
public class PriceLvl implements Serializable{
    private String content;
    private String id;
    private String lvl;
    private int price;
    private String name;
    private String dscid;
    private String svrid;
    private String lvlid;
    private String city;

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
}
