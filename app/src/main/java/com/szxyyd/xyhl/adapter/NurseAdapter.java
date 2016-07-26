package com.szxyyd.xyhl.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.activity.Constant;
import com.szxyyd.xyhl.activity.NurseDetailsActivity;
import com.szxyyd.xyhl.activity.NurselistActivity;
import com.szxyyd.xyhl.activity.OrderNurseActivity;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.modle.NurseList;
import com.szxyyd.xyhl.utils.CommUtils;
import com.szxyyd.xyhl.view.RoundImageView;

public class NurseAdapter extends BaseAdapter implements OnClickListener{
	private NurselistActivity mContext;
	private LayoutInflater inflater;
	private List<NurseList> mData;
	private ImageLoader mImageLoader;
	private RequestQueue mQueue ;
	public NurseAdapter(NurselistActivity context , List<NurseList> data){
		mContext = context;
		mData = data;
		inflater = LayoutInflater.from(mContext);
		mQueue = BaseApplication.getRequestQueue();
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
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
	public View getView(int positon, View contentView, ViewGroup arg2) {
		NurseList nurse = mData.get(positon);
		ViewHolder view;
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.adapter_nurse_collect, null, false);
			view = new ViewHolder();
			view.iv_teach = (RoundImageView) contentView.findViewById(R.id.iv_teach);
			view.tv_nurse_name = (TextView) contentView.findViewById(R.id.tv_nurse_name);
			view.tv_nurse_city = (TextView) contentView.findViewById(R.id.tv_nurse_city);
			view.tv_nurse_age = (TextView) contentView.findViewById(R.id.tv_nurse_age);
			view.btn_nurse_order = (TextView) contentView.findViewById(R.id.btn_ordernurse);
			view.tv_healthteacher = (TextView) contentView.findViewById(R.id.tv_healthteacher);
			view.tv_experience = (TextView) contentView.findViewById(R.id.tv_experience);
			view.tv_technology = (TextView) contentView.findViewById(R.id.tv_technology);
			view.ll_srvscore = (LinearLayout) contentView.findViewById(R.id.ll_srvscore);
			view.tv_distance = (TextView) contentView.findViewById(R.id.tv_distance);
			view.btn_nurse_order.setOnClickListener(this);
			contentView.setTag(view);
		}else{
			view = (ViewHolder) contentView.getTag();
		}
		Constant.nurseId = nurse.getNursvrid();
		Constant.name = nurse.getName();
		view.tv_nurse_name.setText(nurse.getName());
		view.tv_nurse_city.setText(nurse.getCity());
		view.tv_technology.setText(nurse.getSpcty());
		view.tv_distance.setText(nurse.getDistance()+"公里");
		//年龄
		int age = CommUtils.showAge(nurse.getBirth());
		view.tv_nurse_age.setText(age + "岁");
		view.btn_nurse_order.setTag(nurse);
		view.tv_healthteacher.setText(nurse.getSvrid());
		view.tv_experience.setText(nurse.getSrvyears()+"年工作经验");
		showStar(view.ll_srvscore,nurse.getSrvscore());
		// String imgUrl;//图片的Url
		String imgUrl = nurse.getIcon();
		//Log.e("getView","imgUrl=="+imgUrl);
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(view.iv_teach, R.drawable.teach, R.drawable.teach);
		mImageLoader.get(Constant.nurseImage+imgUrl, listener);
		return contentView;
	}
	private static class ViewHolder{
		TextView btn_nurse_order;//立即预约
		TextView tv_nurse_name;//姓名
		TextView tv_nurse_city; //城市
		TextView tv_nurse_age;//年龄
		TextView tv_healthteacher;
		TextView tv_experience;
		TextView tv_technology;
		TextView tv_distance;
		RoundImageView iv_teach;
		LinearLayout ll_srvscore;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_ordernurse:
				NurseList nurse = (NurseList) view.getTag();
				Intent intent = new Intent(mContext,OrderNurseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("nurse", nurse);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
				break;
		}
	}
private void showStar(LinearLayout linearLayout,int num){
	linearLayout.removeAllViews();
	int leng = num > 5 ? 5 : num;
	for(int i = 0; i < leng;i++){
			ImageView imageView = new ImageView(mContext);
		imageView.setBackground(mContext.getResources().getDrawable(R.drawable.star));
		linearLayout.addView(imageView);
	}
}
}
