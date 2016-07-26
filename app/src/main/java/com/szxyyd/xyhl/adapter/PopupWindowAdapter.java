package com.szxyyd.xyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.modle.City;

import java.util.List;

public class PopupWindowAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private List<String> mData;
	private boolean isClick = false;
	public PopupWindowAdapter(Context context, List<String> data){
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder view;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_popupwidom, null, false);
			view = new ViewHolder();
			view.tv_title = (TextView) contentView.findViewById(R.id.tv_pw_title);
			view.iv_right = (ImageView) contentView.findViewById(R.id.iv_right);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		view.tv_title.setText(mData.get(position).toString());
		return contentView;
	}

	static class ViewHolder{
	   TextView tv_title;
	   ImageView iv_right;
   }

}
