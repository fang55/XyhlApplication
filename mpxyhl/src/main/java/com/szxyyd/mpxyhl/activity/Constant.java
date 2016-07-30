package com.szxyyd.mpxyhl.activity;

import java.util.ArrayList;
import java.util.List;

public class Constant {
	public static final int TUISONG = 101;
	public static final int SUCCEED = 1;
	public static final int  FAILURE = 10;
	public static final int  CANCLE = 20;
	public static final int  DELECT = 30;
	public static final int  SUBITM = 40;
	public static final int  LIST = 50;
	public static final int SERVICE_LEVEL = 100;
	public static final int SERVICE_PEOPLE = 200;
	public static final int SERVICE_DEFADDR = 300;
	public static final int SERVICE_ADDR_LIST = 400;
	public static final int SERVICE_ADDR = 5;
	public static final int SERVICE_OPTION_LEVEL = 600;
	public static final int PROGRESS_CANCLE =7; // 进度条取消
	public static final String BROAD_ADD_ACTION = "com.add.addr";//添加服务地址
	public static final String BROAD_CITY_ACTION = "com.change.city";//切换城市

	//常量
	public static final int PAGE_ONE = 0;
	public static final int PAGE_TWO = 1;
	public static final int PAGE_THREE = 2;
	public static final int PAGE_FOUR = 3;

	public static final int TAKE_PICTURE = 0;
	public static final int CHOOSE_PICTURE = 1;
	public static int screenWidth = 0;
	public static int screenHeigh = 0;
	public static String cstId = "6";//客户Id
	public static String usrId = null;
	public static int svrId = 0;//服务类别
	public static String lvlTitle = null;//服务级别
	public static String lvlId = null;//服务级别
	public static String name = null;//客户名称
	public static String nurseId = null;//护理师id
	public static String cityId = "440300";//城市Id 440300
	public static String cityName = null;//
	public static List<String> listLevel = new ArrayList<String>();
	public static List<String> listpople = new ArrayList<String>();
	//外网服务地址
	 public static String baseUrl = "http://120.25.101.140:8080/xyhl/";
	 public static String baseFileUrl = "http://120.25.101.140:8080/";
	// 服务器地址
	/*public static String baseUrl = "http://192.168.111.131:8080/xyhl/";
	public static String baseFileUrl = "http://192.168.111.131:8080/";*/

