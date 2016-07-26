package com.szxyyd.xyhl.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.adapter.MassageAdapter;
import com.szxyyd.xyhl.fragment.ChatFragment;

public class MessageActivity extends FragmentActivity implements OnClickListener{
	private ListView listview;
	private Button btn_back;
	private TextView tv_title;
	private MassageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initView();
		showChatFragment();
	}
	 private void initView(){
		 tv_title = (TextView) findViewById(R.id.tv_title);
		 tv_title.setText("消息");
		 btn_back = (Button) findViewById(R.id.btn_back);
		 btn_back.setVisibility(View.VISIBLE);
		 btn_back.setOnClickListener(this);
	    	listview = (ListView)findViewById(R.id.listview);
	    	listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					
				}
			});
	    	adapter = new MassageAdapter(MessageActivity.this);
	    	listview.setAdapter(adapter);
	    }
	    private void showChatFragment(){
	    	FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			ChatFragment chatFragment = null;
			if (chatFragment == null) {
				chatFragment = new ChatFragment();
				transaction.add(R.id.ll_right_content, chatFragment);
			} else {
				transaction.show(chatFragment);
			}
			transaction.commit();
	    }
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.btn_back) {
				finish();
			}
		}
}
