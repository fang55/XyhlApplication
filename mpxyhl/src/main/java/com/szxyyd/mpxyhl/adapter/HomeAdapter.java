package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.modle.NurseType;

import java.util.List;

/**
 * Created by jq on 2016/7/5.
 */
public class HomeAdapter extends BaseAdapter{
    private Context mContext;
    private List<NurseType.SvrBean> nurseList;
    private LayoutInflater inflater;
    private int[] incoData = new int[]{R.mipmap.tab_im1,R.mipmap.tab_im2,R.mipmap.tab_im3,R.mipmap.tab_im4,R.mipmap.tab_im5,R.mipmap.tab_im6};
    public HomeAdapter(Context context,List<NurseType.SvrBean> nurseList){
        mContext = context;
        this.nurseList = nurseList;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return nurseList.size();
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        NurseType.SvrBean nurseType = nurseList.get(position);
        if(contentView == null){
            contentView = inflater.inflate(R.layout.adapter_home,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            viewHolder.iv_mark = (ImageView) contentView.findViewById(R.id.iv_mark);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        Log.e("HomeAdapter","nurseType.getName()=="+nurseType.getName());
        viewHolder.tv_title.setText(nurseType.getName());
        viewHolder.iv_mark.setBackgroundResource(incoData[position]);
        return contentView;
    }
    static class ViewHolder{
       TextView tv_title;
        ImageView iv_mark;

    }
}
