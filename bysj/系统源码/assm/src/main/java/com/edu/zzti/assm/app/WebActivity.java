package com.edu.zzti.assm.app;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.edu.zzti.assm.core.BaseActivity;


public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private ImageView mBack;
    @Override
    public void initLayout() {
        setContentView(R.layout.ui_web);
    }

    @Override
    public void initCompontent() {
        mBack = this.getById(R.id.ui_back,ImageView.class);
        mWebView = this.getById(R.id.ui_web,WebView.class);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //禁用横向滚动条
        //加载需要显示的网页
        mWebView.loadUrl(getApplicationContext().getString(R.string.ass_web_url));
        //设置Web视图
        mWebView.setWebViewClient(new ASSWebViewClient());
    }

    @Override
    public void initListener() {


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.destroy();
                WebActivity.this.finish();
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
