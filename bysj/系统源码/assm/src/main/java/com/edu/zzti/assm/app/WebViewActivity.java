package com.edu.zzti.assm.app;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.edu.zzti.assm.core.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class WebViewActivity extends BaseActivity {
    private WebView mWebView;
    private ImageView mBack;
    private  String paperid;
    private int intentType;
    private String type; //身份
    private MyApp myApp;
    @Override
    public void initLayout() {
        setContentView(R.layout.ui_web_view);
        paperid  =  getIntent().getStringExtra("paperid");
        intentType = getIntent().getIntExtra("intentType",0);
        myApp = (MyApp)getApplication();
    }

    @Override
    public void initCompontent() {
        mBack = this.getById(R.id.ui_back,ImageView.class);
        mWebView = this.getById(R.id.ui_web,WebView.class);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setHorizontalScrollbarOverlay(false);
        //加载需要显示的网页
        if(intentType==0) {
            mWebView.loadUrl(getApplicationContext().getString(R.string.ass_pre_pra) + "/" + paperid);
        }else if(intentType==1){
            JSONObject jsonObject = (JSONObject)myApp.getObj();
            String id = "";
            try {
                id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String type ="";
            try {
                type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mWebView.loadUrl(getApplicationContext().getString(R.string.ass_into_pra) + "/" + paperid+"/"+type+"/"+id);
        }else if(intentType==3){
            mWebView.loadUrl(getApplicationContext().getString(R.string.ass_into_answer) + "/" + paperid);

        }else if(intentType==4){
            JSONObject jsonObject = (JSONObject)myApp.getObj();
            String id = "";
            try {
                id = jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mWebView.loadUrl(getApplicationContext().getString(R.string.ass_into_info) + "/" + paperid+"/"+id+"/-999");
        }


        //设置Web视图
        mWebView.setWebViewClient(new ASSWebViewClient());
    }

    @Override
    public void initListener() {


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.destroy();
                WebViewActivity.this.finish();
            }
        });
    }


    //Web视图
    private class ASSWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

}
