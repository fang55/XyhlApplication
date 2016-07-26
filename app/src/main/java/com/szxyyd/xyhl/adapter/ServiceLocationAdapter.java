package com.szxyyd.xyhl.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.modle.Reladdr;
import com.szxyyd.xyhl.view.EditDialog;

public class ServiceLocationAdapter extends BaseAdapter implements OnClickListener{
	private Context mContext;
	private List<Reladdr> mData;
	private LayoutInflater inflater;
	private selectOnclickListener mListener;
	private Reladdr reladdr;
	private boolean isDwon = false;
	public ServiceLocationAdapter(Context context,List<Reladdr> data,selectOnclickListener listener){
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(mContext);
		mListener = listener;
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
		 reladdr = mData.get(position);
		ViewHolder view;
		if(contentView == null){
			contentView = inflater.inflate(R.layout.listview_item_location, null, false);
			view = new ViewHolder();
			view.tv_addr_name = (TextView) contentView.findViewById(R.id.tv_addr_name);
			view.tv_addr_phone = (TextView) contentView.findViewById(R.id.tv_addr_phone);
			view.tv_addr = (TextView) contentView.findViewById(R.id.tv_addr);
			view.btn_radio = (ImageView) contentView.findViewById(R.id.btn_radio);
			view.ll_edit = (LinearLayout) contentView.findViewById(R.id.ll_edit);
			view.ll_delect = (LinearLayout) contentView.findViewById(R.id.ll_delect);
			view.ll_edit.setOnClickListener(this);
			view.ll_delect.setOnClickListener(this);
			view.btn_radio.setOnClickListener(this);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		view.tv_addr_name.setText(reladdr.getName());
		view.tv_addr_phone.setText(reladdr.getMobile());
		view.tv_addr.setText(reladdr.getAddr());
		if(reladdr.getIfdef().equals("是")){
			view.btn_radio.setBackground(mContext.getDrawable(R.drawable.iv_dwon));
		}else{
			view.btn_radio.setBackground(mContext.getDrawable(R.drawable.iv_select));
		}
		view.ll_delect.setTag(position);
		view.ll_edit.setTag(position);
		view.btn_radio.setTag(position);
		return contentView;
	}

	static class ViewHolder{
	   LinearLayout ll_edit;
	   LinearLayout ll_delect;
	  TextView tv_addr_name;
	  TextView tv_addr_phone;
	  TextView tv_addr;
		ImageView btn_radio;
  }

@Override
public void onClick(View view) {
	switch (view.getId()){
		case R.id.ll_edit:
			int id = Integer.parseInt(view.getTag().toString());
			mListener.selectDelect(id,"edit");
			break;
		case R.id.ll_delect:
          int dex = Integer.parseInt(view.getTag().toString());
			mListener.selectDelect(dex,"delect");
			break;
		case R.id.btn_radio:
			int radio = (int) view.getTag();
            if(isDwon){
				view.setBackground(mContext.getDrawable(R.drawable.iv_select));
				isDwon = false;
			}else{
				isDwon = true;
				view.setBackground(mContext.getDrawable(R.drawable.iv_dwon));
				mListener.selectDelect(radio,"checked");
			}
			break;
	}
}
	public interface selectOnclickListener{
		   void selectDelect(int position,String type);
	}
}
