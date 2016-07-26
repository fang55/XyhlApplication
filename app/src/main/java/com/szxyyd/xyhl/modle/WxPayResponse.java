package com.szxyyd.xyhl.modle;

/**
 * 微信支付对象
 * Created by jq on 2016/6/21.
 */
public class WxPayResponse {
    private String appid; //微信签约的ID
    private String partnerid; //微信收款账号
    private String prepayid; //预支付ID微信
    private String packageX; //微信包名值
    private String Noncestr; //防重复随机值
    private String timestamp; //时间戳
    private String sign; //签名

    public WxPayResponse() {
        super();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return Noncestr;
    }

    public void setNoncestr(String noncestr) {
        Noncestr = noncestr;
    }
}
