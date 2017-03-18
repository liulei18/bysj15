package com.edu.zzti.assm.core;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by feng on 2015/5/1.
 */
public class MySharedPreferences {
    private Context context;
    public MySharedPreferences( Context context) {
        this.context = context;
    }

    public boolean saveMessage(Map<String ,String> map){
        boolean flag = false;
        SharedPreferences spf = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =spf.edit();
        for(String key: map.keySet() ){
            String value =  map.get(key);
            editor.putString(key,value);
        }
        flag = editor.commit();
        return  flag;
    }

    public Map<String,String>  getMessage(List<String> keys){
        Map<String ,String> map   = new HashMap<String,String>();

            SharedPreferences spf = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
            map.put("info","yes");
            for(String key :keys){
                String  value =  spf.getString(key,"");
                if(value.equals("")){
                    map.put("info","no");
                }
                map.put(key,value);
            }


        return  map;
    }



}
