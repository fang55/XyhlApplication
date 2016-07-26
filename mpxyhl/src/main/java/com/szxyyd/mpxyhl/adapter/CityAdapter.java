package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.modle.City;

import java.util.List;

/**
 * Created by jq on 2016/7/6.
 */
public class CityAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<City> list;
    public CityAdapter(Context context,List<City> list){
        mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list.size();
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
        City city = list.get(position);
        if(contentView == null){
            contentView = inflater.inflate(R.layout.adapter_city,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_city = (TextView) contentView.findViewById(R.id.tv_city);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_city.setText(city.getName());
        return contentView;
    }
   static class ViewHolder{
       TextView tv_city;
   }
}
