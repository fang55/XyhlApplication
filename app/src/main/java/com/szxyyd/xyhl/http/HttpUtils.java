package com.szxyyd.xyhl.http;

import com.szxyyd.xyhl.utils.ThreadPoolUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtils {
	public callBackListener listener;
	public HttpUtils(callBackListener listener){
		this.listener = listener;
	}
	public static void getHttpData(final String path,final callBackListener listener){
		/*BaseApplication.getInstance().executorService.execute(new Runnable() {
			@Override
			public void run() {*/
		    ThreadPoolUtils.execute(new Runnable() {
				@Override
				public void run() {
					HttpURLConnection urlConnection = null;  
					try {
						URL url = new URL(path);
						urlConnection = (HttpURLConnection)url.openConnection();  
						urlConnection.setRequestMethod("GET");// 设置请求的方式  
			            urlConnection.setReadTimeout(8000);// 设置超时的时间  
			            urlConnection.setConnectTimeout(8000);// 设置链接超时的时间  
			            if (urlConnection.getResponseCode() == 200) {
			                InputStream is = urlConnection.getInputStream();  
			                ByteArrayOutputStream os = new ByteArrayOutputStream();  
			                int len = 0;  
			                byte buffer[] = new byte[1024];  
			                while ((len = is.read(buffer)) != -1) {  
			                    os.write(buffer, 0, len);  
			                }  
			                is.close();  
			                os.close(); 
			                String result = new String(os.toByteArray());
			                if(result != null){
			                	 listener.onSucceed(result);
			                }else{
			                	listener.onFailure("网络异常");
			                }
			            }
					} catch (IOException e) {  
					    e.printStackTrace();  
					}  
					finally {  
						if (urlConnection != null) {
						//	 Log.e("urlConnection", " urlConnection != null==");
							 urlConnection.disconnect();  
						}
					}  
				}
			});
				
		/*	}
		});*/
	}
	
	public  interface callBackListener{
		void onSucceed(String result);
		void onFailure(String data);
	}
	
}
