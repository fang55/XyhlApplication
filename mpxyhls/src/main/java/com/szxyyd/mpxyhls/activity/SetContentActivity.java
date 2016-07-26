package com.szxyyd.mpxyhls.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.utils.ImageTools;
import com.szxyyd.mpxyhls.view.RoundImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by jq on 2016/7/18.
 */
public class SetContentActivity extends Activity implements View.OnClickListener{
    private TextView tv_title;
    private TextView tv_add;
    private TextView tv_sex; //性别
    private TextView tv_boy;
    private TextView tv_gril;
    private TextView tv_pepleName; //昵称
    private RoundImageView iv_photo;
    private LinearLayout ll_content;
    private LayoutInflater inflater;
    private int type;
    private EditText et_updname;
    private EditText et_original;
    private EditText et_new;
    private EditText et_renew;
    private EditText et_question;
    private  LinearLayout ll_boy;
    private  LinearLayout ll_gril;
    private Dialog dialog;
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
            case Constant.SET_INFO:
                showInfoView();
                break;
            case Constant.SET_REVISE:
                showRevisePsdView();
                break;
            case Constant.SET_ADVICE:
                showAdviceView();
                break;
            case Constant.SET_ABOUT:
                showAboutView();
                break;
            case Constant.SET_CHECK:  //检查版本
                showVersionsView();
                break;
            case Constant.SET_PROTOCOL:
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
        View view = inflater.inflate(R.layout.view_people,null,false);
        tv_sex = (TextView) view.findViewById(R.id.tv_sex);
        tv_pepleName = (TextView) view.findViewById(R.id.tv_pepleName);
        iv_photo = (RoundImageView) findViewById(R.id.iv_photo);
        RelativeLayout rl_data = (RelativeLayout) view.findViewById(R.id.rl_data);
        RelativeLayout rl_set_sex = (RelativeLayout) view.findViewById(R.id.rl_set_sex);
        RelativeLayout rl_set_name = (RelativeLayout) view.findViewById(R.id.rl_set_name);
        rl_data.setOnClickListener(this);
        rl_set_sex.setOnClickListener(this);
        rl_set_name.setOnClickListener(this);
        rl_data.setOnClickListener(this);
        ll_content.addView(view);
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
     * 显示检查版本
     */
    private void showVersionsView(){
        ll_content.removeAllViews();
        View view = inflater.inflate(R.layout.view_versions,null,false);
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
                    iv_photo.setImageBitmap(newBitmap);
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
                            iv_photo.setImageBitmap(smallBitmap);
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
       }
    }
}
