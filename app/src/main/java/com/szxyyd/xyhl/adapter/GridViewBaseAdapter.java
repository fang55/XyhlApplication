package com.szxyyd.xyhl.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.modle.NurseType;

public class GridViewBaseAdapter extends BaseAdapter{
	private Context mContext;
	private List<NurseType> mData;
	private LayoutInflater inflater;
	public GridViewBaseAdapter(Context content,List<NurseType> data){
		mContext = content;
		mData = data;
		inflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return mData.size() == 0 ? 0 : mData.size();
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
		NurseType nurse = mData.get(position);
		String name = nurse.getName().toString();
	//	Log.e("Adapter", "name=="+name);
		ViewHolder view ;
		if(contentView == null){
			contentView = inflater.inflate(R.layout.gridview_item, null, false);
			view = new ViewHolder();
			view.iv_background = (ImageView) contentView.findViewById(R.id.iv_background);
			view.tv_action = (TextView) contentView.findViewById(R.id.tv_action);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		Constant.svrId = mData.get(position).getId();
		view.tv_action.setText(mData.get(position).getName().toString());
		return contentView;
	}
  static class ViewHolder{
	   ImageView iv_background;
	   TextView tv_action;
   }
}
