package net.vinsofts.handbooks.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.adapter.PageDetailPagerAdapter;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.fragment.TableOfContentFragment;
import net.vinsofts.handbooks.object.BookmarkObject;
import net.vinsofts.handbooks.object.PageDetailObject;
import net.vinsofts.handbooks.object.TableOfContentObject;
import net.vinsofts.handbooks.sqlite.Database;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HongChien on 22/04/2016.
 */
public class PageDetailActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ViewPager vpPageDetail;
    private List<PageDetailObject> listPageDetail = new ArrayList<>();
    private List<BookmarkObject> listBookmarkNote = new ArrayList<>();
    private List<TableOfContentObject> listTableOfContent = new ArrayList<>();

    private Database database;
    private TextView tvPageNumber;
    private ImageView imgBack, imgNode, imgMenuPageDetail, imgShare;
    private ToggleButton tgDone, tgFavorite;
    private int checkPosition;
    private PageDetailPagerAdapter adapterPager;
    private DrawerLayout drawerLayout;
    private FrameLayout containerTreeView;
    private int resultFinal;
    private String contentBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_detail_layout);
//        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        Intent intent = getIntent();
        int idCategory = intent.getIntExtra(Constant.PASS_ID_CATEGORY_TABLE, -1);
        //Gán fragment cho framelayout sliding
        FragmentManager frManager = getSupportFragmentManager();
        Fragment fragment = frManager.findFragmentById(R.id.containerTreeView);
        if (fragment == null) {
            fragment = new TableOfContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("PASS_ID", idCategory);
            ((TableOfContentFragment) fragment).listener = new TableOfContentFragment.TableContentFragmentListener() {
                @Override
                public void onClickItem(int pageId, int storyId) {
                    //change page here
                    listPageDetail = database.listPageDb(Constant.PAGE_DETAIL_TABLE, storyId);
                    adapterPager = new PageDetailPagerAdapter(
                            getSupportFragmentManager(),
                            listPageDetail
                    );
                    vpPageDetail.setAdapter(adapterPager);
                    for (PageDetailObject temp : listPageDetail) {

                        if (temp.getId() == pageId) {
                            int index = listPageDetail.indexOf(temp);
                            if (index > -1) {
                                vpPageDetail.setCurrentItem(index);
                            }
                            break;
                        }
                    }
                }
            };
            fragment.setArguments(bundle);
            frManager.beginTransaction().add(R.id.containerTreeView, fragment).commit();
        }

        initView();

        checkPosition = 0;

        drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, 0, 0));


        database = new Database(this);
        database.openDatabase();
        listPageDetail = database.listPageDb(Constant.PAGE_DETAIL_TABLE, idCategory);
        database.closeDatabase();

        tvPageNumber.setText(listPageDetail.get(0).getPageIndex() + "/" + listPageDetail.size());


        adapterPager = new PageDetailPagerAdapter(
                getSupportFragmentManager(),
                listPageDetail
        );
        vpPageDetail.setAdapter(adapterPager);


        if (listPageDetail.get(0).getComplete() == 100) {
            tgDone.setChecked(true);
        } else tgDone.setChecked(false);

        if (listPageDetail.get(0).getFavorite() == 100) {
            tgFavorite.setChecked(true);
        } else tgFavorite.setChecked(false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        contentBookmark = "";
        checkPosition = position;
        checkDefaultToggle(listPageDetail.get(position).getComplete(), tgDone);
        checkDefaultToggle(listPageDetail.get(position).getFavorite(), tgFavorite);
        tvPageNumber.setText(listPageDetail.get(position).getPageIndex() + "/" + listPageDetail.size());


        stateBookmark();
        if (listPageDetail.get(position).getmIconNote() == 100) {
            imgNode.setBackgroundResource(R.drawable.ic_note_select);
        } else {
            imgNode.setBackgroundResource(R.drawable.ic_note);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.imgNode:
                Intent intent = new Intent(PageDetailActivity.this, NoteActivity.class);
                intent.putExtra("ID_PAGEDETAIL", listPageDetail.get(checkPosition).getId());
                startActivity(intent);
                break;
            case R.id.imgMenuPageDetail:
                drawerLayout.openDrawer(containerTreeView);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgDone:
                checkToggle(isChecked, Constant.COMPLETE_COLUMN_PD);

                break;
            case R.id.tgFavorite:
                getListTableofContent();
                if (listTableOfContent != null) {
                    Log.d("Chien1231231", listTableOfContent.size() + "");
                }
                checkToggle(isChecked, Constant.FAVORITE_COLUMN_PD);
                break;

        }
    }

    private void checkToggle(boolean isChecked, String column) {
        database.openDatabase();
        if (isChecked) {
            database.updatePageDetail(
                    Constant.PAGE_DETAIL_TABLE,
                    column,
                    100,
                    listPageDetail.get(checkPosition).getPageIndex(),
                    listPageDetail.get(checkPosition).getStoryId());
            if (column.equals(Constant.COMPLETE_COLUMN_PD)) {
                listPageDetail.get(checkPosition).setComplete(100);
                updateProgressIntoCate();
            } else if (column.equals(Constant.FAVORITE_COLUMN_PD)) {

                for (int i = 0; i < listTableOfContent.size(); i++) {
                    if (listPageDetail.get(checkPosition).getId() == listTableOfContent.get(i).getPageDetailId()) {
                        contentBookmark = listTableOfContent.get(i).getName();
                    }
                }
                if (listPageDetail.get(checkPosition).getFavorite() == 0) {
                    database.insertBookmark(
                            listPageDetail.get(checkPosition).getStoryId(),
                            listPageDetail.get(checkPosition).getId(),
                            contentBookmark);
                }
                listPageDetail.get(checkPosition).setFavorite(100);
            }

        } else {
            if (column.equals(Constant.FAVORITE_COLUMN_PD)) {

                database.deleteNoteBookmark(listPageDetail.get(checkPosition).getStoryId(), listPageDetail.get(checkPosition).getId());
                listPageDetail.get(checkPosition).setFavorite(0);
            }
            if (column.equals(Constant.COMPLETE_COLUMN_PD)) {
                listPageDetail.get(checkPosition).setComplete(0);
                updateProgressIntoCate();
            }
            database.updatePageDetail(
                    Constant.PAGE_DETAIL_TABLE,
                    column,
                    0,
                    listPageDetail.get(checkPosition).getPageIndex(),
                    listPageDetail.get(checkPosition).getStoryId());

        }

        database.closeDatabase();
    }

    private void checkDefaultToggle(float attr, ToggleButton view) {
        if (attr == 100) {
            view.setChecked(true);
        } else view.setChecked(false);
    }

    private void initView() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        vpPageDetail = (ViewPager) findViewById(R.id.vpPageDetail);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgNode = (ImageView) findViewById(R.id.imgNode);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        tgDone = (ToggleButton) findViewById(R.id.tgDone);
        tgFavorite = (ToggleButton) findViewById(R.id.tgFavorite);
        tvPageNumber = (TextView) findViewById(R.id.tvPageNumber);
        imgMenuPageDetail = (ImageView) findViewById(R.id.imgMenuPageDetail);
        containerTreeView = (FrameLayout) findViewById(R.id.containerTreeView);

        tgDone.setOnCheckedChangeListener(this);
        tgFavorite.setOnCheckedChangeListener(this);
        imgBack.setOnClickListener(this);
        imgNode.setOnClickListener(this);
        imgMenuPageDetail.setOnClickListener(this);
        vpPageDetail.setOnPageChangeListener(this);
        imgShare.setOnClickListener(this);
    }

    public int excuteProgress() {
        int result = 0;
        for (int i = 0; i < listPageDetail.size(); i++) {
            result += listPageDetail.get(i).getComplete();
            resultFinal = result / (listPageDetail.size());
        }
        return resultFinal;
    }

    public void updateProgressIntoCate() {
        database.updateCategory(Constant.CATEGORY_TABLE, listPageDetail.get(checkPosition).getStoryId(), excuteProgress());
        database.closeDatabase();
    }

    //Check state icon note
    private void stateBookmark() {
        database = new Database(this);
        database.openDatabase();
        listBookmarkNote = database.listBookmarkDB(Constant.BOOKMARK_TABLE);
        database.closeDatabase();
        for (int i = 0; i < listPageDetail.size(); i++) {
            listPageDetail.get(i).setmIconNote(0);
        }
        for (int i = 0; i < listBookmarkNote.size(); i++) {
            if (listBookmarkNote.get(i).getStoryId() == 0 && listBookmarkNote.get(i).getPageDetailId() == listPageDetail.get(checkPosition).getId()) {
                listPageDetail.get(checkPosition).setmIconNote(100);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stateBookmark();

        if (listPageDetail.get(checkPosition).getmIconNote() == 100) {
            imgNode.setBackgroundResource(R.drawable.ic_note_select);
        } else {
            imgNode.setBackgroundResource(R.drawable.ic_note);
        }
    }

    private void getListTableofContent() {
        SharedPreferences sharedPrefs = getSharedPreferences("tableofcontent", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("ListTableOfContent", null);
        Type type = new TypeToken<List<TableOfContentObject>>() {
        }.getType();
        listTableOfContent = gson.fromJson(json, type);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
