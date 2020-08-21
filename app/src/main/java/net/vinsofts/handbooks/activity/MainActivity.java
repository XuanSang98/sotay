package net.vinsofts.handbooks.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.stetho.Stetho;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.fragment.ContentFragment;
import net.vinsofts.handbooks.fragment.FavoriteFragment;
import net.vinsofts.handbooks.fragment.HomeFragment;
import net.vinsofts.handbooks.fragment.SettingFragment;
import net.vinsofts.handbooks.fragment.TipsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnHome, btnContent, btnFavorite, btnTips, btnSetting;
    private LinearLayout demo;
    private SharedPreferences sharedPref;
    private FragmentManager frManager;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        initView();

        frManager = getSupportFragmentManager();
        Fragment fragment = frManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new ContentFragment();
            frManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        Stetho.initializeWithDefaults(this);
    }



    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        if (v == findViewById(R.id.btnHome)) {
            fragment = new HomeFragment();
        } else if (v == findViewById(R.id.btnContent)) {
            fragment = new ContentFragment();
        } else if (v == findViewById(R.id.btnFavorite)) {
            fragment = new FavoriteFragment();
        } else if (v == findViewById(R.id.btnTips)) {
            fragment = new TipsFragment();
        } else if (v == findViewById(R.id.btnSetting)) {
            fragment = new SettingFragment();
        }


        FragmentTransaction transaction = frManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void initView() {
        btnHome = (Button) findViewById(R.id.btnHome);
        btnContent = (Button) findViewById(R.id.btnContent);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        btnTips = (Button) findViewById(R.id.btnTips);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        demo = (LinearLayout) findViewById(R.id.demo);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);

        btnHome.setOnClickListener(this);
        btnContent.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnTips.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        sharedPref = getSharedPreferences("background",
                Context.MODE_PRIVATE);
        int dem = sharedPref.getInt("check", -1);
        Log.d("Chien",dem+"");
        if (dem == 0) {
            changeBackgroundStill();
        } else if (dem == 1) {
            changebackgroundYourOwn();
        }
    }


    public void changeBackgroundStill() {
//        sharedPref = this.getSharedPreferences("still",
//                Context.MODE_PRIVATE);
        int bg = sharedPref
                .getInt("background_resource", android.R.color.white);
        demo.setBackgroundResource(bg);
    }

    public void changebackgroundYourOwn() {
//        sharedPref = getSharedPreferences("my_prefs",
//                Context.MODE_PRIVATE);
        String fondo = sharedPref.getString("background_path", "vacio");
        Log.d("Chien_path2",fondo);
        if (!fondo.equals("vacio")) {
//            byte[] b = Base64.decode(fondo, Base64.DEFAULT);
//            InputStream is = new ByteArrayInputStream(b);
//            Bitmap yourSelectedImage = BitmapFactory.decodeStream(is);
//            BitmapDrawable bd = new BitmapDrawable(getResources(), yourSelectedImage);
//            demo.setBackgroundDrawable(bd);

            Bitmap bitmap = BitmapFactory.decodeFile(fondo);
            BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
            demo.setBackgroundDrawable(bd);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
}
