package com.szxyyd.mpxyhls.activity;

/**
 * Created by jq on 2016/7/12.
 */
public class Constant {
    public static final int TUISONG = 101;
    public static final int SUCCEED = 1;
    public static final int  SUBITM = 2;
    public static final int  FAILURE = 3;
    public static final int  LIST = 4;
    public static final int  DELECT= 5;
    public static String usrId;
    public static String nurId = null;
    public static String nickname = null;
    public static  String BROAD_LIST_ACTION = "com.add.list";
    //档案模块页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    //我的模块页面的常量
    public static final int SET_INFO = 1;  //个人资料
    public static final int SET_REVISE = 2; //修改密码
    public static final int SET_ADVICE= 3; //意见反馈
    public static final int SET_ABOUT= 4; //关于我们
    public static final int SET_CHECK = 5; //检查版本
    public static final int SET_PROTOCOL = 6; //服务协议
   //头像设置
    public static final int TAKE_PICTURE = 0;
    public static final int CHOOSE_PICTURE = 1;

    //外网服务地址
    public static String baseUrl = "http://183.232.35.71:8080/xyhl/";
    public static String baseFileUrl = "http://183.232.35.71:8080/";
    //  图片路径http://192.168.1.131:8080/upload//icon//522701199211222108.jpg
    public static String nurseImage = baseFileUrl+"upload//icon//";
    //  生活照
    public static String lifePic = baseFileUrl+"upload//lifepic//";
    //工作视频
    public static String workvideo = baseFileUrl+"upload//workvideo//";
    //服务器地址
//   public static String baseUrl = "http://120.25.101.140:8080/xyhl/";
    //登录 usr（用户名） pwd（密码）
    public static String loginUrl = baseUrl + "nur?a=login";
    //验证登录 usr（用户名）
    public static String findUsrUrl = baseUrl + "cst?a=findUsr";
    //找回密码 cst?a=getPwd id（用户id）pwd（用户密码）
    public static String getPwdUrl = baseUrl + "cst?a=getPwd";
    //注册护理师 usr（用户名） pwd（密码） nur?a=reg&usr=654321&pwd=654321
    public static String registerUrl = baseUrl + "nur?a=reg";
    //获取验证码  mobile（手机号） cst?a=getVerificationCode&mobile=15071456691
    public static String getVerifiUrl = baseUrl + "cst?a=getVerificationCode";
    //找回密码  usr（用户名）   cst?a=findUsr&usr=111
    public static String findUserUrl = "cst?a=findUsr";
    //培训经历列表  nurseid(护理师id)
    public static String nurseTrainListAddUrl = baseUrl + "nur?a=nurseTrainList";
    //培训经历新增  nur?a=nurseTrainAdd
    public static String nurseTrainAddUrl = baseUrl + "nur?a=nurseTrainAdd";
    //删除培训经历   id（培训经历id）
    public static String nnurseTrainDelUrl = baseUrl + "nur?a=nurseTrainDel";
    //修改培训经历
    public static String nurseTrainUpdUrl = baseUrl + "nur?a=nurseTrainUpd";
    //护理师工作信息修改 id(护理师id) spcty(特长，可多个
    public static String yxNurseUpdWorkUrl = baseUrl + "nur?a=yxNurseUpdWork";
    //获取所有特长
    public static String findAllSpctyUrl = baseUrl + "nur?a=yxNurseUpdWork";
    //护理师基本信息修改
    public static String yxNurseUpdBasicUrl = baseUrl + "nur?a=yxNurseUpdBasic";
    //获取护理师档案里面的所有下拉框数据
    public static String getDetailsUrl = baseUrl + "nur?a=getDetails";
    //获取护理师附件类别
    public static String findFileByNurseidUrl = baseUrl + "nur?a=findFileByNurseid";
    //护理师证件上传
    public static String nurUploadUrl = baseUrl + "nur?a=nurUpload";
    //订单列表
    public static String orderListUrl = baseUrl + "ord?a=mktOrderList";
    //提交订单
    public static String odrespUpdUrl = baseUrl + "ord?a=mktOdrespUpd";
}
