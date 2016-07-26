package com.szxyyd.mpxyhls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.modle.Info;

import java.util.List;

/**
 * Created by jq on 2016/7/13.
 */
public class ListAdapter extends BaseAdapter{
    private Context mContext;
    private String[] mData;
    private List<String> listData;
    private LayoutInflater inflater;
    public ListAdapter(Context context,String[] data,List<String> list){
        mContext = context;
        mData = data;
        listData = list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mData.length;
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
            contentView = inflater.inflate(R.layout.list_info,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_type = (TextView) contentView.findViewById(R.id.tv_type);
            viewHolder.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_type.setText(mData[position].toString());
        viewHolder.tv_content.setText(listData.get(position).toString());
        return contentView;
    }
     class ViewHolder{
         TextView tv_type;
         TextView tv_content;
     }
}
