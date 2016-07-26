package com.szxyyd.xyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.modle.City;

import java.util.List;

public class CityAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	private List<City> mData;
	public CityAdapter(Context context,List<City> data){
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
		City city = mData.get(position);
		ViewHolder view;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.listview_item_city, null, false);
			view = new ViewHolder();
			view.tv_city = (TextView) contentView.findViewById(R.id.tv_city);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		view.tv_city.setText(city.getName().toString());
		return contentView;
	}
   static class ViewHolder{
	   TextView tv_city;
   }
}
