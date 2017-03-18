package com.edu.zzti.assm.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.loader.AsyncImageLoader;
import com.edu.zzti.assm.loader.ImageCallBackImpl;
import com.edu.zzti.assm.service.InfoSaveAsync;
import com.edu.zzti.assm.util.Tools;
import com.edu.zzti.assm.view.CircleImageView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.apache.http.entity.ContentType;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.HashMap;


public class InfoEditActivity extends  BaseActivity  {
    private EditText mDescTxt;
    private EditText mEmailTxt;
    private EditText mRemarkTxt;
    private ImageView mImgBack;
    private TextView mSave;
    private CircleImageView mPhoto;
    private TextView mInfo;
    private MyApp myApp;
    private boolean isChange = false; //判断头像是否更换
    private Bitmap photoBitmap;

    private String[] items = new String[] { "选择本地图片", "拍照" };
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_info_edit);
        myApp =  (MyApp)getApplication();
    }

    @Override
    public void initCompontent() {
        mDescTxt = this.getById(R.id.ui_desc, EditText.class);
        mDescTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEmailTxt = this.getById(R.id.ui_email, EditText.class);
        mEmailTxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mRemarkTxt = this.getById(R.id.ui_remark, EditText.class);
        mRemarkTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mSave = this.getById(R.id.ui_save,TextView.class);
        mImgBack = this.getById(R.id.ui_back,ImageView.class);
        mPhoto = this.getById(R.id.id_photo,CircleImageView.class);
        mInfo = this.getById(R.id.ui_info_txt,TextView.class);



        String name = "";
        String desc ="";
        String email="";
        String remark ="";
        String imgurl ="";
        try {
            name  =((JSONObject)myApp.getObj()).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mInfo.setText(name+"\n     个性签名：");
        try {
            desc = ((JSONObject)myApp.getObj()).getString("descs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDescTxt.setText(desc);
        try {
            email  =((JSONObject)myApp.getObj()).getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mEmailTxt.setText(email);
        try {
            remark  =((JSONObject)myApp.getObj()).getString("remark");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRemarkTxt.setText(remark);
        try {
            imgurl = ((JSONObject)myApp.getObj()).getString("imgurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!imgurl.equals("")){
            AsyncImageLoader loader  = new AsyncImageLoader();
            Bitmap bitmap =loader.loadAsyncImage(imgurl, new ImageCallBackImpl(mPhoto));
            if(bitmap!=null) {
                mPhoto.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public void initListener() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAlert();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String desc =  mDescTxt.getText().toString().trim();
               String email=  mEmailTxt.getText().toString().trim();
               String remark=    mRemarkTxt.getText().toString().trim();
               String id = "";
               String type ="";
                try {
                    id  =((JSONObject)myApp.getObj()).getString("id");
                    type  =((JSONObject)myApp.getObj()).getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MultipartEntityBuilder builder=  MultipartEntityBuilder.create();
                builder .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.setCharset(Charset.forName(HTTP.UTF_8));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mPhoto.setDrawingCacheEnabled(true);
                Bitmap bitmap = mPhoto.getDrawingCache();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
                mPhoto.setDrawingCacheEnabled(false);

                builder.addBinaryBody("photo", inputStream, ContentType.DEFAULT_BINARY, "photo.png");

                ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
                StringBody sid = new StringBody(id,contentType);
                builder.addPart("id", sid);
                StringBody stype = new StringBody(type,contentType);
                builder.addPart("type", stype);
                StringBody sdesc = new StringBody(desc,contentType);
                builder.addPart("desc", sdesc);
                StringBody semail = new StringBody(email,contentType);
                builder.addPart("email", semail);
                StringBody sremark = new StringBody(remark,contentType);
                builder.addPart("remark",sremark);

                Map<String,String>  map= new HashMap();
                map.put("desc",desc);
                map.put("email",email);
                map.put("remark",remark);
                InfoSaveAsync async =  new InfoSaveAsync(context,builder.build(),params,map,myApp,InfoEditActivity.this);
                async.startTask();

            }
        });

        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intentFromGallery = new Intent();
                                        intentFromGallery.setType("image/*"); // 设置文件类型
                                        intentFromGallery
                                                .setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intentFromGallery,
                                                IMAGE_REQUEST_CODE);
                                        break;
                                    case 1:

                                        Intent intentFromCapture = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        // 判断存储卡是否可以用，可用进行存储
                                        if (Tools.hasSdcard()) {
                                            File root =new  File(Environment.getExternalStorageDirectory() +"/assm/img/");
                                            if(!root.exists()){
                                                root.mkdirs();
                                            }
                                            intentFromCapture.putExtra(
                                                    MediaStore.EXTRA_OUTPUT,
                                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/assm/img/",
                                                            IMAGE_FILE_NAME)));
                                        }

                                        startActivityForResult(intentFromCapture,
                                                CAMERA_REQUEST_CODE);
                                        break;
                                }
                            }
                        })
/*                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })*/.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        File root =new  File(Environment.getExternalStorageDirectory() +"/assm/img/");
                        if(!root.exists()){
                            root.mkdirs();
                        }
                        File tempFile = new File(Environment.getExternalStorageDirectory() +"/assm/img/"
                                + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(InfoEditActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 140);
        intent.putExtra("outputY", 140);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(photo);
//            mPhoto.setImageDrawable(drawable);
            mPhoto.setImageBitmap(photo);
            isChange = true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            backAlert();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("修改中，确定退出？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InfoEditActivity.this.finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .create();
        builder.show();
    }
}
