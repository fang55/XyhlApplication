package com.szxyyd.xyhl.fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.activity.MyActivity;
import com.szxyyd.xyhl.adapter.CollectAdapter;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.NurseList;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 我的收藏
 * @author jq
 *
 */
public class CollectFragment extends Fragment{
	private View rootView;
	private GridView gridview;
	private CollectAdapter adapter;
	private MyActivity mActivity;
	private List<NurseList> listNurse;
	private NurseList nurse;
	private ProgressDialog proDialog;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constant.SUCCEED:
					listNurse = (List<NurseList>) msg.obj;
					if(listNurse.size() != 0 ) {
						adapter = new CollectAdapter(getActivity(), listNurse);
						gridview.setAdapter(adapter);
						adapter.setCancleListener(new CollectAdapter.clickCancleListener() {
							@Override
							public void onSelect(int position, NurseList nurse) {
								Toast.makeText(getActivity(), "position==" + position, Toast.LENGTH_SHORT).show();
								String id = nurse.getId();
								showCancleDialog(id,position);
							}
						});
					}else{
						Toast.makeText(mActivity,"暂无数据",Toast.LENGTH_SHORT).show();
						if(adapter != null){
							adapter.notifyDataSetChanged();
						}
					}
					break;

				default:
					break;
			}

		};
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MyActivity) activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_collect, container, false);
		initView();
		lodeCollectData();
		return rootView;
	}
   private void initView(){
	   gridview = (GridView) rootView.findViewById(R.id.gridview);
	   gridview.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
		}
	});
   }
	private void showCancleDialog(final String id,final int position){
		final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_cancle);
		TextView tv_dialog_cancle = (TextView) window.findViewById(R.id.tv_dialog_cancle);
		tv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		TextView tv_dialog_sure = (TextView) window.findViewById(R.id.tv_dialog_sure);
		tv_dialog_sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submitCancleData(id);
				listNurse.remove(position);
				adapter.notifyDataSetChanged();
				alertDialog.cancel();
			}
		});
	}
	/**
	 * 获取收藏列表
	 * cstId 用户id
	 *
	 */
	private void lodeCollectData(){
		proDialog = ProgressDialog.show(mActivity, "", "加载中");
		String url = Constant.cstFvrnurListUrl+"&cstid="+Constant.cstId;
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(getActivity(), url, "getcollect",
				new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("CollectFragment", "lodeNurseTrainList--result=="+result);
						if(proDialog != null){
							proDialog.cancel();
						}
						parserData(result,"getcollect");
					}
					@Override
					public void onError(VolleyError error) {

					}
				});
	}

	/**
	 * 提交取消收藏
	 * collectId 收藏id
	 */
	private void submitCancleData(String collectId){
		String url = Constant.cstFvrnurDelUrl+"&id=="+collectId;
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(getActivity(), url, "subcollect",
				new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("CollectFragment", "submitData--result=="+result);
						parserData(result,"subcollect");
					}
					@Override
					public void onError(VolleyError error) {

					}
				});

	}

	/**
	* 解析返回来的数据
	* @param result
	*/
	private void parserData(String result,String state) {
		try {
			JSONObject json = new JSONObject(result);
			String jsonData = null;
			if(state.equals("getcollect")){
				jsonData = json.getString("nurse");
				Type listType = new TypeToken<LinkedList<NurseList>>(){}.getType();
				Gson gson = new Gson();
				List<NurseList> list = gson.fromJson(jsonData, listType);
				Message message = new Message();
				message.what = Constant.SUCCEED;
				message.obj = list;
				handler.sendMessage(message);
			}else{

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
