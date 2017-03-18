package com.edu.zzti.assm.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.core.MySharedPreferences;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_TIME = 1500; // 延迟1秒
    private Context context = this;
    private MySharedPreferences mspf;
    private MyApp myApp;
    private Intent intent = null;

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_splash);

    }

    @Override
    public void initCompontent() {
        myApp = (MyApp) getApplication();
        List<String> keys = new ArrayList<>();
        keys.add("userName");
        keys.add("userPwd");
        keys.add("type");
        mspf = new MySharedPreferences(context);
        Map<String, String> map = mspf.getMessage(keys);
        if (!map.get("info").equals("no")) {
            final String userName = map.get("userName");
            final String userPwd = map.get("userPwd");
            final String type = map.get("type");
            new Thread() {
                @Override
                public void run() {

                    HttpHelper helper = new HttpHelper();
                    List<NameValuePair> list = new ArrayList<>();
                    list.add(new BasicNameValuePair("userName", userName));
                    list.add(new BasicNameValuePair("userPwd", userPwd));
                    list.add(new BasicNameValuePair("type", type));
                    String info = null;
                    try {
                        MyResponse response = helper.post(context.getApplicationContext().getString(R.string.ass_login_url), list);
                        info = response.asString();
                        if ("no".equals(info)) {

                            Intent intent = new Intent(context, LoginActivity.class);
                            Thread.sleep(1500);
                            context.startActivity(intent);
                            SplashActivity.this.finish();
                        } else {
                            JSONObject jsonObject = null;
                            if (type.equals("student")) {
                                try {
                                    jsonObject = new JSONObject(info);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (type.equals("teacher")) {
                                try {
                                    jsonObject = new JSONObject(info);
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
                            myApp.setObj(jsonObject);

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
                            Thread.sleep(1500);
                            context.startActivity(intent);
                            SplashActivity.this.finish();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_TIME);

        }

    }

    @Override
    public void initListener() {


    }


}
