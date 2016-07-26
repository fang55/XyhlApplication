package com.szxyyd.xyhl.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.fragment.CollectFragment;
import com.szxyyd.xyhl.fragment.EarningsFragment;
import com.szxyyd.xyhl.fragment.OrderFragment;
import com.szxyyd.xyhl.fragment.ServiceLocationFragment;
import com.szxyyd.xyhl.http.BitmapCache;
import com.szxyyd.xyhl.view.MenuPopupWindow;
import com.szxyyd.xyhl.view.RoundImageView;

public class MyActivity extends FragmentActivity implements OnClickListener {
    private TextView tv_name;
    private RoundImageView iv_people;
    private LinearLayout ll_set;
    private LinearLayout ll_collect;
    private LinearLayout ll_order;
    private LinearLayout ll_location;
    private LinearLayout ll_integral;
    private TextView tv_order;
    private TextView tv_integral;
    private TextView tv_location;
    private TextView tv_collect;
    private TextView tv_set;
    private Button btn_back;
    private Button btn_navigation;
    private TextView tv_title;
    private OrderFragment orderFragment = null;
    private CollectFragment collectFragment = null;
    private ServiceLocationFragment locationFragment;
    private EarningsFragment earningsFragment = null;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue ;
    private MenuPopupWindow popupWindow;
    private BitmapCache bitmapCache= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();
        showFragment(0);
        setBgColor(0);
        mQueue = BaseApplication.getRequestQueue();
        bitmapCache = new BitmapCache();
        mImageLoader = new ImageLoader(mQueue, bitmapCache);
        showImae();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_people = (RoundImageView) findViewById(R.id.iv_people);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的");
        tv_order = (TextView) findViewById(R.id.tv_order);
        tv_integral = (TextView) findViewById(R.id.tv_integral);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_set = (TextView) findViewById(R.id.tv_set);
        btn_back = (Button) findViewById(R.id.btn_back);
        Button btn_def_msg = (Button) findViewById(R.id.btn_def_msg);
        btn_def_msg.setVisibility(View.GONE);
        btn_navigation = (Button) findViewById(R.id.btn_navigation);
        btn_back.setOnClickListener(this);
        btn_navigation.setOnClickListener(this);
        ll_order = (LinearLayout) findViewById(R.id.ll_order);
        ll_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setBgColor(0);
                showFragment(0);
            }
        });
        ll_integral = (LinearLayout) findViewById(R.id.ll_integral);
        ll_integral.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setBgColor(1);
                showFragment(1);
            }
        });

        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        ll_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setBgColor(2);
                showFragment(2);
            }
        });
        ll_collect = (LinearLayout) findViewById(R.id.ll_collect);
        ll_collect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                setBgColor(3);
                showFragment(3);
            }
        });
        ll_set = (LinearLayout) findViewById(R.id.ll_set);
        ll_set.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                setBgColor(4);
                Intent intent = new Intent(MyActivity.this, SetActivity.class);
                intent.putExtra("state", "set");
                startActivity(intent);
            }
        });
    }

    private void showImae() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String imge = preferences.getString("icon", "");
        String nickname = preferences.getString("nickname", "");
        tv_name.setText(nickname + "");
       if (imge != null) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_people, 0, R.drawable.teach);
            mImageLoader.get("http://183.232.35.71:8080/upload//icon//15712956260.png", listener);
        }
    }

    private void showFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    transaction.add(R.id.ll_order_content, orderFragment);
                } else {
                    transaction.show(orderFragment);
                }
                break;
            case 1:
                if (earningsFragment == null) {
                    earningsFragment = new EarningsFragment();
                    transaction.add(R.id.ll_order_content, earningsFragment);
                } else {
                    transaction.show(earningsFragment);
                }
                break;
            case 2:
                if (locationFragment == null) {
                    locationFragment = new ServiceLocationFragment();
                    transaction.add(R.id.ll_order_content, locationFragment);
                } else {
                    transaction.show(locationFragment);
                }
                break;
            case 3:
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    transaction.add(R.id.ll_order_content, collectFragment);
                } else {
                    transaction.show(collectFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (earningsFragment != null) {
            transaction.hide(earningsFragment);
        }
        if (locationFragment != null) {
            transaction.hide(locationFragment);
        }
        if (collectFragment != null) {
            transaction.hide(collectFragment);
        }

    }
private void ChangeLinearLayoutBg(LinearLayout ll1,LinearLayout ll2,LinearLayout ll3,LinearLayout ll4,LinearLayout ll5){
    ll1.setBackgroundColor(getResources().getColor(R.color.tv_login));
    ll2.setBackgroundColor(getResources().getColor(R.color.white));
    ll3.setBackgroundColor(getResources().getColor(R.color.white));
    ll4.setBackgroundColor(getResources().getColor(R.color.white));
    ll5.setBackgroundColor(getResources().getColor(R.color.white));
}
private void ChangeTextColor(TextView tv1,TextView tv2,TextView tv3,TextView tv4,TextView tv5){
    tv1.setTextColor(getResources().getColor(R.color.white));
    tv2.setTextColor(getResources().getColor(R.color.black));
    tv3.setTextColor(getResources().getColor(R.color.black));
    tv4.setTextColor(getResources().getColor(R.color.black));
    tv5.setTextColor(getResources().getColor(R.color.black));

}

    private void setBgColor(int index) {
        switch (index) {
            case 0:
                ChangeLinearLayoutBg(ll_order,ll_integral,ll_location,ll_collect,ll_set);
                ChangeTextColor(tv_order,tv_integral,tv_location,tv_collect,tv_set);
                break;
            case 1:
                ChangeLinearLayoutBg(ll_integral,ll_order,ll_location,ll_collect,ll_set);
                ChangeTextColor(tv_integral,tv_order,tv_location,tv_collect,tv_set);
                break;
            case 2:
                ChangeLinearLayoutBg(ll_location,ll_order,ll_integral,ll_collect,ll_set);
                ChangeTextColor(tv_location,tv_order,tv_integral,tv_collect,tv_set);
                break;
            case 3:
                ChangeLinearLayoutBg(ll_collect,ll_order,ll_integral,ll_location,ll_set);
                ChangeTextColor(tv_collect,tv_order,tv_integral,tv_location,tv_set);
                break;
            case 4:
                ChangeLinearLayoutBg(ll_set,ll_order,ll_integral,ll_location,ll_collect);
                ChangeTextColor(tv_set,tv_order,tv_integral,tv_location,tv_collect);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_navigation:
                popupWindow = new MenuPopupWindow(MyActivity.this);
                popupWindow.initPopupWindow(btn_navigation,124,372);
                popupWindow.setOutsideTouchable(true);
                break;
            default:
                break;
        }
    }
}
