package net.vinsofts.handbooks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.common.Constant;

/**
 * Created by HongChien on 29/04/2016.
 */
public class PrimacyActivity extends AppCompatActivity {
    private WebView wvPrimacy;
    private ImageView imgBackPrivacy;
    private TextView tvHeaderWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primacy_layout);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        wvPrimacy = (WebView) findViewById(R.id.wvPrivacy);
        imgBackPrivacy = (ImageView) findViewById(R.id.imgBackPrivacy);
        tvHeaderWebview = (TextView) findViewById(R.id.tvHeaderWebview);
        imgBackPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();

        if (intent.getStringExtra("LinkContent") == null) {
            //Click vào primacy
            tvHeaderWebview.setText(getString(R.string.privacy));
            wvPrimacy.loadUrl(Constant.PATH_ASSEST + "II_bai1_quanhequocte.html");
            wvPrimacy.getSettings().setJavaScriptEnabled(false);
        } else {
            //Click vào item favorite
            tvHeaderWebview.setText(intent.getStringExtra("TenHeader"));
            wvPrimacy.loadUrl(Constant.PATH_ASSEST + intent.getStringExtra("LinkContent"));
            wvPrimacy.getSettings().setJavaScriptEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
