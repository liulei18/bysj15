package com.edu.zzti.assm.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.zzti.assm.adapter.PracticeListAdapter;
import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.model.Unit;
import com.edu.zzti.assm.task.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PracticeListActivity extends BaseActivity {
    private ImageView mBack;
    private ListView mListView;
    private Context context = this;
    private TextView centerTxt;
    private MyApp myApp;
    private Integer intentType;
    private Unit unit;
    private String id;
    private String type;
    private ProgressDialog progressDialog = null;
    private List<JSONObject> list = new ArrayList<>();

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_practice_list);
        myApp = (MyApp) getApplication();
        intentType = getIntent().getIntExtra("intentType", 1);
        unit = myApp.getUnit();
    }

    @Override
    public void initCompontent() {
        centerTxt = this.getById(R.id.center_text, TextView.class);
        centerTxt.setText(getIntent().getStringExtra("unit"));
        mBack = this.getById(R.id.ui_back, ImageView.class);
        mListView = this.getById(R.id.ui_practice_list, ListView.class);
        JSONObject object = (JSONObject) myApp.getObj();
        id = "";
        try {
            id = ((JSONObject) myApp.getObj()).getString("id");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        type = "";
        try {
            type = ((JSONObject) myApp.getObj()).getString("type");
        } catch (JSONException e) {
//            e.printStackTrace();
        }


            new AsyncTask(new AsyncTask.ICallBack() {
                @Override
                public void doBefore() {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("正在加载,请稍候...");
                    progressDialog.show();
                }

                @Override
                public void doAsync(Handler handler) {
                    HttpHelper helper = new HttpHelper();
                    String info = null;
                    Message msg = null;

                    try {
                        if (intentType == 1 ||intentType == 4) {
                            MyResponse response = helper.get(context.getApplicationContext().getString(R.string.ass_pra_list) + "/" + id + "/" + type + "/" + unit.getId());
                            info = response.asString();
                        }else if(intentType == 3){
                            MyResponse response =  helper.get(context.getApplicationContext().getString(R.string.ass_stu_pra_list)+ "/" + id +  "/" + unit.getId());
                            info = response.asString();
                        }

                        msg = new Message();
                        if (!"".equals(info)) {
                            msg.obj = info;
                            msg.arg1 = 1;
                        } else {
                            msg.obj = "服务器连接出现问题,请稍候再试...";
                            msg.arg1 = 0;
                        }
                    } catch (Exception e) {
                        msg = new Message();
                        msg.obj = "应用程序出错了,请稍候再试...";
                        msg.arg1 = 0;
                        e.printStackTrace();
                    }
                    handler.sendMessage(msg);
                }

                @Override
                public void success(Message msg) {
                    progressDialog.dismiss();
                    String info = msg.obj.toString();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(info);

                        int size = jsonArray.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            list.add(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PracticeListAdapter adapter = new PracticeListAdapter(context, list);
                    mListView.setAdapter(adapter);
                }
            }).start();




    }

    @Override
    public void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(intentType==1){
                    JSONObject jsonObject =(JSONObject) mListView.getItemAtPosition(position);
                    Intent intent = new Intent(context,WebViewActivity.class);
                    intent.putExtra("type",type);
                    try {
                        intent.putExtra("paperid",jsonObject.getString("paperid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("intentType",intentType);
                    context.startActivity(intent);
//                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeListActivity.this.finish();
            }
        });
    }
}
