package com.szxyyd.mpxyhl.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.modle.Reladdr;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
	public static String subStr(String result){

		String newStr = result.substring(0,result.indexOf("."));
		return  newStr;
	}
}
