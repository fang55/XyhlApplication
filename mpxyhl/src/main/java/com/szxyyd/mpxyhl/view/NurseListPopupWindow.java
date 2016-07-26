package com.szxyyd.mpxyhl.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.PopupWindowAdapter;

/**
 * Created by jq on 2016/7/7.
 */
public class NurseListPopupWindow extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater inflater;
    private OnPopuItemClickListener mListener;
    private ListView lv;
    private PopupWindowAdapter adapter;
    private String[] mData;
    public NurseListPopupWindow(Context context,String[] data) {
        super(context);
        mContext = context;
        mData = data;
        inflater = LayoutInflater.from(mContext);
    }
    public void initView(View rootView){
        View view = inflater.inflate(R.layout.pw_list, null, false);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.home_bg));
        lv = (ListView) view.findViewById(R.id.listview);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               if(mListener != null){
                   mListener.onSure(position);
               }
                dismiss();
            }
        });
        adapter = new PopupWindowAdapter(mContext,mData);
        lv.setAdapter(adapter);
        this.setWidth((int) mContext.getResources().getDimension(R.dimen.w720));
        this.setHeight((int) mContext.getResources().getDimension(R.dimen.h1085));
        setContentView(view);
        showAsDropDown(rootView);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){

       }
    }
    public void setonPopuItemListener(OnPopuItemClickListener listener){
        mListener = listener;
    }
    public interface OnPopuItemClickListener{
        void onSure(int position);
    }
}
