package com.szxyyd.xyhl.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.modle.Reladdr;


public class CommUtils {
	/**
	 * 根据出生日期算出年龄
	 * @param brither
	 * @return
	 */
	public static int showAge(String brither){
		  //1463587200
		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  long change = Long.valueOf(brither);
		  int i = Integer.parseInt(brither);
		  String result = df.format(new Date(i * 1000L));
		//  String dateString = df.format(brither);
		//  Log.e("CommUtils", "result=="+result);
			try {
				    Date d1 = (Date) df.parse(result);
				    Calendar cal = Calendar.getInstance();
				    int yearNow = cal.get(Calendar.YEAR);
				//	int monthNow = cal.get(Calendar.MONTH) + 1;
				//	int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
					cal.setTime(d1);
					int yearBirth = cal.get(Calendar.YEAR);
				//	int monthBirth = cal.get(Calendar.MONTH) + 1;
				//	int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
					int age = yearNow - yearBirth;
					// Log.e("LoginActivity", "age=="+age);
					 return age;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
	}
	public static String showData(String brither){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		int i = Integer.parseInt(brither);
		String result = df.format(new Date(i * 1000L));
		return result;
	}
	/**
	 * 获取当前日期
	 */
	public static String showToDay(){
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String result = df.format(curDate);
		return result;
	}
	/**
	 * 缓存默认地址
	 */
	public static void sharedDefAddr( List<Reladdr> list){
		SharedPreferences preferences = BaseApplication.getInstance().getSharedPreferences("defaddr", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		String name = list.get(0).getName().toString();
		String phone = list.get(0).getMobile().toString();
		String addr = list.get(0).getAddr().toString();
		editor.putString("name", name);
		editor.putString("mobile", phone);
		editor.putString("addr", addr);
		editor.putString("ifdef", list.get(0).getIfdef());
		editor.commit();
	}
	public static String subStr(String result){

		String newStr = result.substring(0,result.indexOf("."));
		return  newStr;
	}
}
