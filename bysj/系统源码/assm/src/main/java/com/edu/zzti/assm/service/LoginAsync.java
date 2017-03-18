package com.edu.zzti.assm.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.edu.zzti.assm.app.LoginActivity;
import com.edu.zzti.assm.app.MainActivity;
import com.edu.zzti.assm.app.MainTeaActivity;
import com.edu.zzti.assm.app.MyApp;
import com.edu.zzti.assm.app.R;
import com.edu.zzti.assm.core.MySharedPreferences;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.task.AsyncTask;
import com.edu.zzti.assm.task.BaseTask;
import com.edu.zzti.assm.util.ViewParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by feng on 2015/4/27.
 */
public class LoginAsync extends BaseTask {

    private ProgressDialog progressDialog = null;

    public LoginAsync(final Context context, final String userName, final String userPwd, final String type,
                      ViewParams params, final MyApp myapp, final LoginActivity login, final boolean flag) {
        super(params);

        this.asyncTask = new AsyncTask(new AsyncTask.ICallBack() {
            @Override
            public void doBefore() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在登录,请稍候...");
                progressDialog.show();
            }

            @Override
            public void doAsync(Handler handler) {
                HttpHelper helper = new HttpHelper();
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("userName", userName));
                list.add(new BasicNameValuePair("userPwd", userPwd));
                list.add(new BasicNameValuePair("type", type));
                Message msg = null;
                String info = null;
                try {
                    MyResponse response = helper.post(context.getApplicationContext().getString(R.string.ass_login_url), list);
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
                    if ("no".equals(msg.obj.toString())) {
                        Toast.makeText(context, "帐号或者密码错误...", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject = null;
                        if (type.equals("student")) {
                            try {
                                jsonObject = new JSONObject(msg.obj.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (type.equals("teacher")) {
                            try {
                                jsonObject = new JSONObject(msg.obj.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            String imgurl= context.getApplicationContext().getString(R.string.ass_web_url)+jsonObject.get("imgurl");
                            if(!imgurl.trim().equals(context.getApplicationContext().getString(R.string.ass_web_url))) {
                                jsonObject.put("imgurl", imgurl);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myapp.setObj(jsonObject);
                        Intent intent = null;
                        String roleType = "";
                        try {
                            roleType = jsonObject.get("type").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (roleType.equals("student")) {
                            intent = new Intent(context, MainActivity.class);
                        } else {
                            intent = new Intent(context, MainTeaActivity.class);
                        }
                        if (flag == true) {
                            MySharedPreferences mspf = new MySharedPreferences(context);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("userName", userName);
                            map.put("userPwd", userPwd);
                            map.put("type", type);
                            boolean isSave = mspf.saveMessage(map);
                            Log.i("TAG","-->"+isSave);
                        }

                        context.startActivity(intent);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                login.finish();
                            }
                        }, 1000);

                    }
                } else {
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
