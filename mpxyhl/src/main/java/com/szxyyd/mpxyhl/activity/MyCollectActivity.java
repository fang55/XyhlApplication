package com.szxyyd.mpxyhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.adapter.CollectAdapter;
import com.szxyyd.mpxyhl.http.HttpMethods;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import com.szxyyd.mpxyhl.modle.JsonBean;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.modle.ProgressSubscriber;
import com.szxyyd.mpxyhl.view.PopupDialog;
import java.util.List;

/**
 * 我的收藏
 * Created by fq on 2016/7/6.
 */
public class MyCollectActivity extends Activity implements AdapterView.OnItemLongClickListener{
    private GridView gv_collect;
    private List<NurseList> listNurse;
    private CollectAdapter adapter;
    private int collectPotion;
    private boolean isShowDelete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        lodeCollectData();
        initView();
    }
    private void initView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.text_collect));
        Button btn_back = (Button) findViewById(R.id.btn_back);
        gv_collect = (GridView) findViewById(R.id.gv_collect);
        gv_collect.setOnItemLongClickListener(this);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 弹出询问是否删除地址
     */
    private void showDelectDialog(){
        PopupDialog dialog = new PopupDialog(MyCollectActivity.this,"collect");
        dialog.setonSelectListener(new PopupDialog.onSelectClickListener() {
            @Override
            public void onSure() {
                submitCancleData();
            }
        });
        dialog.initView();
    }
    SubscriberOnNextListener getFvnurDelOnNext = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String str) {
            Toast.makeText(MyCollectActivity.this,"已删除",Toast.LENGTH_SHORT).show();
            listNurse.remove(collectPotion);
            gv_collect.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };
    /**
     * 提交取消收藏
     * collectId 收藏id
     */
    private void submitCancleData(){
        int collectId =  listNurse.get(collectPotion).getFvrid();
        HttpMethods.getInstance().submitFvrnurDelData("cstFvrnurDel",String.valueOf(collectId),new ProgressSubscriber<String>(getFvnurDelOnNext,this));
    }

    /**
     * 获取收藏列表
     * cstId 用户id
     *
     */
    private void lodeCollectData(){
        HttpMethods.getInstance().getFvrnurListData("cstFvrnurList",Constant.cstId,new ProgressSubscriber<JsonBean>(getFvnurOnNext,this));
    }
    private SubscriberOnNextListener getFvnurOnNext = new SubscriberOnNextListener<JsonBean>() {
        @Override
        public void onNext(JsonBean jsonBean) {
            listNurse = jsonBean.getNurse();
            if(listNurse.size() != 0 ) {
                adapter = new CollectAdapter(MyCollectActivity.this,listNurse);
                adapter.setonSelectDelectListener(new CollectAdapter.onSelectDelectListener() {
                    @Override
                    public void onDelect(int position) {
                        collectPotion = position;
                        showDelectDialog();
                    }
                });
                gv_collect.setAdapter(adapter);
            }else{
                Toast.makeText(MyCollectActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (isShowDelete) {
            isShowDelete = false;
        } else {
            isShowDelete = true;
        }
        adapter.setIsShowDelete(isShowDelete);
        return true;
    }
}
