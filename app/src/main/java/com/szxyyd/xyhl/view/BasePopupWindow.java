package com.szxyyd.xyhl.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.activity.BaseApplication;
import com.szxyyd.xyhl.adapter.PopupWindowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fq on 2016/5/31.
 */
public class BasePopupWindow extends PopupWindow {
    private Context mContext;
    private LayoutInflater inflater;
    private OnPopuItemClickListener mItemClickListener;
    private PopupWindowAdapter adapter;
    private  ListView lv_distance;
    public BasePopupWindow(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }
    public void showRootView(View rootView,final List<String> data,final String[] dataTag){ //rootView 是点击弹出框的view
        View view = inflater.inflate(R.layout.popupwindow_item, null, false);
         lv_distance = (ListView) view.findViewById(R.id.lv_distance);
        lv_distance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mItemClickListener.onItemClick(position,data.get(position),dataTag[position]);
                dismiss();
              }
            });
        adapter = new PopupWindowAdapter(mContext,data);
        lv_distance.setAdapter(adapter);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.def_pwbg));
        this.setWidth(200);
        this.setHeight(400);
        setContentView(view);
        showAsDropDown(rootView);
    }
    public void setOnPopuItemClickListener(OnPopuItemClickListener listener){
        mItemClickListener = listener;
    }
    /**
     * 点击监听
     */
    public interface OnPopuItemClickListener{
       public abstract void onItemClick(int pos,String result,String tag);
    }
}
