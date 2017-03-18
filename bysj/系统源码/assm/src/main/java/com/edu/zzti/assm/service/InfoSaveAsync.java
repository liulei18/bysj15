package com.edu.zzti.assm.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.edu.zzti.assm.app.InfoEditActivity;
import com.edu.zzti.assm.app.MyApp;
import com.edu.zzti.assm.app.R;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.task.AsyncTask;
import com.edu.zzti.assm.task.BaseTask;
import com.edu.zzti.assm.util.ViewParams;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by feng on 2015/5/13.
 */
public class InfoSaveAsync extends BaseTask{

    private ProgressDialog progressDialog = null;

    public InfoSaveAsync(final Context context ,final HttpEntity entity,final ViewParams params,final Map<String, String> map,final MyApp myApp,final InfoEditActivity infoEdit) {
        super(params);
        this.asyncTask = new AsyncTask(new AsyncTask.ICallBack() {

            @Override
            public void doBefore() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在保存,请稍候...");
                progressDialog.show();
            }

            @Override
            public void doAsync(Handler handler) {
                HttpHelper helper = new HttpHelper();
                Message msg = null;
                String info = null;
                try {
                    MyResponse response = helper.post(context.getApplicationContext().getString(R.string.ass_info_edit), entity);
                    info = response.asString();

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
                if (msg.arg1 == 1) {
                    String imgurl = context.getApplicationContext().getString(R.string.ass_web_url)+msg.obj.toString().trim();
                    JSONObject jsonObject =(JSONObject) myApp.getObj();
                    try {
                        jsonObject.put("imgurl",imgurl);
                        jsonObject.put("descs",map.get("desc"));
                        jsonObject.put("email",map.get("email"));
                        jsonObject.put("remark",map.get("remark"));
                        infoEdit.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

}
