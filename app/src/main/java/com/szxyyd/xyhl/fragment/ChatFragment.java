package com.szxyyd.xyhl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.adapter.ChatMsgViewAdapter;
import com.szxyyd.xyhl.modle.ChatMsgEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment{
	private View rootView;
	private ListView listview;
	private final static int COUNT = 6; 
	private String[] dataArray = new String[] { "2012-10-31 18:00",  
            "2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",  
            "2012-10-31 18:30", "2012-10-31 18:35"}; 
	private String[] msgArray = new String[] { "有人就有恩怨","有恩怨就有江湖","人就是江湖","你怎么退出？ ",
			"生命中充满了巧合","两条平行线也会有相交的一天。"};  
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_chat, container, false);
		initView();
		return rootView;
	}
	private void initView(){
		listview = (ListView) rootView.findViewById(R.id.lv_chat);
		initData();
	}
	public void initData() {  
        for (int i = 0; i < 2; i++) {  
            ChatMsgEntity entity = new ChatMsgEntity();  
            entity.setDate(dataArray[i]);  
            entity.setName("您好，本店新款商品推荐，欢迎您选购!"); 
//            if (i % 2 == 0) {  
//                entity.setName("您好，本店新款商品推荐，欢迎您选购!");  
//                entity.setType(0);  
//            } else {  
//                entity.setName("谢谢");  
//                entity.setType(1);  
//            }  
            entity.setText(msgArray[i]);  
            mDataArrays.add(entity);  
        }  
        mAdapter = new ChatMsgViewAdapter(getActivity(), mDataArrays);  
        listview.setAdapter(mAdapter);  
    }  
}
