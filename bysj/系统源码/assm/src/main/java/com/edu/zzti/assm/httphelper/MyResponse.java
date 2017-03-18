package com.edu.zzti.assm.httphelper;


import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义MyResponse 内部封装了 HttpResponse
 * 可以返回HttpResponse 的各种需求值
 * Created by feng on 2015/2/5.
 */
public class MyResponse {
    private HttpResponse response = null;

    /**
     * 构造函数 初始化HttpResponse
     * @param response
     */
    public MyResponse(HttpResponse response) {
        this.response = response;
    }

    /**
     * 返回HttpResponse的内的HttpEntity对象
     * @return HttpEntity
     */
    public HttpEntity asEntity() {
        if (response.getStatusLine().getStatusCode() == 200) {
            return response.getEntity();
        }
        return null;
    }

    /**
     *  返回HttpResponse的内的HttpEntity对象的字符串形式
     *  @return String 服务器返回的字符串内容
     */
    public String asString() {
        if (response.getStatusLine().getStatusCode() == 200) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 得到HttpResponse的HttpEntity对象的InputStream形式
     * @return InputStream
     * @throws Exception
     */
    public InputStream asStream() throws Exception{
        if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity().getContent();
        }
        return null;
    }

    /**
     * 把HttpResponse返回的信息 以JSONObject的形式进行返回
     * @return JSONObject
     */
    public JSONObject asJSONObjetc(){
        String content = this.asString();
        if(!"".equals(content)){
            try {
                return new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 把HttpResponse返回的信息 以JSONArray的形式进行返回
     * @return JSONArray
     */
    public JSONArray asJSONArray(){
        String content = this.asString();
        if(!"".equals(content)){
            try {
                return new JSONArray(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
