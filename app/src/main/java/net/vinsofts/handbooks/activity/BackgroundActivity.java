package net.vinsofts.handbooks.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.fragment.StillFragment;
import net.vinsofts.handbooks.fragment.YourOwnFragment;


/**
 * Created by Administrator on 22/04/2016.
 */
public class BackgroundActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rbtStill, rbtYour;
    private FragmentManager manager;
    private ImageButton btnBackBackground;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        rbtStill = (RadioButton) findViewById(R.id.btnStill);
        rbtYour = (RadioButton) findViewById(R.id.btnYourOwn);
        btnBackBackground = (ImageButton) findViewById(R.id.btnBackBackground);

        btnBackBackground.setOnClickListener(this);
        rbtStill.setOnClickListener(this);
        rbtYour.setOnClickListener(this);


        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.input);
        if (fragment == null) {
            fragment = new StillFragment();
            manager.beginTransaction().add(R.id.input,fragment).commit();
        }

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.btnStill:
                fragment = new StillFragment();
                transaction.replace(R.id.input, fragment);
                transaction.commit();
                break;
            case R.id.btnYourOwn:
                fragment = new YourOwnFragment();
                transaction.replace(R.id.input, fragment);
                transaction.commit();
                break;
            case R.id.btnBackBackground:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
