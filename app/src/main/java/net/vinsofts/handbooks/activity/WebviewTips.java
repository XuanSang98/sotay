package net.vinsofts.handbooks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.common.Constant;

/**
 * Created by Administrator on 25/04/2016.
 */
public class WebviewTips extends AppCompatActivity {
    private WebView wbTips;
    private ProgressBar progressWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_tips);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        Intent intent = getIntent();

        wbTips = (WebView) findViewById(R.id.wb_stips);
        progressWebview = (ProgressBar) findViewById(R.id.progressWebview);
        wbTips.setWebViewClient(new myWebViewClient());
        if (intent.getStringExtra(Constant.PASS_URL) == null) {
            wbTips.loadUrl(Constant.BASE_URL);
        } else {
            wbTips.loadUrl(Constant.BASE_URL + intent.getStringExtra(Constant.PASS_URL));
        }
        wbTips.getSettings().setJavaScriptEnabled(true);

    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressWebview.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            wbTips.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wbTips.canGoBack()) {
            //if Back key pressed and webview can navigate to previous page
            wbTips.goBack();
            // go back to previous page
            return true;
        }
        else
        {
            finish();
            // finish the activity
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
