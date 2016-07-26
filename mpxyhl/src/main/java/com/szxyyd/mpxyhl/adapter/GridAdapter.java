package com.szxyyd.mpxyhl.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szxyyd.mpxyhl.R;
import com.szxyyd.mpxyhl.utils.Bimp;

/**
 * Created by jq on 2016/7/16.
 */
public class GridAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    private boolean shape;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public GridAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    public boolean isShape() {
        return shape;
    }
    public void update() {
        loading();
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    @Override
    public int getCount() {
        if(Bimp.tempSelectBitmap.size() == 9){
            return 9;
        }
        return (Bimp.tempSelectBitmap.size() + 1);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position ==Bimp.tempSelectBitmap.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_addpic_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        Bimp.max += 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
    }
}