	/* public static String baseUrl = "http://183.232.35.71:8080/xyhl/";
	 public static String baseFileUrl = "http://183.232.35.71:8080/";*/
	//  图片路径http://192.168.1.131:8080/upload//icon//522701199211222108.jpg
	public static String nurseImage = baseFileUrl+"upload//icon//";
	//评论图片
	public static String evaluateImage =baseFileUrl+"upload/orderfiles//";
	//  生活照
	public static String lifePic = baseFileUrl+"upload//lifepic//";
	//工作视频
	public static String workvideo = baseFileUrl+"upload//workvideo//";
	//注册
	public static String registerUrl = baseUrl + "cst?a=zc";
	// 登录
	public static String loginUrl = baseUrl + "cst?a=login";
	//获取验证码  &mobile=15071456691
	public static String getVerifiUrl = baseUrl + "cst?a=getVerificationCode";
	//根据用户usr获取用户  &usr=111
	public static String findUsrUrl = baseUrl + "cst?a=findUsr";
	//输入新密码 &id=80001910&pwd=1111  id（用户id
	public static String getPwdUrl = baseUrl + "cst?a=getPwd";
	//修改密码 &id=80001969&pwd=123456&pwd2=123
	public static String cstPwdUpdUrl = baseUrl + "cst?a=cstPwdUpd";
	//修改昵称 Id (用户id) nickname (昵称)
	public static String cstNameUpdUrl = baseUrl + "cst?a=cstNameUpd";
	// 首页
	public static String homeUrl = baseUrl + "svr?a=svrlist";
	//更换城市
	public static String saveCityUrl = baseUrl + "addr?a=saveCity";
	//获取已开通城市
	public static String cityUrl = baseUrl + "addr?a=citychg";
	// 护理师筛选的页面
	public static String detailUrl = baseUrl + "svr?a=svrDetail&svrid";
	public static String funUrl = baseUrl + "svr?a=svrFun";
	//服务人员
	public static String catUrl = baseUrl + "svr?a=svrCat";
	//获取所有的价格和服务的级别
	public static String getPriceUrl = baseUrl + "svr?a=getPrice";
	//改变价格
	public static String getPriceAndLvUrl = baseUrl + "svr?a=getPriceAndLvl";
	// 护理师列表 
	public static String nurseListUrl = baseUrl + "nur?a=nurList";
	// 护理师详情
	public static String nurseDetailUrl = baseUrl + "nur?a=nurDetail";
	//提交订单ord?a=add
	public static String orderUrl = baseUrl + "ord?a=add";
	//订单列表
	public static String ordLisUrl = baseUrl + "ord?a=ordList";
	//护理师服务人员
	public static String nurseServiceUrl = baseUrl + "svr?a=svrCal";
    //找回密码  cst?a=getPwd&id(用户id)&pwd(新密码)
	public static String findPwdUrl = baseUrl + "cst?a=getPwd&";
    //顾客收藏列表  cstid （客户id）
	public static String cstFvrnurListUrl = baseUrl + "cst?a=cstFvrnurList";
	//删除收藏    id  （收藏id）cst?a=cstFvrnurDelcst?a=cstFvrnurDel
	public static String cstFvrnurDelUrl = baseUrl + "cst?a=cstFvrnurDel";
    //新增收藏  cstid  （客户id）  svrid   （服务id） nurseid   （护理师id）
	public static String cstFvrnurAddUrl = baseUrl + "cst?a=cstFvrnurAdd";
	//培训经历列表
	public static String nurseTrainListUrl = baseUrl + "nur?a=nurseTrainList";
	//护理师评论
	public static String nurseCmtUrl = baseUrl + "nur?a=nurseCmt";
	//用户支付
	public static String odrPayUrl = baseUrl + "ord?a=odrPay";
	//用户支付宝支付  ord?a=aliPay
	public static String aliPayUrl = baseUrl + "ord?a=aliPay";
	//获取用户默认地址
	public static String defAdressUrl = baseUrl + "addr?a=addressByIfdef";
	//获取收货地址
	public static String locationUrl = baseUrl + "addr?a=addressList";
	//删除服务地址 id(服务地址id)
	public static String delAddresUrl = baseUrl + "addr?a=delAddres";
	//修改服务地址 cstid（用户id）name（用户名称）mobile（用户手机号）addr（用户地址）ifdef（是否是默认，是传1，不是传0
	public static String saveAddresUrl = baseUrl + "addr?a=saveAddres";
	//添加服务地址 id(服务地址id)
	public static String addAddresUrl = baseUrl + "addr?a=addAddres";
	//修改默认地址  id(服务地址id)  cstid（用户id）
	public static String saveAddressByIdUrl = baseUrl + "addr?a=saveAddressById";
	//用户取消订单和开始服务 id (订单id) status (	状态
   public static String odrCstUpdUrl = baseUrl + "ord?a=odrCstUpd";
   //订单列表 nurseid(护理师id)/ cstid(顾客id) status （订单状态
   public static String mktOrderListUrl = baseUrl + "ord?a=mktOrderList";
  //提交评论  http://183.232.35.71:8080/xyhl/nur?a=nurseCmtAll&bycstid=6&nurseid=1294110&content="不错"&id=201607283674818&star=5;
  public static String nurseCmtAllUrl = baseUrl + "nur?a=nurseCmtAll";
  //反馈  id (用户意见id)  resp （反馈内容）
  public static String respUpdAllUrl = baseUrl + "cst?a=respUpd";
	//获取籍贯
	public static String findOrigoUrl = baseUrl + "code?a=findOrigo";
	// 服务地址 一级 城市
	public static String findCityUrl = baseUrl + "code?a=findCity";
	// 服务地址 二级 区县  iid为城市返回值中的iid  http://localhost:8080/xyhl/code?a=findCounty&iid=440100
	public static String findCountyUrl = baseUrl + "code?a=findCounty";
	//服务地址 三级 街道 iid为区县返回值中的iid http://192.168.111.131:8080/xyhl/code?a=findStreet&iid=541100
	public static String findStreetUrl = baseUrl + "code?a=findStreet";
}
