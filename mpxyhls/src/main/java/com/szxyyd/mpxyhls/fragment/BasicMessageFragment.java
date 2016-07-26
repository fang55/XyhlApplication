package com.szxyyd.mpxyhls.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.ArchivesActivity;
import com.szxyyd.mpxyhls.activity.BasicContentActivity;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.adapter.ListAdapter;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.Nurse;
import com.szxyyd.mpxyhls.utils.ImageTools;
import com.szxyyd.mpxyhls.utils.UploadUtil;
import com.szxyyd.mpxyhls.view.RoundImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本信息
 * Created by jq on 2016/7/13.
 */
public class BasicMessageFragment extends Fragment{
    private View rootView;
    private ListView ll_arInfo;
    private ListView ll_arContent;
    private RoundImageView iv_photo;
    private String[] infoData = new String[]{"姓名","身份证号","手机号","等级","年龄","民族"};
    private String[] contentData = new String[]{"籍贯","属相","星座","学历"};
    private List<String> lisInfo = new ArrayList<>();
    private List<String> listContent = new ArrayList<>();
    private List<String> contentCode = new ArrayList<>();
    private int mPosition;
    private ListAdapter listAdapter;
    private ArchivesActivity mActivity;
    private ReceiveBroadCast receiveBroadCast;  //广播实例
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int SCALE = 3;//照片缩小比例
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null) {
            mActivity = (ArchivesActivity) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_basic,container,false);
        initView();
        initData();
        showInfoView();
        showContetnView();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROAD_LIST_ACTION);    //只有持有相同的action的接受者才能接收此广播
        mActivity.registerReceiver(receiveBroadCast, filter);
    }

    private void initView(){
        ll_arInfo = (ListView) rootView.findViewById(R.id.ll_arInfo);
        ll_arContent = (ListView) rootView.findViewById(R.id.ll_arContent);
        iv_photo = (RoundImageView) rootView.findViewById(R.id.iv_photo);
        RelativeLayout rl_photo = (RelativeLayout) rootView.findViewById(R.id.rl_photo);
        Button btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNurseUpdBasic();
            }
        });
        rl_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicturePicker();
            }
        });
    }
    private void initData(){
        Nurse nurse = mActivity.nurse;
        if(nurse != null) {
            lisInfo.add(nurse.getName());
            lisInfo.add(nurse.getPid());
            lisInfo.add(nurse.getMobile());
            lisInfo.add(nurse.getSvrid() + nurse.getLvl());
            lisInfo.add(nurse.getAge());
            lisInfo.add(nurse.getNation());

            listContent.add(nurse.getOrigo());
            listContent.add(nurse.getAnmsign());
            listContent.add(nurse.getZodiac());
            listContent.add(nurse.getEdu());
        }else{
            for(int i = 0;i<infoData.length;i++){
                lisInfo.add("未定义");
            }
            for(int i = 0;i<contentData.length;i++){
                listContent.add("未定义");
            }
        }
    }
    /**
     * 显示基本信息
     */
     private void showInfoView(){

         listAdapter = new ListAdapter(getActivity(),infoData,lisInfo);
         ll_arInfo.setAdapter(listAdapter);
         ll_arInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                 showInfoDialog(position);
             }
         });

     }
    /**
     * 弹出填写信息框
     */
    private void showInfoDialog(final int position){
        final Dialog alertDialog = new Dialog(mActivity, R.style.dialog);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.view_info);
        TextView tv_dialogTitle = (TextView) window.findViewById(R.id.tv_dialogTitle);
        tv_dialogTitle.setText(infoData[position].toString());
        final EditText et_content = (EditText) window.findViewById(R.id.et_content);
        Button btn_sure = (Button) window.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lisInfo.set(position,et_content.getText().toString());
                listAdapter = new ListAdapter(mActivity,infoData,lisInfo);
                ll_arInfo.setAdapter(listAdapter);
                alertDialog.cancel();
            }
        });
    }
    /**
     * 显示籍贯、属相等信息
     */
    private void showContetnView(){
        listAdapter = new ListAdapter(getActivity(),contentData,listContent);
        ll_arContent.setAdapter(listAdapter);
        ll_arContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mPosition = position;
                Intent intent = new Intent(getActivity(),BasicContentActivity.class);
                intent.putExtra("type",contentData[position]);
                startActivity(intent);
            }
        });
    }
    class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String resultName = intent.getStringExtra("name");
            String id = intent.getStringExtra("id");
            Toast.makeText(mActivity,"id=="+id,Toast.LENGTH_SHORT).show();
         //   Toast.makeText(mActivity,"resultName=="+resultName,Toast.LENGTH_SHORT).show();
            listContent.set(mPosition,resultName);
            for(int i = 0;i<contentData.length;i++){
                contentCode.add("0");
            }
            contentCode.set(mPosition,id);
            listAdapter = new ListAdapter(mActivity,contentData,listContent);
            ll_arContent.setAdapter(listAdapter);
        }
    }

    /**
     * 提交护理师信息修改
     */
    private void submitNurseUpdBasic(){
        Map<String,String> map = new HashMap<>();
        map.put("id","1294130"); //护理师id
        map.put("name",lisInfo.get(0)); //护理师姓名
        map.put("nickname","哈哈"); //昵称
        map.put("pid",lisInfo.get(1)); //身份号码
        map.put("nation","1"); //民族
        String[] code = new String[]{"origo","anmsign","zodiac","edu"};
      /*  for(int i = 0;i<code.length;i++){
            map.put(code[i],contentCode.get(i));
        }
        for(int i = 0;i<contentCode.size();i++){
           Log.e("Basic","contentCode.get(i)=="+contentCode.get(i).toString());
        }*/
        map.put("origo","14"); //籍贯
        map.put("anmsign","3"); //属相
        map.put("zodiac","1"); //星座
        map.put("edu","20"); //学历
        VolleyRequestUtil.newInstance().RequestPost(getActivity(), Constant.yxNurseUpdBasicUrl, "base",map,
                new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ArchivesTrainFragment", "submitNurseUpdBasic---result=="+result);
                        Toast.makeText(getActivity(),"已保存",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    /**
     * 选择照片或者拍照
     */
    public void showPicturePicker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void sumbitPhotoData(final  File file){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UploadUtil.uploadFile(file,"http://183.232.35.71:8080//upload//icon");
            }
        }).start();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();
                    //将处理过的图片显示在界面上，并保存到本地
                    iv_photo.setImageBitmap(newBitmap);
                    ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
                    File file = new File(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Log.e("BasicMessageFragment","file.getName()=="+file.getName());
                    sumbitPhotoData(file);
                    break;
                case CHOOSE_PICTURE:
                    ContentResolver resolver = mActivity.getContentResolver();
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
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(receiveBroadCast);
    }
}
