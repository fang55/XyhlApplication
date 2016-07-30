package com.szxyyd.mpxyhl.modle;

import java.util.List;

/**
 * Created by jq on 2016/7/4.
 */
public class JsonBean {
    private List<NurseType> svr;  //首页
    private List<PriceLvl> yxvPriceLvl; //级别
    private List<PriceLvl> svrFun;   //选项
    private List<PriceLvl> svrCal;  //服务人员
    private List<Reladdr> reladdr; //服务地址
    private List<NurseList> nurse; //护理师列表
    private List<DetailFile> files; //证书、生活照
    private List<NurseTrain> nurseTrain; //护理师培训
    private List<City> origo; //获取籍贯
    private List<City> city; //获取城市
    private List<City> county; //获取城市市区
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
    public List<NurseType> getSvr() {
        return svr;
    }
    public void setSvr(List<NurseType> svr) {
        this.svr = svr;
    }

    public List<PriceLvl> getYxvPriceLvl() {
        return yxvPriceLvl;
    }
    public void setYxvPriceLvl(List<PriceLvl> yxvPriceLvl) {
        this.yxvPriceLvl = yxvPriceLvl;
    }

    public List<PriceLvl> getSvrFun() {
        return svrFun;
    }

    public void setSvrFun(List<PriceLvl> svrFun) {
        this.svrFun = svrFun;
    }

    public List<PriceLvl> getSvrCal() {
        return svrCal;
    }

    public void setSvrCal(List<PriceLvl> svrCal) {
        this.svrCal = svrCal;
    }

    public List<Reladdr> getReladdr() {
        return reladdr;
    }

    public void setReladdr(List<Reladdr> reladdr) {
        this.reladdr = reladdr;
    }

    public List<DetailFile> getFiles() {
        return files;
    }

    public void setFiles(List<DetailFile> files) {
        this.files = files;
    }

    public List<NurseTrain> getNurseTrain() {
        return nurseTrain;
    }

    public void setNurseTrain(List<NurseTrain> nurseTrain) {
        this.nurseTrain = nurseTrain;
    }

    public List<NurseList> getNurse() {
        return nurse;
    }

    public void setNurse(List<NurseList> nurse) {
        this.nurse = nurse;
    }

    public List<City> getOrigo() {
        return origo;
    }

    public void setOrigo(List<City> origo) {
        this.origo = origo;
    }

    public List<City> getCity() {
        return city;
    }
    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<City> getCounty() {
        return county;
    }

    public void setCounty(List<City> county) {
        this.county = county;
    }
}
