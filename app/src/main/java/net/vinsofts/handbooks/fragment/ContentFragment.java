package net.vinsofts.handbooks.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.activity.BackgroundActivity;
import net.vinsofts.handbooks.activity.PageDetailActivity;
import net.vinsofts.handbooks.adapter.CategoryAdapter;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.object.CategoryObject;
import net.vinsofts.handbooks.sqlite.Database;

/**
 * Created by Administrator on 20/04/2016.
 */
public class ContentFragment extends Fragment implements TextWatcher, AdapterView.OnItemClickListener, View.OnClickListener {
    private Database database;
    private List<CategoryObject> listContent = new ArrayList<>();
    private ListView lstContent;
    private EditText svContent;
    private ImageButton imgContentShare;
    private CategoryAdapter adapter;
    private View view;
    private RelativeLayout reFragmentContent;
    private SwipeRefreshLayout swipeContainer;
    private ImageButton imgBackground;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_content, viewGroup, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

        swipeContainer.setOnRefreshListener(new MyRefreshListView(getActivity(),swipeContainer));

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //open database
        database = new Database(getActivity());
        database.openDatabase();
        listContent = database.listCateDb(Constant.CATEGORY_TABLE);
        database.closeDatabase();

        adapter = new CategoryAdapter(getActivity(), R.layout.item_category_listview, listContent, database);
        lstContent.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PageDetailActivity.class);
        CategoryObject item = adapter.getItem(position);
        intent.putExtra(Constant.PASS_ID_CATEGORY_TABLE, item.getId());
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgContentShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = getString(R.string.share_body);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
                break;
            case  R.id.imgBackground:
                Intent intent = new Intent(getActivity(), BackgroundActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                break;

        }
    }

    private void initView() {
        imgBackground = (ImageButton) view.findViewById(R.id.imgBackground);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        lstContent = (ListView) view.findViewById(R.id.lstContent);
        svContent = (EditText) view.findViewById(R.id.svContent);
        imgContentShare = (ImageButton) view.findViewById(R.id.imgContentShare);
        reFragmentContent = (RelativeLayout) view.findViewById(R.id.reFragmentContent);
        svContent.addTextChangedListener(this);
        lstContent.setTextFilterEnabled(true);
        lstContent.setOnItemClickListener(this);
        imgContentShare.setOnClickListener(this);
        imgBackground.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 21) {
            reFragmentContent.setPadding(0, getStatusBarHeight(), 0, 0);
        }


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
