package com.nepalese.virgolib.mainbody.activity.oricom;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WebviewActivity extends BaseActivity {
    private static final String TAG = "WebviewActivity";

    private WebView webView;
    private AutoCompleteTextView input;
    private ImageButton ibSearch;
    private String curUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }

    @Override
    protected void initUI() {
        webView = findViewById(R.id.webview);
        input = findViewById(R.id.atv_input_wv);
        ibSearch = findViewById(R.id.ib_search_wv);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curUrl = input.getText().toString().trim().toLowerCase();
                if(curUrl.equals("")){
                    return;
                }

                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new MyChromeClient());
                webView.setWebViewClient(new MyViewClient());
                webView.loadUrl(curUrl);
            }
        });
    }

    @Override
    protected void release() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    @Override
    protected void onBack() {
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            finish();
        }
    }

    public void onBackLeftWv(View view) {

    }

    public void onBackRightWv(View view) {

    }

    public void onHomeWv(View view) {

    }

    public void onFreshWv(View view) {
    }

    static class MyChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i(TAG, "onReceivedTitle: " + title);
        }
    }

    //load only in local page
    class MyViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.evaluateJavascript("javascript;alert('Hello')",null);
        }
    }
}