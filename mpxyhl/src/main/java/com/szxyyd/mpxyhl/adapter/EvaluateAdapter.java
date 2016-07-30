package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.activity.BaseApplication;
import com.szxyyd.mpxyhl.activity.Constant;
import com.szxyyd.mpxyhl.http.BitmapCache;
import com.szxyyd.mpxyhl.modle.DetailFile;
import com.szxyyd.mpxyhl.modle.NurseList;
import com.szxyyd.mpxyhl.utils.CommUtils;

import java.util.List;

/**
 * Created by jq on 2016/7/29.
 */
public class EvaluateAdapter extends BaseAdapter{
    private Context mContent;
    private List<NurseList> mList;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;
    public EvaluateAdapter(Context context,List<NurseList> list){
        mContent = context;
        mList = list;
        inflater = LayoutInflater.from(mContent);
        mQueue = BaseApplication.getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(contentView == null){
            contentView = inflater.inflate(R.layout.listview_item_evaluate,null,false);
            viewHolder  = new ViewHolder();
            viewHolder.tv_commTime = (TextView) contentView.findViewById(R.id.tv_commTime);
            viewHolder.tv_evcontent = (TextView) contentView.findViewById(R.id.tv_evcontent);
            viewHolder.ll_commStar = (LinearLayout) contentView.findViewById(R.id.ll_commStar);
            viewHolder.ll_commImage = (LinearLayout) contentView.findViewById(R.id.ll_commImage);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_evcontent.setText(mList.get(i).getContent());
        viewHolder.tv_commTime.setText(CommUtils.showData(mList.get(i).getAtpub()));
        showStar(viewHolder.ll_commStar,mList.get(i).getStar());
        showImage(viewHolder.ll_commImage,mList.get(i));
        return contentView;
    }
    private class ViewHolder{
        TextView tv_commTime;
        TextView tv_evcontent;
        LinearLayout ll_commStar;
        LinearLayout ll_commImage;
    }
    /**
     * 显示星数
     */
    private void showStar(LinearLayout linearLayout,String num){
        linearLayout.removeAllViews();
        int leng = Integer.parseInt(num) > 5 ? 5 : Integer.parseInt(num);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,0,10,0);
        for (int i = 0; i < leng; i++) {
            ImageView imageView = new ImageView(mContent);
            imageView.setBackground(mContent.getResources().getDrawable(R.mipmap.star));
            linearLayout.addView(imageView,layoutParams);
        }
    }
    /**
     * 显示图片
     */
    private void showImage(LinearLayout linearLayout,NurseList nurseList){
        linearLayout.removeAllViews();
        List<DetailFile> detailFiles = nurseList.getOrdFiles();
        for(int i = 0;i<detailFiles.size();i++){
            String imagePath = detailFiles.get(i).getImgname();
         //   Log.e("EvaluateAdapter","imagePath==="+imagePath);
            View view = inflater.inflate(R.layout.item_image,null,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_item);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.teach, R.mipmap.teach);
            mImageLoader.get(Constant.evaluateImage + imagePath, listener,100,100);
            linearLayout.addView(view);
        }
    }
}
