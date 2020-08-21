package net.vinsofts.handbooks.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.common.ObservableWebView;

/**
 * Created by HongChien on 22/04/2016.
 */
public class PageDetailFragment extends Fragment implements View.OnTouchListener, ObservableWebView.OnScrollChangedCallback{
    private ObservableWebView wvContent;
    private Bundle bundle;
    private RelativeLayout relativeDetail, layout_menu_top, layout_menu_bottom;
    private GestureDetector gs = null;
    private ProgressBar progressPage;
    Runnable mRunnable;
    Handler mHandler = new Handler();
//    private float pointX;
//    private float pointY;
//    private int tolerance = 50;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_page_detail_layout, container, false);

        wvContent = (ObservableWebView) view.findViewById(R.id.wvContent);
        progressPage = (ProgressBar) view.findViewById(R.id.progressPage);

        layout_menu_bottom = (RelativeLayout) getActivity().findViewById(R.id.layout_menu_bottom);
        layout_menu_top = (RelativeLayout) getActivity().findViewById(R.id.layout_menu_top);
        relativeDetail = (RelativeLayout) view.findViewById(R.id.relativeDetail);

        bundle = getArguments();

        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.setWebViewClient(new myWebViewClient());
        wvContent.loadUrl(Constant.PATH_ASSEST + bundle.getString(Constant.PASS_CONTENT_PG));

//        wvContent.getSettings().setBuiltInZoomControls(true);
//        wvContent.setInitialScale(50);
        if (bundle.getBoolean("DHC")) {

        } else {
            wvContent.setOnTouchListener(this);
            wvContent.setOnScrollChangedCallback(this);
        }

        mRunnable = new Runnable() {
            @Override
            public void run () {

                    if (layout_menu_top.getVisibility() == View.VISIBLE) {
                        layout_menu_bottom.setVisibility(View.GONE);
                        layout_menu_top.setVisibility(View.GONE);
                        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_page_detail_invisible);
                        layout_menu_bottom.startAnimation(anim);
                        layout_menu_top.startAnimation(anim);
                    }

            }
        };
//        relativeDetail.setOnClickListener(this);
//        wvContent.setOnTouchListener(this);
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

//
//        if (gs == null) {
//            gs = new GestureDetector(getContext(),
//                    new GestureDetector.SimpleOnGestureListener() {
//                        @Override
//                        public boolean onDoubleTapEvent(MotionEvent e) {
//
//                            //Double Tap
//                            wvContent.zoomIn();//Zoom in
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//                            //Single Tab
//                            wvContent.zoomOut();// Zoom out
//                            return false;
//                        };
//                    });
//        }
//
//        gs.onTouchEvent(event);

        if (v.getId() == R.id.wvContent && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (layout_menu_top.getVisibility() == View.GONE) {
                layout_menu_bottom.setVisibility(View.VISIBLE);
                layout_menu_top.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_page_detail_visible);
                layout_menu_bottom.startAnimation(anim);
                layout_menu_top.startAnimation(anim);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }


        }



//        if (v.getId() == R.id.wvContent && event.getAction() == MotionEvent.ACTION_DOWN){
//            if (layout_menu_top.getVisibility() == View.VISIBLE) {
//                layout_menu_bottom.setVisibility(View.GONE);
//                layout_menu_top.setVisibility(View.GONE);
//            } else {
//                layout_menu_bottom.setVisibility(View.VISIBLE);
//                layout_menu_top.setVisibility(View.VISIBLE);
//            }
//        }
        return false;
    }

    @Override
    public void onScroll(int l, int t) {
        if (layout_menu_top.getVisibility() == View.VISIBLE) {
            layout_menu_bottom.setVisibility(View.GONE);
            layout_menu_top.setVisibility(View.GONE);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_page_detail_invisible);
            layout_menu_bottom.startAnimation(anim);
            layout_menu_top.startAnimation(anim);
        }
    }

//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//

//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                return false;
//            case MotionEvent.ACTION_DOWN:
//                pointX = event.getX();
//                pointY = event.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
//                boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
//                if (sameX && sameY) {
//                    //The user "clicked" certain point in the screen or just returned to the same position an raised the finger
//                    layout_menu_bottom.setVisibility(View.VISIBLE);
//                    layout_menu_top.setVisibility(View.VISIBLE);
//                    Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_page_detail_visible);
//                    layout_menu_bottom.startAnimation(anim);
//                    layout_menu_top.startAnimation(anim);
//                    Log.e("fsdfsfasfa", "123456789");
//                    mHandler.removeCallbacks(mRunnable);
//                    mHandler.postDelayed(mRunnable, 3000);
//                }
//        }
//        return true;
//
//    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressPage.setVisibility(View.GONE);
        }
    }
}
