package com.edu.zzti.assm.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.edu.zzti.assm.app.AddPraActivity;
import com.edu.zzti.assm.app.R;
import com.edu.zzti.assm.app.WebViewActivity;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.task.AsyncTask;
import com.edu.zzti.assm.task.BaseTask;
import com.edu.zzti.assm.util.ViewParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2015/5/17.
 */
public class PraAddAsync extends BaseTask {

    private ProgressDialog progressDialog = null;
    public PraAddAsync(final Context context, final Map map, final ViewParams params) {
        super(params);
        asyncTask = new AsyncTask(new AsyncTask.ICallBack() {
            @Override
            public void doBefore() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在加载,请稍候...");
                progressDialog.show();
            }

            @Override
            public void doAsync(Handler handler) {
                HttpHelper helper = new HttpHelper();
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("praName", (String) map.get("praName")));
                list.add(new BasicNameValuePair("praDx", map.get("praDx").toString()));
                list.add(new BasicNameValuePair("praPd", map.get("praPd").toString()));
                list.add(new BasicNameValuePair("praJd", map.get("praJd").toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String praCutTime = sdf.format((Date)(map.get("praCutTime")));
                list.add(new BasicNameValuePair("praCutTime", praCutTime));
                list.add(new BasicNameValuePair("teacherId", (String) map.get("teacherId")));
                list.add(new BasicNameValuePair("unitId",(String) map.get("unitId")) );
                list.add(new BasicNameValuePair("chapterIds",(String) map.get("chapterIds") ));
                Message msg = null;
                String info = null;
                try {
                    MyResponse response = helper.post(context.getApplicationContext().getString(R.string.ass_pra_add), list);
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
                String info =msg.obj.toString();
                if (msg.arg1 == 1) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("intentType",0);
                    intent.putExtra("paperid",info);
                    context.startActivity(intent);

                }else{
                   Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
