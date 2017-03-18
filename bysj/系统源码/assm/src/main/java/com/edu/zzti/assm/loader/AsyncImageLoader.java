package com.edu.zzti.assm.loader;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.edu.zzti.assm.util.Encrypt;


/**
 * 图片的异步加载类 ,加载的同时设置一级和二级缓存
 * 一级缓存： 在内存中的数据 容易被GC清理
 * 二级缓存： 在内存卡中进行存储
 * Created by feng on 2015/2/6.
 */
public class AsyncImageLoader {

    /**
     * 定义一级缓存 使用 SoftReference<T>的缓存图片到内存中
     */
    private Map<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 此类的主要方法 用来异步加载图片
     * 若图片已缓存则返回Bitmap 否则返回null
     * 需要自己处理子线程的任务 即从网络下载的图片 需要在callBack中进行处理
     *
     * @param url
     * @param callBack
     * @return
     */
    public Bitmap loadAsyncImage(final String url, final IImageCallBack callBack) {
        //判断一级缓存 即内存中是否存在
        if (map.containsKey(url)) {
            SoftReference<Bitmap> softReference = map.get(url);
            if (softReference != null) {
                return softReference.get();
            }
        }
        //判断二级缓存即 内存卡中是否存在
        if (this.findInFile(url) != null) {
            return this.findInFile(url);
        }
        //Handler 用于处理子线程剩余的逻辑，通过它来更改UI线程的视图
        final Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                callBack.imageLoaded((Bitmap) msg.obj);
            }
        };
        //开启一个新的线程来处理图片的下载
        new Thread() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = loadImageFromURL(url);
                    //定义一个一级缓存对象
                    SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
                    //把缓存对象放入map中
                    map.put(url, softReference);
                    try {
                        //把图片存入SD卡
                        writeToSDCard(bitmap, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = bitmap;
                    myHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return null;

    }

    /**
     * 根据网络url地址来获取Bitmap
     *
     * @param url url 地址
     * @return Bitmap
     */
    private Bitmap loadImageFromURL(String url) {

        try {
            //创建并返回一个Bitmap
            return BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据URL 地址查询内存卡缓存是否有 已缓存的图片 有的话返回，没有返回null
     *
     * @param url 图片的URL地址
     * @return Bitmap
     */
    private Bitmap findInFile(String url) {
        //根据文件url来 得到文件的在SD卡的路径
        String root = Environment.getExternalStorageDirectory() + "/assm/cache/" + Encrypt.md5(url);
        File image = new File(root);
        //判断文件是否存在
        if (image.exists()) {
            //若存在则返回Bitmap
            return BitmapFactory.decodeFile(root);
        }
        return null;
    }

    /**
     * 传入位图和url 把位图存入内存卡/feng/cache目录下 文件名为url地址经过md5加密后的的字符串
     *
     * @param bitmap 网络上下载的图片
     * @param url    图片的url地址
     * @throws Exception
     */
    private void writeToSDCard(Bitmap bitmap, String url) throws Exception {
        //获得缓存图片资源的目录
        String root = Environment.getExternalStorageDirectory() + "/assm/cache/";
        File rootFile = new File(root);
        //判断目录是否存在，不存在则创建
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        //根据目录和文件的URL的md5加密字段来创建文件 存放内存卡
        File image = new File(root + Encrypt.md5(url));
        FileOutputStream outputStream = null;
        try {
            //根据文件拿到输出流
            outputStream = new FileOutputStream(image);
            //图片进压缩存储 需设置 存储格式，压缩百分比，文件的输出流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public interface IImageCallBack {
        /**
         * 用于回调 对UI线程界面进行处理
         *
         * @param bitmap
         */
        public void imageLoaded(Bitmap bitmap);

    }

}

