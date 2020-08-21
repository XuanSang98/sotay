package net.vinsofts.handbooks.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.activity.BackgroundActivity;
import net.vinsofts.handbooks.activity.PageDetailActivity;
import net.vinsofts.handbooks.adapter.CategoryAdapter;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.object.CategoryObject;
import net.vinsofts.handbooks.sqlite.Database;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by Administrator on 20/04/2016.
 */
public class HomeFragment extends Fragment implements View.OnClickListener
        , RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    private ImageButton imgBackground, imgShare;
    private SegmentedGroup segmented3;
    private CategoryAdapter adapter;
    private Database database;
    private List<CategoryObject> listContentHome = new ArrayList<>();
    private List<CategoryObject> listUpdate = new ArrayList<>();
    private ListView lstCategory;
    private ArcProgress arcProgress;
    int result;
    private View view;
    private RelativeLayout reFragmentHome;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, viewGroup, false);
        initView();

        //open database
        database = new Database(getActivity());
        database.openDatabase();

        listContentHome = database.listCateDb(Constant.CATEGORY_TABLE);
        database.closeDatabase();

        adapter = new CategoryAdapter(getActivity(), R.layout.item_category_listview, listContentHome, database);

        lstCategory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBackground:
                Intent intent = new Intent(getActivity(), BackgroundActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                break;
            case R.id.imgShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = getString(R.string.share_body);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.btnLastest:
                Collections.sort(listContentHome, CategoryObject.compareID);
                break;

            case R.id.btnHighest:
                Collections.sort(listContentHome, CategoryObject.descendingPercent);
                break;

            case R.id.btnLowest:
                Collections.sort(listContentHome, CategoryObject.ascendingPercent);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PageDetailActivity.class);
        CategoryObject item = adapter.getItem(position);
        intent.putExtra(Constant.PASS_ID_CATEGORY_TABLE, item.getId());
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }

    private int setProgressOverview() {
        //open database
        database = new Database(getActivity());
        database.openDatabase();
        int valueProgress = 0;
        listUpdate = database.listCateDb(Constant.CATEGORY_TABLE);
        database.closeDatabase();
        for (int i = 0; i < listUpdate.size(); i++) {
            valueProgress += listUpdate.get(i).getPercentComplete();
            result = valueProgress / listUpdate.size();
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        arcProgress.setProgress(setProgressOverview());
    }

    private void initView() {
        reFragmentHome = (RelativeLayout) view.findViewById(R.id.reFragmentHome);

        if (Build.VERSION.SDK_INT >= 21) {
            reFragmentHome.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        imgShare = (ImageButton) view.findViewById(R.id.imgShare);
        arcProgress = (ArcProgress) view.findViewById(R.id.arc_progress);
        imgBackground = (ImageButton) view.findViewById(R.id.imgBackground);
        segmented3 = (SegmentedGroup) view.findViewById(R.id.segmented3);
        lstCategory = (ListView) view.findViewById(R.id.lstCategory);

        imgShare.setOnClickListener(this);
        imgBackground.setOnClickListener(this);
        segmented3.setOnCheckedChangeListener(this);
        lstCategory.setOnItemClickListener(this);
        swipeContainer.setOnRefreshListener(new MyRefreshListView(getActivity(), swipeContainer));

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}