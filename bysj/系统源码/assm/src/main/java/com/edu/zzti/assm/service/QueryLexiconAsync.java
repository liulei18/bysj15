package com.edu.zzti.assm.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.edu.zzti.assm.app.R;
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

/**
 * Created by feng on 2015/5/2.
 */
public class QueryLexiconAsync extends BaseTask {
    private ProgressDialog progressDialog = null;

    public QueryLexiconAsync(final Context context,final String text,final ViewParams params) {
        super(params);

        this.asyncTask =new AsyncTask(new AsyncTask.ICallBack() {
            @Override
            public void doBefore() {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("正在查询,请稍候...");
                progressDialog.show();
            }

            @Override
            public void doAsync(Handler handler) {
                HttpHelper helper = new HttpHelper();
                List<NameValuePair> list = new ArrayList<>();
                list.add(new BasicNameValuePair("word", text));
                list.add(new BasicNameValuePair("type", "app"));
                Message msg = null;
                String info = null;
                try {
                    MyResponse response = helper.post(context.getApplicationContext().getString(R.string.ass_query_lexicon_url), list);
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
                    if(info.equals("no")){
//                        Toast.makeText(context, "抱歉，词条尚未录入...", Toast.LENGTH_SHORT).show();
                        params.getTextViewMap().get("result").setText("抱歉，词条尚未录入...");
                    }else{
                        try {
                            JSONObject jsonObject = new JSONObject(info);
                            String result =jsonObject.get("word").toString()+" : \n\n" +
                                    "   中文释义："+jsonObject.get("zhMean").toString()+"\n\n"+
                                    "   English："+jsonObject.get("enMean").toString();

                            params.getTextViewMap().get("result").setText(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    Toast.makeText(context,  msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
