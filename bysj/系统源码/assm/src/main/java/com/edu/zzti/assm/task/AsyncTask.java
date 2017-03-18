package com.edu.zzti.assm.task;

import android.os.Handler;
import android.os.Message;

/**
 * 异步线程任务处理类
 * 最新版本的 SDK 要求 Http访问必须放在子线程中，若放在主线程则程序立刻停止
 * Created by feng on 2015/2/5.
 */
public class AsyncTask  {
    /**
     * 构造函数用于进行异步任务的处理；
     * 初始化ICallBack的操作；
     * 创造实例对象时记得调用start()方法 创建线程去处理你的任务。
     * @param callBack  ICallBack 定义的的一组要是实现的规范操作
     */
    public AsyncTask(ICallBack callBack){
        this.callBack = callBack;
    }

    private ICallBack callBack = null;
    /**
     * 定义一个Handler用于对主线程的的UI 界面进行处理
     */
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //调用接口实现类重写的success方法对UI 主线程进行处理
           callBack.success(msg);
        }
    };

    /**
     * 此方法用于创建线程操作
     */
    public void start(){
        this.callBack.doBefore();
        new Thread(){
            @Override
            public void run() {
                callBack.doAsync(myHandler);
            }
        }.start();

    }

    /**
     * 此接口定义的一组 处理异步任务的流程
     */
    public interface  ICallBack{
        /**
         * 预备工作，可以对用户进行提示
         */
        public void doBefore();

        /**
         * 异步任务的主要操作
         * @param handler 用于处理自定义的Message的信息
         */
        public void doAsync(Handler handler);

        /**
         * 用于最后操作 得到异步任务的 处理信息;
         * 自定义处理方式
         * @param msg Message 异步任务完成后的到的Message 信息
         */
        public void success(Message msg);

    }
}
