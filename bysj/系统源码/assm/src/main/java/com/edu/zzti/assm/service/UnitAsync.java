package com.edu.zzti.assm.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.edu.zzti.assm.adapter.UnitListAdapter;
import com.edu.zzti.assm.app.R;
import com.edu.zzti.assm.httphelper.HttpHelper;
import com.edu.zzti.assm.httphelper.MyResponse;
import com.edu.zzti.assm.model.Chapter;
import com.edu.zzti.assm.model.Unit;
import com.edu.zzti.assm.task.AsyncTask;
import com.edu.zzti.assm.task.BaseTask;
import com.edu.zzti.assm.util.ViewParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by feng on 2015/5/15.
 */
public class UnitAsync extends BaseTask {

    private ProgressDialog progressDialog = null;
    public UnitAsync(final Context context, final ViewParams params) {
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
                Message msg = null;
                JSONArray info = null;
                try {
                    MyResponse response = helper.get(context.getApplicationContext().getString(R.string.ass_get_units));
                    info = response.asJSONArray();

                    msg = new Message();
                    if (info != null) {
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
                    List<Unit> units = new ArrayList<>();
                    JSONArray jsonArray = (JSONArray) msg.obj;
                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Unit unit = new Unit();
                            unit.setId((Integer) obj.get("id"));
                            unit.setUnitName((String) obj.get("unitName"));
                            JSONArray array = (JSONArray)obj.get("chapters");
                            Set<Chapter> chapters = new HashSet<Chapter>();
                            for(int j = 0 ;j<array.length();j++){
                                JSONObject obj1 = array.getJSONObject(j);
                                Chapter chapter = new Chapter();
                                chapter.setId((Integer) obj1.get("id"));
                                chapter.setChaName((String) obj1.get("chaName"));
                                chapters.add(chapter);
                            }
                            unit.setChapters(chapters);
                            units.add(unit);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    UnitListAdapter adapter = new UnitListAdapter(context, units);
                    params.getListViewMap().get("mListView").setAdapter(adapter);

                } else {
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
