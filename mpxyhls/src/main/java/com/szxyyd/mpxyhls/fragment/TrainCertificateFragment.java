package com.szxyyd.mpxyhls.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szxyyd.mpxyhls.R;
import com.szxyyd.mpxyhls.activity.AddTrainActivity;
import com.szxyyd.mpxyhls.activity.BaseApplication;
import com.szxyyd.mpxyhls.activity.Constant;
import com.szxyyd.mpxyhls.adapter.CertificateAdapter;
import com.szxyyd.mpxyhls.adapter.InfoAdapter;
import com.szxyyd.mpxyhls.adapter.TrainAdapter;
import com.szxyyd.mpxyhls.http.VolleyRequestUtil;
import com.szxyyd.mpxyhls.inter.VolleyListenerInterface;
import com.szxyyd.mpxyhls.modle.Info;
import com.szxyyd.mpxyhls.modle.JsonBean;
import com.szxyyd.mpxyhls.modle.NurseTrain;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 培训与证书
 * Created by jq on 2016/7/13.
 */
public class TrainCertificateFragment extends Fragment implements View.OnClickListener{
    private View rootView;
    private ExpandableListView el_info;
    private ExpandableListView el_certificate;
    private RelativeLayout rl_job;
    private ListView lv_joncontent;
    private List<String> grParent = null;
    private List<String> zyParent = null;
    private Map<String, List<Info>> grMap = null;
    private Map<String, List<Info>> zyMap = null;
    private List<Info> grFiels;
    private List<Info> zyFiels;
    private List<NurseTrain> nurseTrainsList;
    private NurseTrain train;
    private InfoAdapter infoAdapter;
    private CertificateAdapter cerAdapter;
    private TrainAdapter traAdapter;
    private int Checkposition = -1;
    private ProgressDialog proDialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SUCCEED:
                    grMap = new HashMap<>();
                    grMap.put("个人信息",grFiels);
                    infoAdapter = new InfoAdapter(getActivity(),grParent,grMap);
                    el_info.setAdapter(infoAdapter);

                    zyMap = new HashMap<>();
                    zyMap.put("专业证书",zyFiels);
                    cerAdapter = new CertificateAdapter(getActivity(),zyParent,zyMap);
                    el_certificate.setAdapter(cerAdapter);
                    break;
                case Constant.LIST:
                    nurseTrainsList = (List<NurseTrain>) msg.obj;
                    showTrainList();
                    break;
                case Constant.DELECT:
                    int position = (int) msg.obj;
                    nurseTrainsList.remove(position);
                    traAdapter.notifyDataSetChanged();
                    lv_joncontent.setAdapter(traAdapter);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_train,container,false);
        lodeFileNurse();
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        lodeTraindata();
    }

    private void initView(){
        el_info = (ExpandableListView) rootView.findViewById(R.id.el_info);
        el_certificate = (ExpandableListView) rootView.findViewById(R.id.el_certificate);
        rl_job = (RelativeLayout) rootView.findViewById(R.id.rl_job);
        lv_joncontent = (ListView) rootView.findViewById(R.id.lv_joncontent);
        LinearLayout ll_delect = (LinearLayout) rootView.findViewById(R.id.ll_delect);
        LinearLayout ll_add = (LinearLayout) rootView.findViewById(R.id.ll_add);
        el_info.setGroupIndicator(null);
        el_certificate.setGroupIndicator(null);
        ll_delect.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        grParent = new ArrayList<>();
        grParent.add("个人信息");
        zyParent = new ArrayList<>();
        zyParent.add("专业证书");
        el_info.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(getActivity(),"el_info--childPosition=="+childPosition,Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        el_certificate.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(getActivity(),"el_certificate--childPosition=="+childPosition,Toast.LENGTH_SHORT).show();

                return true;
            }
        });
        lv_joncontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                train = nurseTrainsList.get(position);
                Intent intent = new Intent(getActivity(), AddTrainActivity.class);
                intent.putExtra("type","edit");
                Bundle bundle = new Bundle();
                bundle.putSerializable("train",train);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    /**
     * 显示培训列表
     */
    private void showTrainList(){
        if(nurseTrainsList.size() != 0) {
            traAdapter = new TrainAdapter(getActivity(), nurseTrainsList);
            lv_joncontent.setAdapter(traAdapter);
            traAdapter.notifyDataSetChanged();
            traAdapter.setOnCheckListener(new TrainAdapter.onCheckClickListener() {
                @Override
                public void onCheckClick(int position) {
                    Checkposition = position;
                }
            });
        }
    }
    /**
     * 获取护理师附件类别
     */
    private void lodeFileNurse(){
        proDialog = ProgressDialog.show(getActivity(), "", "正在加载.....");
        Map<String,String> map = new HashMap<String,String>();
        map.put("nurseid","1294110");
        VolleyRequestUtil.newInstance().RequestPost(getActivity(), Constant.findFileByNurseidUrl,"file",map,new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
            @Override
            public void onSuccess(String result) {
                Log.e("TrainCertificateFragment", "lodeFileNurse---result=="+result);
                proDialog.dismiss();
                parserFileData(result);
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    /**
     * 获取培训经历列表
     */
private void lodeTraindata(){
    Map<String,String> map = new HashMap<>();
    map.put("nurseid","1294110");
    VolleyRequestUtil.newInstance().GsonPostRequest(getActivity(),Constant.nurseTrainListAddUrl,"list" ,map,new TypeToken<JsonBean>(){},
            new Response.Listener<JsonBean>() {
                @Override
                public void onResponse(JsonBean jsonBean) {
                    List<NurseTrain> list = jsonBean.getNurseTrain();
                    Message message = new Message();
                    message.what = Constant.LIST;
                    message.obj = list;
                    handler.sendMessage(message);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
}

    private void parserFileData(String result){
        try {
            JSONObject json = new JSONObject(result);
            String jsonSvr = json.getString("grFiels");
            Type svrType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson svrgson = new Gson();
            grFiels = svrgson.fromJson(jsonSvr, svrType);

            String jsonNurse = json.getString("zyFiels");
            Type nurseType = new TypeToken<LinkedList<Info>>() {}.getType();
            Gson nursegson = new Gson();
            zyFiels = nursegson.fromJson(jsonNurse, nurseType);

            Message message = new Message();
            message.what = Constant.SUCCEED;
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 删除培训经历
     */
    private void submitDelectTrain(final int position){
        int trainId = nurseTrainsList.get(position).getId();
        Log.e("ArchivesTrainFragment", "submitDelectTrain---trainId=="+trainId);
        Map<String,String> map = new HashMap<>();
        map.put("id",String.valueOf(trainId));
        VolleyRequestUtil.newInstance().RequestPost(getActivity(), Constant.nnurseTrainDelUrl, "delect",map,
                new VolleyListenerInterface(getActivity(),VolleyListenerInterface.mListener,VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("ArchivesTrainFragment", "submitDelectTrain---result=="+result);
                        Toast.makeText(BaseApplication.getInstance(),"已删除",Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = Constant.DELECT;
                        message.obj = position;
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_delect:
                if(Checkposition != -1){
                    Log.e("ArchivesTrainFragment", "onClick---Checkposition=="+Checkposition);
                    submitDelectTrain(Checkposition);
                }
                break;
            case R.id.ll_add:
                Intent intent = new Intent(getActivity(), AddTrainActivity.class);
                intent.putExtra("type","add");
                Bundle bundle = new Bundle();
                bundle.putSerializable("train",train);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
