package com.edu.zzti.assm.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.zzti.assm.core.BaseActivity;
import com.edu.zzti.assm.core.MySharedPreferences;
import com.edu.zzti.assm.loader.AsyncImageLoader;
import com.edu.zzti.assm.loader.ImageCallBackImpl;
import com.edu.zzti.assm.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

public class InfoActivity extends BaseActivity {
    private ImageView mImageView;
    private TextView mInfoEdit;
    private TextView mInfoTxt;
    private EditText mEmailEdit;
    private EditText mRemarkEdit;
    private Button mExitBtn;
    private CircleImageView mPhoto;
    private Context context = this;
    private MyApp myApp;

    @Override
    public void initLayout() {
        setContentView(R.layout.ui_info);
        myApp = (MyApp) getApplication();
    }

    @Override
    public void initCompontent() {
        mImageView = this.getById(R.id.ui_back,ImageView.class);
        mInfoEdit =  this.getById(R.id.ui_info_edit,TextView.class);
        mInfoTxt =  this.getById(R.id.ui_info_txt,TextView.class);
        mEmailEdit =  this.getById(R.id.ui_email,EditText.class);
        mRemarkEdit =  this.getById(R.id.ui_remark,EditText.class);
        mRemarkEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mExitBtn = this.getById(R.id.ui_exit_btn,Button.class);
        mPhoto = this.getById(R.id.id_photo,CircleImageView.class);

//        initView();

    }

    @Override
    public void initListener() {

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.this.finish();
            }
        });

        mInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent = new Intent(context, InfoEditActivity.class);
                context.startActivity(intent);

            }
        });

        mExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreferences    mspf = new MySharedPreferences(context);
                Map<String,String > map  = new HashMap<>();
                map.put("userPwd", "");
                mspf.saveMessage(map);
                boolean isSave = mspf.saveMessage(map);
                Log.i("TAG", "-->" + isSave);
                Intent  intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                InfoActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }

    private void initView(){
        String name = "";
        String desc ="";
        String email="";
        String remark ="";
        String imgurl ="";
        try {
            name  =((JSONObject)myApp.getObj()).getString("name");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        try {
            desc = ((JSONObject)myApp.getObj()).getString("descs");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        mInfoTxt.setText(name+"\n     个性签名：\n"+desc);
        try {
            email  =((JSONObject)myApp.getObj()).getString("email");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        mEmailEdit.setText(email);
        try {
            remark  =((JSONObject)myApp.getObj()).getString("remark");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        mRemarkEdit.setText(remark);
        try {
            imgurl = ((JSONObject)myApp.getObj()).getString("imgurl");
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        if(!imgurl.equals("")){
            AsyncImageLoader loader  = new AsyncImageLoader();
            Bitmap bitmap =loader.loadAsyncImage(imgurl, new ImageCallBackImpl(mPhoto));
            if(bitmap!=null) {
                mPhoto.setImageBitmap(bitmap);
            }
        }
    }

}
