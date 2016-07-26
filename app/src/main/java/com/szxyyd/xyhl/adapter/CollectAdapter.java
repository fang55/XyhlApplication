package com.szxyyd.xyhl.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.activity.OrderNurseActivity;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.modle.NurseList;
import com.szxyyd.xyhl.utils.CommUtils;
import com.szxyyd.xyhl.view.RoundImageView;

import java.util.List;

public class CollectAdapter extends BaseAdapter implements View.OnClickListener {
	private Context mContext;
	private LayoutInflater inflater;
	private clickCancleListener mlistener;
	private int selectPosition;
	private List<NurseList> listNurse;
	private NurseList nurse;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue ;
	public CollectAdapter(Context context,List<NurseList> listNurse){
		mContext = context;
		this.listNurse = listNurse;
		inflater = LayoutInflater.from(mContext);
		mQueue = BaseApplication.getRequestQueue();
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
	}
	@Override
	public int getCount() {
		return listNurse.size();
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
		 nurse = listNurse.get(position);
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_collect, null, false);
			view = new ViewHolder();
			view.iv_teach = (RoundImageView) contentView.findViewById(R.id.iv_teach);
			view.tv_nurse_name = (TextView) contentView.findViewById(R.id.tv_nurse_name);
			view.tv_nurse_city = (TextView) contentView.findViewById(R.id.tv_nurse_city);
			view.tv_nurse_age = (TextView) contentView.findViewById(R.id.tv_nurse_age);
            view.tv_distance = (TextView) contentView.findViewById(R.id.tv_distance);
			view.btn_nurse_order = (Button) contentView.findViewById(R.id.btn_go);
			view.btn_canclecollect = (Button) contentView.findViewById(R.id.btn_canclecollect);
			view.tv_healthteacher = (TextView) contentView.findViewById(R.id.tv_healthteacher);
			view.tv_experience = (TextView) contentView.findViewById(R.id.tv_experience);
			view.btn_nurse_order.setOnClickListener(this);
			view.btn_canclecollect.setOnClickListener(this);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		view.tv_nurse_name.setText(nurse.getName());
		view.tv_nurse_age.setText(CommUtils.showAge(nurse.getBirth())+"岁");
		view.tv_nurse_city.setText(nurse.getCity());
		view.tv_experience.setText(nurse.getSrvyears()+"年工作经验");
		view.tv_distance.setText(nurse.getDistance()+"公里");
		selectPosition = position;
		String imgUrl = nurse.getIcon();
		//Log.e("getView","imgUrl=="+imgUrl);
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(view.iv_teach, R.drawable.teach, R.drawable.teach);
		mImageLoader.get(Constant.nurseImage+imgUrl, listener);
		return contentView;
	}

	@Override
	public void onClick(View view) {
      switch (view.getId()){
		  case R.id.btn_go:
			  Intent intent = new Intent(mContext,OrderNurseActivity.class);
			  Bundle bundle = new Bundle();
			  bundle.putSerializable("nurse", nurse);
			  intent.putExtras(bundle);
			  mContext.startActivity(intent);
			  break;
		  case R.id.btn_canclecollect:
			  mlistener.onSelect(selectPosition,nurse);
			  break;
	  }
	}

	static class ViewHolder{
		RoundImageView iv_teach;
		Button btn_canclecollect;//取消收藏
	   Button btn_nurse_order;//立即预约
	   TextView tv_nurse_name;//姓名
	   TextView tv_nurse_city; //城市
	   TextView tv_nurse_age;//年龄
	   TextView tv_healthteacher;
	   TextView tv_experience;
		TextView tv_distance;
  }
	public void setCancleListener(clickCancleListener mlistener){
		  this.mlistener = mlistener;
	}
	public interface clickCancleListener{
		void onSelect(int position,NurseList nurse);
	}

}
