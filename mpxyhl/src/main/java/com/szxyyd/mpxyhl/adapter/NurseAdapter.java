package com.szxyyd.mpxyhl.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.activity.NurselistActivity;
import com.szxyyd.mpxyhl.activity.OrderNurseActivity;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.utils.CommUtils;
import com.szxyyd.mpxyhl.view.RoundImageView;

import java.util.List;

/**
 * Created by jq on 2016/7/5.
 */
public class NurseAdapter extends BaseAdapter implements View.OnClickListener {
    private NurselistActivity mContext;
    private LayoutInflater inflater;
    private List<NurseList> mData;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;

    public NurseAdapter(NurselistActivity context, List<NurseList> data) {
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int positon, View contentView, ViewGroup viewGroup) {
        NurseList nurse = mData.get(positon);
        ViewHolder view;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.adapter_nurse_list, null, false);
            view = new ViewHolder();
            view.iv_teach = (RoundImageView) contentView.findViewById(R.id.iv_teach);
            view.tv_nurse_name = (TextView) contentView.findViewById(R.id.tv_nurse_name);
            view.tv_nurse_distance = (TextView) contentView.findViewById(R.id.tv_nurse_distance);
            view.tv_nurse_age = (TextView) contentView.findViewById(R.id.tv_nurse_age);
            view.btn_nurse_order = (TextView) contentView.findViewById(R.id.btn_ordernurse);
            view.tv_healthteacher = (TextView) contentView.findViewById(R.id.tv_healthteacher);
            view.tv_experience = (TextView) contentView.findViewById(R.id.tv_experience);
            view.tv_technology = (TextView) contentView.findViewById(R.id.tv_technology);
            view.ll_srvscore = (LinearLayout) contentView.findViewById(R.id.ll_srvscore);
            view.btn_nurse_order.setOnClickListener(this);
            contentView.setTag(view);
        } else {
            view = (ViewHolder) contentView.getTag();
        }
        Constant.nurseId = nurse.getNursvrid();
        Constant.name = nurse.getName();
        view.tv_nurse_name.setText(nurse.getName());
        view.tv_technology.setText(nurse.getSpcty());
        view.tv_nurse_distance.setText(nurse.getDistance() + "公里");
        //年龄
        int age = CommUtils.showAge(nurse.getBirth());
        view.tv_nurse_age.setText(age + "岁");
        view.btn_nurse_order.setTag(nurse);
        view.tv_healthteacher.setText(nurse.getSvrid());
        view.tv_experience.setText(nurse.getSrvyears() + "年工作经验");
        showStar(view.ll_srvscore, nurse.getSrvscore());
        // String imgUrl;//图片的Url
        String imgUrl = nurse.getIcon();
        //Log.e("getView","imgUrl=="+imgUrl);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view.iv_teach, R.mipmap.teach, R.mipmap.teach);
        mImageLoader.get(Constant.nurseImage + imgUrl, listener);
        return contentView;
    }

    @Override
    public void onClick(View view) {
         if(view.getId() == R.id.btn_ordernurse){
             NurseList nurse = (NurseList) view.getTag();
             Intent intent = new Intent(mContext,OrderNurseActivity.class);
             Bundle bundle = new Bundle();
             bundle.putSerializable("nurse", nurse);
             intent.putExtras(bundle);
             mContext.startActivity(intent);
         }
    }

    private static class ViewHolder {
        TextView btn_nurse_order;//立即预约
        TextView tv_nurse_name;//姓名
        TextView tv_nurse_distance; //城市
        TextView tv_nurse_age;//年龄
        TextView tv_healthteacher;
        TextView tv_experience;
        TextView tv_technology;
        RoundImageView iv_teach;
        LinearLayout ll_srvscore;
    }

    private void showStar(LinearLayout linearLayout, int num) {
        linearLayout.removeAllViews();
        int leng = num > 5 ? 5 : num;
        for (int i = 0; i < leng; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackground(mContext.getResources().getDrawable(R.mipmap.star));
            linearLayout.addView(imageView);
        }
    }
}
