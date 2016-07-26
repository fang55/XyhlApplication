package com.szxyyd.mpxyhls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.modle.NurseTrain;
import com.szxyyd.mpxyhls.utils.ExampleUtil;

import java.util.List;

/**
 * Created by jq on 2016/6/14.
 */
public class TrainAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private LayoutInflater inflater;
    private List<NurseTrain> nurseTrainsList;
    private onCheckClickListener mListener;
    private boolean isSelect = false;
    public TrainAdapter(Context context, List<NurseTrain> data){
        mContext = context;
        nurseTrainsList = data;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return nurseTrainsList.size() == 0 ? 0 : nurseTrainsList.size();
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        NurseTrain nurseTrain = nurseTrainsList.get(position);
        ViewHolder viewHolder;
        if(contentView == null){
            contentView = inflater.inflate(R.layout.el_job_children,null,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_sel = (ImageView) contentView.findViewById(R.id.iv_sel);
            viewHolder.tv_school = (TextView) contentView.findViewById(R.id.tv_school);
            viewHolder.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            viewHolder.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            viewHolder.iv_sel.setOnClickListener(this);
            contentView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.tv_school.setText(nurseTrain.getTitle());
        String time = ExampleUtil.showData(nurseTrain.getAtrec1()) + "-" + ExampleUtil.showData(nurseTrain.getAtrec2());
        viewHolder.tv_time.setText(time);
        viewHolder.tv_content.setText(nurseTrain.getScore());
        viewHolder.iv_sel.setTag(position);
        return contentView;
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_sel){
            if(isSelect){
                view.setBackgroundResource(R.mipmap.iv_radio);
                isSelect = false;
            }else{
                isSelect = true;
                view.setBackgroundResource(R.mipmap.iv_dwon_radio);
                if(mListener != null){
                    int id = Integer.parseInt(view.getTag().toString());
                    mListener.onCheckClick(id);
                }
            }
        }
    }
    static class ViewHolder{
        TextView tv_school;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_sel;
    }
    public void setOnCheckListener(onCheckClickListener listener){
        mListener = listener;
    }
    public interface onCheckClickListener{
        void onCheckClick(int position);
    }
}
