package com.szxyyd.xyhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.szxyyd.xyhl.R;

public class CommunityActivity extends Activity implements OnClickListener{
	private Button btn_back;
	private TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community);
		initView();
	}
	private void initView(){
		 tv_title = (TextView) findViewById(R.id.tv_title);
		 tv_title.setText("社区");
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

}
