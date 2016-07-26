package com.szxyyd.mpxyhls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.modle.Info;

import java.util.List;
import java.util.Map;

/**
 * Created by jq on 2016/6/8.
 */
public class InfoAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> parent = null;
    private Map<String, List<Info>> map = null;
    public InfoAdapter(Context context, List<String> parent, Map<String, List<Info>> map){
        mContext = context;
        this.parent = parent;
        this.map = map;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String key = parent.get(groupPosition);
        int size=map.get(key).size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = parent.get(groupPosition);
        return (map.get(key).get(childPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.el_parent,null,false);
        }
        TextView tv_certificate = (TextView) convertView.findViewById(R.id.tv_certificate);
       tv_certificate.setText(parent.get(groupPosition).toString());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView , ViewGroup viewGroup) {
        String key = parent.get(groupPosition);
        String info = map.get(key).get(childPosition).getName();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.el_children,null,false);
        }
        TextView tv_children_certificate = (TextView) convertView.findViewById(R.id.tv_children_certificate);
        tv_children_certificate.setText(info);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
