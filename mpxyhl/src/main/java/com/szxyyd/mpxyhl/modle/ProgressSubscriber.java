package com.szxyyd.mpxyhl.modle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.szxyyd.mpxyhl.inter.SubscriberOnNextListener;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public class ProgressSubscriber<T> extends Subscriber<T> {
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private WeakReference<Context> mActivity;
    //    是否能取消请求
    private boolean cancel;
    //    加载框可自己定义
    private ProgressDialog pd;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<>(context);
        this.cancel = true;
        initProgressDialog();
    }
    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context, boolean cancel) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mActivity = new WeakReference<>(context);
        this.cancel = cancel;
        initProgressDialog();
    }
    /**
     * 初始化加载框
     */
    private void initProgressDialog() {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }
    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
      //  Toast.makeText(BaseApplication.getInstance(), "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }
    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Context context = mActivity.get();
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ProgressSubscriber","error:==="+e.getMessage());
        }
        dismissProgressDialog();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }
    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}