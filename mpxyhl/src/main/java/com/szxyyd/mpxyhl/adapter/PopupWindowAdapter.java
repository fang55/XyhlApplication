package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;

import java.util.List;

public class PopupWindowAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private String[] mData;
	public PopupWindowAdapter(Context context, String[] data){
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData.length;
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
			contentView = inflater.inflate(R.layout.pw_list_item, null, false);
			view = new ViewHolder();
			view.tv_title = (TextView) contentView.findViewById(R.id.tv_pwname);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		view.tv_title.setText(mData[position].toString());
		return contentView;
	}

	static class ViewHolder{
	   TextView tv_title;
	   ImageView iv_right;
   }

}
