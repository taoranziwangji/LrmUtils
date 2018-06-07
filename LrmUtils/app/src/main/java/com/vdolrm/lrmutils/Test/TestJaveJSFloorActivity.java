package com.vdolrm.lrmutils.Test;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vdolrm.lrmutils.BaseFloorActivity;
import com.vdolrm.lrmutils.R;

/**
 * Created by vdo on 16/11/16.
 * 参考：
 * http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237720&idx=1&sn=e2b27592eee630ab35f4e921bf2fa766&chksm=88639a77bf141361aa23e90f930652a2ed013e999a96f6b7f10ef1e86ed8f0f37024fde28b18&mpshare=1&scene=1&srcid=1116ZTXoTujsTSq8eiz0V7nY#rd
 */

public class TestJaveJSFloorActivity extends BaseFloorActivity {
    private WebView webView;
    private LinearLayout ll_root;
    private EditText ed_user;

    @Override
    public void initView() {
        setContentView(R.layout.activity_test_java_js);
    }

    @Override
    public void init() {
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        ed_user = (EditText) findViewById(R.id.edt_user);
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userJs();
            }
        });
        initWebView();

    }

    private void initWebView(){
        webView = new WebView(getApplication());//new出来而不是在xml中加载，好处时可以释放，防止内存溢出；同样适用getApplication()也是这个意思
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(lp);
        ll_root.addView(webView);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
        webView.addJavascriptInterface(new JSInterface(),"Android");

    }


    /**
     * android调用JS
     */
    public void userJs(){
        webView.loadUrl("javascript:javaCallJs("
                +"'"+ed_user.getText().toString()+"'"+")");

    }



    @Override
    public void initEvent() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ll_root.removeView(webView);
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }


    private class JSInterface{
        @JavascriptInterface
        public void showToastByJs(String arg){
            Toast.makeText(TestJaveJSFloorActivity.this, arg, Toast.LENGTH_SHORT).show();
        }
    }
}
