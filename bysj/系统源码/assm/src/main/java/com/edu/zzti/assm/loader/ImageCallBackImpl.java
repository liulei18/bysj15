package com.edu.zzti.assm.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * AsyncImageLoader.IImageCallBack的实现类
 * 用于对UI线程的界面进行处理 设置ImageView的值
 * Created by feng on 2015/2/6.
 */
public class ImageCallBackImpl implements AsyncImageLoader.IImageCallBack {

    private ImageView image = null;

    /**
     * 构造函数 存放如UI线程中的ImageView
     *
     * @param image
     */
    public ImageCallBackImpl(ImageView image) {
        this.image = image;

    }

    /**
     * 设置UI线程中ImageView的值
     *
     * @param bitmap
     */
    @Override
    public void imageLoaded(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
    }
}
