package com.szxyyd.xyhl.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.http.VolleyRequestUtil;
import com.szxyyd.xyhl.inf.VolleyListenerInterface;

/**
 * 设置界面
 * 
 * @author jq
 * 
 */
public class SetActivity extends Activity implements OnClickListener {
	private Button btn_back;
	private String state;
	private LinearLayout ll_content;
	private RelativeLayout rl_data;
	private RelativeLayout rl_update;
	private RelativeLayout rl_idea;
	private RelativeLayout rl_about;
	private RelativeLayout rl_check;
	private RelativeLayout rl_protocol;
	private EditText et_newpsd; //新密码
	private EditText et_surepsd; //确认密码
	private EditText et_updname; //昵称
	private TextView tv_pepleName;//
	private TextView tv_olepassword;//旧密码
	private EditText et_question; //内容描述
	private Dialog alertDialog;
	private Dialog nameDialog;
	private SharedPreferences preferences;
	private String usrId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_set);
		state = getIntent().getStringExtra("state");
		initView();
		initEvent();
		 preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
		 usrId = preferences.getString("userid","");
	}
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		rl_data = (RelativeLayout) findViewById(R.id.rl_data);
		rl_update = (RelativeLayout) findViewById(R.id.rl_update);
		rl_idea = (RelativeLayout) findViewById(R.id.rl_idea);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_check = (RelativeLayout) findViewById(R.id.rl_check);
		rl_protocol = (RelativeLayout) findViewById(R.id.rl_protocol);
	}

	private void initEvent() {
		rl_data.setOnClickListener(this);
		rl_data.setOnClickListener(this);
		rl_update.setOnClickListener(this);
		rl_idea.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_check.setOnClickListener(this);
		rl_protocol.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
            finish();
			break;
		case R.id.rl_data:
			showDiffDialog(0);
			break;
		case R.id.rl_update:
			showDiffDialog(1);
			break;
		case R.id.rl_idea:
			showDiffDialog(2);
			break;
		case R.id.rl_about:
			showDiffDialog(3);
			break;
		case R.id.rl_check:
			showDiffDialog(4);
			break;
		case R.id.rl_protocol:
			showDiffDialog(5);
			break;
		default:
			break;
		}
	}

	/**
	 * 个人资料对话框
     */
	private void dateDialog(){
		final Dialog dialog = new Dialog(SetActivity.this,R.style.dialog);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_personal_data);
		tv_pepleName  = (TextView) window.findViewById(R.id.tv_pepleName);
		Button btn_return = (Button) window.findViewById(R.id.btn_back);
		btn_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.cancel();
			}
		});
		RelativeLayout rl_set_name = (RelativeLayout) window.findViewById(R.id.rl_set_name);
		rl_set_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				nameDialog();
			}
		});
		RelativeLayout rl = (RelativeLayout)window.findViewById(R.id.rl_set_sex);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sexDialog();
			}
		});
	}
	//修改称呢
	private void nameDialog(){
		nameDialog = new Dialog(SetActivity.this,R.style.dialog);
		nameDialog.show();
		Window window = nameDialog.getWindow();
		window.setContentView(R.layout.dialog_change_name);
		TextView tv_set_title = (TextView) window.findViewById(R.id.tv_set_title);
		tv_set_title.setText("修改称呢");
		et_updname = (EditText) window.findViewById(R.id.et_updname);
		Button btn_cleak = (Button) window.findViewById(R.id.btn_cleak);
		Button btn_set_back = (Button) window.findViewById(R.id.btn_set_back);
		TextView btn_set_add = (TextView) window.findViewById(R.id.btn_set_add);
		btn_cleak.setOnClickListener(new nameClick());
		btn_set_back.setOnClickListener(new nameClick());
		btn_set_add.setOnClickListener(new nameClick());
		btn_cleak.setTag(1);
		btn_set_back.setTag(2);
		btn_set_add.setTag(3);

	}
	class nameClick implements OnClickListener{
		@Override
		public void onClick(View view) {
			int tag = (int) view.getTag();
          switch (tag){
			  case 1:
				  et_updname.setText("");
				  break;
			  case 2:

				  break;
			  case 3:
				  submitData(1);
				  nameDialog.cancel();
				  break;
		  }
		}
	}
  //性别对话框
	private void sexDialog(){
		final Dialog alertDialog = new Dialog(SetActivity.this,R.style.dialog);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_sex);
		Button btn_return = (Button) window.findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
	}

	/**
	 * 修改密码对话框
     */
	private void fixPassword(){
		alertDialog = new Dialog(SetActivity.this,R.style.dialog);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_change_password);
		TextView tv_set_title = (TextView) window.findViewById(R.id.tv_set_title);
		tv_set_title.setText("修改密码");
		Button btn_set_back = (Button) window.findViewById(R.id.btn_set_back);
		tv_olepassword = (TextView) window.findViewById(R.id.tv_olepassword);
		et_newpsd = (EditText) window.findViewById(R.id.et_newpsd);
		et_surepsd = (EditText) window.findViewById(R.id.et_surepsd);
		TextView btn_set_add = (TextView) window.findViewById(R.id.btn_set_add);
		btn_set_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		btn_set_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				submitData(2);
				alertDialog.cancel();
			}
		});
	}
	/**
	 * 意见反馈
	 *
     */
	private void ideaDialog(){
		 alertDialog = new Dialog(SetActivity.this,R.style.dialog);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_idea);
		Button btn_set_back = (Button) window.findViewById(R.id.btn_set_back);
		TextView btn_set_add = (TextView) window.findViewById(R.id.btn_set_add);
		 et_question = (EditText) window.findViewById(R.id.et_question);
		btn_set_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				alertDialog.cancel();
			}
		});
		btn_set_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				submitData(3);
				alertDialog.cancel();
			}
		});
	}
	private void showDiffDialog(int index){
		switch (index) {
		case 0:
			dateDialog();
			break;
		case 1:
			fixPassword();
			break;
		case 2:
			ideaDialog();
			break;
		case 3:
			alertDialog = new Dialog(SetActivity.this,R.style.dialog);
			alertDialog.show();
			Window window3 = alertDialog.getWindow();
			window3.setContentView(R.layout.view_about);
			TextView tv_about_title = (TextView) window3.findViewById(R.id.tv_title);
			tv_about_title.setText("关于我们");
			Button btn_back = (Button) window3.findViewById(R.id.btn_back);
			btn_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.cancel();
				}
			});
			break;
		case 4:
			alertDialog = new Dialog(SetActivity.this,R.style.dialog);
			alertDialog.show();
			Window window4 = alertDialog.getWindow();
			window4.setContentView(R.layout.view_check);
			TextView tv_check_title = (TextView) window4.findViewById(R.id.tv_title);
			tv_check_title.setText("检查版本");
			Button btn_back4 = (Button) window4.findViewById(R.id.btn_back);
			btn_back4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.cancel();
				}
			});
			break;
		case 5:
			alertDialog = new AlertDialog.Builder(SetActivity.this).create();
			alertDialog.show();
			Window window5 = alertDialog.getWindow();
			window5.setContentView(R.layout.dialog_protocol);
			TextView tv_title = (TextView) window5.findViewById(R.id.tv_title);
			tv_title.setText("服务协议");
			Button btn_back5 = (Button) window5.findViewById(R.id.btn_back);
			btn_back5.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					alertDialog.cancel();
				}
			});
			break;
		default:
			break;
		}
	}
	/**
	 * 提交数据
	 */
	private void submitData(final int type){
		String url = null;
		switch (type){
			case 1:
				String name = et_updname.getText().toString();
				url = Constant.cstNameUpdUrl + "&id="+usrId
						+"&nickname="+name;
				break;
			case 2:
                String newPsd = tv_olepassword.getText().toString();
				String surePsd = et_surepsd.getText().toString();
				if(tv_olepassword == null){
					Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
					return;
				}
				url = Constant.cstPwdUpdUrl + "&id="+usrId
						+"&pwd="+newPsd
						+"&pwd2="+surePsd;
				break;
			case 3:
                url = Constant.respUpdAllUrl +"&id="+usrId+"&resp="+et_question.getText().toString();
				break;
		}
		VolleyRequestUtil volley = new VolleyRequestUtil();
		volley.RequestGet(this, url, "upate",
				new VolleyListenerInterface(this,VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
					@Override
					public void onSuccess(String result) {
						Log.e("SetActivity", "result=="+result);
						parserData(result,type);
					}
					@Override
					public void onError(VolleyError error) {

					}
				});

	}
	private void parserData(String result,int type){
		switch (type){
			case 1:
				tv_pepleName.setText(et_updname.getText().toString());
				break;
			case 2:

				break;
		}
	}
}
