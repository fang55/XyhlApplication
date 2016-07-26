package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.modle.City;

import java.util.List;

/**
 * Created by jq on 2016/7/19.
 */
public class RegionAdapter extends BaseAdapter{
    private Context mContent;
    private LayoutInflater inflater;
    private List<City> mList;
    public RegionAdapter(Context content,List<City> list){
        mContent = content;
        mList = list;
        inflater = LayoutInflater.from(mContent);
    }
    @Override
    public int getCount() {
        return mList.size();
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
        if(contentView == null){
            contentView = inflater.inflate(R.layout.list_item_region,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_regionName = (TextView) contentView.findViewById(R.id.tv_regionName);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_regionName.setText(mList.get(position).getName());
        return contentView;
    }
    private class ViewHolder{
          TextView tv_regionName;
    }
}
