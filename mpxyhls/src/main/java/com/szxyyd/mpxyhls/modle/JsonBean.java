package com.szxyyd.mpxyhls.modle;

import java.util.List;

/**
 * Created by jq on 2016/7/13.
 */
public class JsonBean {
    private List<OrderList> orderList; //订单列表
    private List<NurseTrain> nurseTrain; //培训列表
    public JsonBean() {
        super();
    }

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }

    public List<NurseTrain> getNurseTrain() {
        return nurseTrain;
    }

    public void setNurseTrain(List<NurseTrain> nurseTrain) {
        this.nurseTrain = nurseTrain;
    }
}
