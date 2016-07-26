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
public class ContentAdapter extends BaseAdapter{
    private Context mContext;
    private List<Info> mData;
    private LayoutInflater inflater;
    public ContentAdapter(Context context,List<Info> data){
        mContext = context;
        mData = data;
        inflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        Info info = mData.get(position);
        ViewHolder viewHolder;
        if(contentView == null){
            contentView = inflater.inflate(R.layout.list_content_item,null,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_content.setText(info.getName());
        return contentView;
    }
     class ViewHolder{
         TextView tv_content;
     }
}
