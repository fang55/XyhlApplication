package com.szxyyd.mpxyhl.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.http.VolleyRequestUtil;
import com.szxyyd.mpxyhl.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhl.utils.ImageTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 设置内容
 * Created by fq on 2016/7/6.
 */
public class SetContentActivity extends Activity implements View.OnClickListener{
    private TextView tv_title  = null;
    private TextView tv_add  = null;
    private TextView tv_sex = null; //性别
    private TextView tv_boy = null;
    private TextView tv_gril = null;
    private TextView tv_pepleName = null; //昵称
    private LinearLayout ll_content = null;
    private LayoutInflater inflater = null;
    private int type;
    private EditText et_updname = null;
    private EditText et_original = null;
    private EditText et_new = null;
    private EditText et_renew = null;
    private EditText et_question = null;
    private  LinearLayout ll_boy = null;
    private  LinearLayout ll_gril = null;
    private Dialog  dialog = null;
    private SharedPreferences preferences;
    private String usrId;
    private static final int SCALE = 3;//照片缩小比例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setcontent);
        type = getIntent().getIntExtra("type",0);
        inflater = LayoutInflater.from(this);
        initView();
        accordingToType(type);
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        usrId = preferences.getString("userid","");
    }
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_add = (TextView) findViewById(R.id.tv_add);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }
    /**
     * 根据类型显示界面
     * @param type
     */
 private void accordingToType(int type){
     switch (type){
         case 1:
             showInfoView();
             break;
         case 2:
             showRevisePsdView();
             break;
         case 4:
             showAboutView();
             break;
         case 6:
             showServiceProtocolView();
             break;
     }
 }
    /**
     * 显示个人资料
     */
    private void showInfoView(){
        tv_title.setText(getString(R.string.text_info));
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_info,null,false);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_pepleName = (TextView) view.findViewById(R.id.tv_pepleName);
        RelativeLayout rl_data = (RelativeLayout) view.findViewById(R.id.rl_data);
        RelativeLayout rl_set_sex = (RelativeLayout) view.findViewById(R.id.rl_set_sex);
        RelativeLayout rl_set_name = (RelativeLayout) view.findViewById(R.id.rl_set_name);
        rl_data.setOnClickListener(this);
        rl_set_sex.setOnClickListener(this);
        rl_set_name.setOnClickListener(this);
        rl_data.setOnClickListener(this);
        ll_content.addView(view);
    }
    //修改称呢
    private void nameDialog(){
        dialog = new Dialog(SetContentActivity.this,R.style.dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_change_name);
        TextView tv_set_title = (TextView) window.findViewById(R.id.tv_set_title);
        tv_set_title.setText("修改称呢");
        et_updname = (EditText) window.findViewById(R.id.et_updname);
        Button btn_cleak = (Button) window.findViewById(R.id.btn_cleak);
        Button btn_set_back = (Button) window.findViewById(R.id.btn_set_back);
        TextView btn_set_add = (TextView) window.findViewById(R.id.btn_set_submit);
        btn_cleak.setOnClickListener(new nameClick());
        btn_set_back.setOnClickListener(new nameClick());
        btn_set_add.setOnClickListener(new nameClick());
        btn_cleak.setTag(1);
        btn_set_back.setTag(2);
        btn_set_add.setTag(3);
    }
    class nameClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int tag = (int) view.getTag();
            switch (tag){
                case 1:
                   et_updname.setText("");
                    break;
                case 2:
                    dialog.cancel();
                    break;
                case 3:
                    tv_pepleName.setText(et_updname.getText().toString());
                    submitData(1);
                    dialog.cancel();
                    break;
            }
        }
    }
    //性别对话框
    private void sexDialog(){
        dialog = new Dialog(SetContentActivity.this,R.style.dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_sex);
        TextView tv_set_title = (TextView) window.findViewById(R.id.tv_set_title);
        tv_set_title.setText("选择性别");
        tv_boy = (TextView) findViewById(R.id.tv_boy);
        tv_gril = (TextView) findViewById(R.id.tv_gril);
        Button btn_return = (Button) window.findViewById(R.id.btn_set_back);
        TextView btn_set_submit = (TextView) window.findViewById(R.id.btn_set_submit);
        ll_boy = (LinearLayout) window.findViewById(R.id.ll_boy);
        ll_gril = (LinearLayout) window.findViewById(R.id.ll_gril);
        btn_return.setTag(1);
        btn_set_submit.setTag(2);
        ll_boy.setTag(3);
        ll_gril.setTag(4);
        btn_return.setOnClickListener(new sexClick());
        btn_set_submit.setOnClickListener(new sexClick());
        ll_boy.setOnClickListener(new sexClick());
        ll_gril.setOnClickListener(new sexClick());
    }
    class sexClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int tag = (int) view.getTag();
            switch (tag){
                case 1:
                    dialog.cancel();
                    break;
                case 2:  //提交

                    dialog.cancel();
                    break;
                case 3:
                //    ll_boy.setBackgroundResource(R.mipmap.def_selsex);
                    break;
                case 4:
                //    ll_gril.setBackgroundResource(R.mipmap.def_selsex);
                 //   ll_boy.setBackgroundResource(0);
                    break;
            }
        }
    }
    /**
     * 显示修改密码
     */
    private void showRevisePsdView(){
        tv_title.setText(getString(R.string.text_revise));
        tv_add.setVisibility(View.VISIBLE);
        tv_add.setText("提交");
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_revise,null,false);
        et_original = (EditText) findViewById(R.id.et_original);
        et_new = (EditText) findViewById(R.id.et_new);
        et_renew = (EditText) findViewById(R.id.et_renew);
        ll_content.addView(view);
    }
    /**
     * 显示意见反馈
     */
    private void showAdviceView(){
        tv_title.setText(getString(R.string.text_advice));
        tv_add.setVisibility(View.VISIBLE);
        tv_add.setText("提交");
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_advice,null,false);
        et_question = (EditText) findViewById(R.id.et_question);
        ll_content.addView(view);
    }
    /**
     * 显示关于我们
     */
    private void showAboutView(){
        tv_title.setText(getString(R.string.text_about));
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_about,null,false);
        ll_content.addView(view);
    }
    /**
     * 显示服务协议
     */
    private void showServiceProtocolView(){
        tv_title.setText(getString(R.string.text_protocol));
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_protocol,null,false);
        ll_content.addView(view);
    }
    /**
     * 提交数据
     */
    private void submitData(final int type){
        String url = null;
        switch (type){
            case 1:  //修改昵称
                String name = et_updname.getText().toString();
                url = Constant.cstNameUpdUrl + "&id="+usrId +"&nickname="+name;
                break;
            case 2: //修改密码
                String newPsd = et_original.getText().toString();
                String surePsd = et_renew.getText().toString();
                if(surePsd == null){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                url = Constant.cstPwdUpdUrl + "&id="+usrId +"&pwd="+newPsd +"&pwd2="+surePsd;
                break;
            case 3: //意见反馈
                url = Constant.respUpdAllUrl +"&id="+usrId+"&resp="+et_question.getText().toString();
                break;
        }
        VolleyRequestUtil.newInstance().RequestGet(this, url, "upate",
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

                break;
            case 2:

                break;
        }
    }
    /**
     * 选择照片或者拍照
     */
    public void showPicturePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SetContentActivity.this);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Constant.TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"user.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, Constant.TAKE_PICTURE);
                        break;
                    case Constant.CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, Constant.CHOOSE_PICTURE);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/user.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    //将处理过的图片显示在界面上，并保存到本地
                 //   mActivity.iv_message_photo.setImageBitmap(newBitmap);
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                    break;
                case Constant.CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                         //   mActivity.iv_message_photo.setImageBitmap(smallBitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
               finish();
                break;
            case R.id.rl_data: //修改头像
                showPicturePicker();
                break;
            case R.id.rl_set_name: //修改昵称
                nameDialog();
                break;
            case R.id.rl_set_sex: //选择性别
                sexDialog();
                break;
            case R.id.tv_add: //提交
             if(tv_title.getText().equals(getString(R.string.text_revise))){ //修改密码
                 submitData(2);
             }else{ //意见反馈
                 submitData(3);
             }
                break;
        }
    }
}
