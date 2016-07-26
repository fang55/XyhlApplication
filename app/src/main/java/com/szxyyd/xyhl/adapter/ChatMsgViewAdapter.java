package com.szxyyd.xyhl.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.szxyyd.xyhl.R;
import com.szxyyd.xyhl.modle.ChatMsgEntity;

public class ChatMsgViewAdapter extends BaseAdapter{
	public static interface IMsgViewType {  
        int IMVT_COM_MSG = 0;  
        int IMVT_TO_MSG = 1;  
    } 
	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();  
    private List<ChatMsgEntity> coll;
    private Context ctx;
    private LayoutInflater mInflater;  
    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {  
        ctx = context;  
        this.coll = coll;  
        mInflater = LayoutInflater.from(ctx);  
    }
	@Override
	public int getCount() {
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		 ChatMsgEntity entity = coll.get(position);  
	        if (entity.getType() == 0) {  
	            return 0;  
	        } else {  
	            return 1;  
	        }  
	}
	  
	 @Override
	public int getViewTypeCount() {
		return 2;
	}
	  
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		  ChatMsgEntity entity = coll.get(position);  
	        ViewHolder viewHolder = null;  
	        if (convertView == null) {  
	            if (entity.getType() == 0) {  
	                convertView = mInflater.inflate(  
	                        R.layout.chat_item_left, null);
	            } else {  
	                convertView = mInflater.inflate(  
	                        R.layout.chat_item_right, null);  
	            }  
	            viewHolder = new ViewHolder();
	            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_text);
	            
	            convertView.setTag(viewHolder);  
	        }else{
	        	 viewHolder = (ViewHolder) convertView.getTag();  
	        }
	        
	        viewHolder.tvContent.setText(entity.getName());  
		return convertView;
	}
	 static class ViewHolder {  
	        public TextView tvSendTime;  
	        public TextView tvUserName;  
	        public TextView tvContent;  
	        public TextView tvTime;  
	        public boolean isComMsg = true;  
	    }  

}
