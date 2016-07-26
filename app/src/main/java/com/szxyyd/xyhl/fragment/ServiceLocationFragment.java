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
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.activity.MyActivity;
import com.szxyyd.xyhl.adapter.ServiceLocationAdapter;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;
import com.szxyyd.xyhl.modle.Reladdr;
import com.szxyyd.xyhl.view.EditDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ServiceLocationFragment extends Fragment {
	private View rootView;
	private GridView listview;
	private Button btn_add;
	private ServiceLocationAdapter adapter;
	private MyActivity mActivity;
	private List<Reladdr> list;
	private ProgressDialog proDialog;
	private AlertDialog alertDialog;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case Constant.SUCCEED:
					list = (List<Reladdr>) msg.obj;
					if(list.size() != 0 ) {
						adapter = new ServiceLocationAdapter(getActivity(), list, new ServiceLocationAdapter.selectOnclickListener() {
							@Override
							public void selectDelect(int position,String type) {
								Reladdr reladdr = list.get(position);
								if(type.equals("edit")){
									EditDialog dialog = new EditDialog(mActivity,reladdr,"edit",position);
									dialog.init();
									dialog.setOnFinshClickListener(new EditDialog.onFinshClickListener() {
										@Override
										public void onfinsh() {
											loadAddrListData("list","0");
										}
									});
								}else if(type.equals("checked")){
									loadAddrListData("checked",String.valueOf(reladdr.getId()));
								}
								else {
									showCancleDialog(position);
								}
							}
						});
						listview.setAdapter(adapter);
					}else{
						Toast.makeText(mActivity,"暂无数据",Toast.LENGTH_SHORT).show();
						if(adapter != null){
							adapter.notifyDataSetChanged();
						}
					}
					break;
				case Constant.SUBITM:
					loadAddrListData("list","0");
					break;
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MyActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_service_location, container, false);
		initView();
		loadAddrListData("list","0");
		return rootView;
	}
  private void initView(){
	  listview = (GridView) rootView.findViewById(R.id.listview);
	  btn_add = (Button) rootView.findViewById(R.id.btn_add);
	  btn_add.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View view) {
			  EditDialog dialog = new EditDialog(getActivity(),null,"add",0);
			  dialog.init();
			  dialog.setOnFinshClickListener(new EditDialog.onFinshClickListener() {
				  @Override
				  public void onfinsh() {
					  loadAddrListData("list","0");
				  }
			  });
		  }
	  });
  }
	private void showCancleDialog(final int position){
		alertDialog = new AlertDialog.Builder(mActivity).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_cancle);
		TextView tv_dialog_title = (TextView) window.findViewById(R.id.tv_dialog_title);
		tv_dialog_title.setText("是否删除地址？");
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
				loadAddrListData("delect", String.valueOf(list.get(position).getId()));
				list.remove(position);
				adapter.notifyDataSetChanged();
				alertDialog.cancel();
				if(proDialog != null){
					proDialog.cancel();
				}
			}
		});
	}
	/**
	 * 获取服务地址列表
	 */
	private void loadAddrListData(String type,String id){
		String url = null;
		if(type.equals("delect")){
			proDialog = ProgressDialog.show(mActivity, "", "加载中");
			url = Constant.delAddresUrl + "&id="+id;
		}else if(type.equals("checked")){  // id(服务地址id) cstid（用户id）
			url = Constant.saveAddressByIdUrl + "&id="+id + "&cstid="+Constant.cstId;
		}
		else if(type.equals("list")){
			proDialog = ProgressDialog.show(mActivity, "", "加载中");
			url = Constant.locationUrl+"&cstid="+Constant.cstId;
		}

		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(getActivity(), url, "addrlist",
				new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("ServiceLocationFragment", "loadAddrListData --result=="+result);
						if(proDialog != null) {
							proDialog.cancel();
						}
						parserData(result);
					}
					@Override
					public void onError(VolleyError error) {

					}
				});
	}
	private void parserData(String result){
		try {
			if(result.equals("yes")) {
				Message message = new Message();
				message.what = Constant.SUBITM;
				message.obj = list;
				handler.sendMessage(message);
			}else{
				JSONObject json = new JSONObject(result);
				String  jsonData = json.getString("reladdr");
				Type listType = new TypeToken<LinkedList<Reladdr>>(){}.getType();
				Gson gson = new Gson();
				List<Reladdr> list = gson.fromJson(jsonData, listType);
				Message message = new Message();
				message.what = Constant.SUCCEED;
				message.obj = list;
				handler.sendMessage(message);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
