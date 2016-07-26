package com.szxyyd.xyhl.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.xyhl.R;

public class MassageAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater infater;
   public MassageAdapter(Context context){
	   mContext = context;
	   infater = LayoutInflater.from(mContext);
   }
	@Override
	public int getCount() {
		return 4;
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
	//	ViewHolder view ;
		//if(contentView == null){
			contentView = infater.inflate(R.layout.adapter_massage, null, false);
		//	view = new ViewHolder();
			/*view.iv_ico = (ImageView) contentView.findViewById(R.id.iv_ico);
			view.tv_name = (TextView) contentView.findViewById(R.id.tv_name);*/
		//	contentView.setTag(view);
		
		/*view.tv_name.setText(mData.get(position).toString());
		view.iv_ico.setImageResource(Constant.icoData[position]);*/
		return contentView;
	}
   private static class ViewHolder{
	   ImageView iv_ico;
	   TextView tv_name;
   }
}
