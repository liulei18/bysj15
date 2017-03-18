package com.edu.zzti.assm.httphelper;

import android.app.Application;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

/**
 * Http访问的工具类  依赖jar 包为HttpClient.jar
 * 此类提供三个方法 get，post,post带参数的
 * 返回自定义对象MyResponse
 * 并通过MyResponse获取相应的返回值
 * Created by feng on 2015/2/5.
 */
public class HttpHelper {
    /**
     * 定义HttpClient HttpGet HttpPost
     */
    private HttpClient client = null;
    private HttpGet httpGet = null;
    private HttpPost httpPost = null;

    /**
     * 构造方法 初始化HttpClient 向上转型 DefaultHttpClient
     */
    public  HttpHelper(){
        client  = new DefaultHttpClient();
    }

    /**
     * 根据参数确定Http访问的类型 以返回相应的类型： HttpGet or HttpPost
     * @param url 访问服务器的URL 地址
     * @param method 请求的的方法 是 get or  post
     * @param params 根据需求传入参数 List<NameValuePair>
     * @throws Exception
     */
    public void prepareClient(String url,String method,List<NameValuePair> params) throws Exception{
         if(method.equalsIgnoreCase("get")){
            this.httpGet = new HttpGet(url);
         }else if(method.equalsIgnoreCase("post")){
             this.httpPost = new HttpPost(url);
             if(params!=null&&params.size()>0){
                 this.httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
             }
         }

    }

    /**
     * 调用此方法用于获得 HttpGet 请求 并执行get 请求，得到相应的response
     * @param url 访问服务器的URL 地址
     * @return MyResponse 自定义类 类面包装了 HttpResponse
     * @throws Exception
     */
    public MyResponse  get(String url) throws Exception{
        HttpResponse response = null;
        this.prepareClient(url, "get", null);
        response = client.execute(httpGet);
        return new MyResponse(response);

    }

    /**
     * 调用此方法用于获得 HttpPost请求 并执行post 请求，得到相应的response
     * @param url 访问服务器的URL 地址
     * @return MyResponse 自定义类 类面包装了 HttpResponse
     * @throws Exception
     */
    public MyResponse post(String url) throws Exception{
        HttpResponse response = null;
        this.prepareClient(url, "post", null);
        response = client.execute(httpPost);
        return  new  MyResponse(response);
    }

    /**
     * 调用此方法用于获得 HttpPost请求 并执行post 请求，得到相应的response
     * @param url 访问服务器的URL 地址
     * @param params 向服务器端传递参数 List<NameValuePair>
     * @return MyResponse 自定义类 类面包装了 HttpResponse
     * @throws Exception
     */
    public MyResponse post(String url,List<NameValuePair> params) throws Exception{
        HttpResponse response = null;
        this.prepareClient(url, "post", params);

        response = client.execute(httpPost);

        return  new MyResponse(response);
    }

    /**
     * 调用此方法用于获得 HttpPost请求 并执行post 请求，得到相应的response
     * @param url 访问服务器的URL 地址
     * @param
     * @return MyResponse 自定义类 类面包装了 HttpResponse
     * @throws Exception
     */
    public MyResponse post(String url,HttpEntity entity) throws Exception{
        HttpResponse response = null;
        this.httpPost = new HttpPost(url);
        this.httpPost.setEntity(entity);
        response = client.execute(httpPost);
        return  new MyResponse(response);
    }

}
