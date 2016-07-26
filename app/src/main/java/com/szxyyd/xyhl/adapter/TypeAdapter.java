package com.szxyyd.xyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.szxyyd.xyhl.R;

public class TypeAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	public TypeAdapter(Context context){
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return 5;
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
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		contentView = inflater.inflate(R.layout.include_border, null, false);
		return contentView;
	}
  static class ViewHolder{
	  
  }
}
