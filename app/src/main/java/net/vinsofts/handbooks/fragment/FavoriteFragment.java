package net.vinsofts.handbooks.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.activity.BackgroundActivity;
import net.vinsofts.handbooks.activity.NoteActivity;
import net.vinsofts.handbooks.activity.PrimacyActivity;
import net.vinsofts.handbooks.activity.WebviewTips;
import net.vinsofts.handbooks.adapter.BookMarkAdapter;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.common.SwipeMenuCreatorListView;
import net.vinsofts.handbooks.object.BookmarkObject;
import net.vinsofts.handbooks.object.PageDetailObject;
import net.vinsofts.handbooks.sqlite.Database;

/**
 * Created by Administrator on 20/04/2016.
 */
public class FavoriteFragment extends Fragment implements SwipeMenuListView.OnMenuItemClickListener,
        AdapterView.OnItemClickListener, View.OnClickListener {
    private SwipeMenuListView lvFavorite;
    private List<BookmarkObject> listFavorite = new ArrayList<>();
    private List<PageDetailObject> listPageDetail = new ArrayList<>();
    private SwipeMenuCreatorListView creator;
    private Database database;
    private BookMarkAdapter adapter;
    int currentPos;
    private String linkDescription;
    private TextView tvBaiTap123;
    private View view;
    private LinearLayout linearFavorite;
    private ImageButton imgShare, imgBackground;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, viewGroup, false);
        initView();

        return view;
    }

    @Override
    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
        currentPos = position;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Dialog);
        builder.setMessage(R.string.warning_when_delete)
                .setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //update lai favorite trong page detail
                        if (listFavorite.get(position).getStoryId() != 0) {
                            database.updateFavoritePageDetail(0, listFavorite.get(position).getPageDetailId());
                        }

                        //xoa khoi database bookmark
                        database.deleteBookmark(listFavorite.get(position).getId());
                        Toast.makeText(getActivity(), R.string.da_xoa, Toast.LENGTH_SHORT).show();

                        //xoa khoi listview
                        listFavorite.remove(position);
                        adapter.notifyDataSetChanged();

                        hideShowLink();

                    }
                })
                .setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        database = new Database(getActivity());
        database.openDatabase();
        listFavorite = database.listBookmarkDB(Constant.BOOKMARK_TABLE);
        database.closeDatabase();
        adapter = new BookMarkAdapter(getActivity(), R.layout.item_favorite_list_view, listFavorite);
        lvFavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        hideShowLink();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listFavorite.get(position).getStoryId() == 0) {
            Intent intent = new Intent(getActivity(), NoteActivity.class);
            intent.putExtra("text", listFavorite.get(position).getDescription());
            intent.putExtra("pageDetailId", listFavorite.get(position).getPageDetailId());
            intent.putExtra("position", position);
            startActivityForResult(intent, Constant.REQUEST_CODE_FAVORITE);
        } else {
            database = new Database(getActivity());
            database.openDatabase();
            listPageDetail = database.listPageDetail();
            database.closeDatabase();
            for (int i = 0; i < listPageDetail.size(); i++) {
                if (listFavorite.get(position).getPageDetailId() == listPageDetail.get(i).getId()) {
                    linkDescription = listPageDetail.get(i).getContent();
                }
            }

            Intent intent = new Intent(getActivity(), PrimacyActivity.class);
            intent.putExtra("LinkContent", linkDescription);
            intent.putExtra("TenHeader", listFavorite.get(position).getDescription());
            startActivity(intent);
            //TODO: lấy pageDetailID trong bookmark table, compare id pagedetail

            //If bang nhau thi get name cua thang pagedetail
            //Put gia tri nhan duoc sang thang primacyActivity

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Chinh sua note
        if (requestCode == Constant.REQUEST_CODE_FAVORITE && data != null) {
            database = new Database(getActivity());
            database.openDatabase();
            database.updateBookmark(Constant.DESCRIPTION_COLUMN_BM, data.getStringExtra("chien"), Constant.ID_COLUMN_CATE, listFavorite.get(data.getIntExtra("chienid", 0)).getId());
            database.closeDatabase();
            listFavorite.get(data.getIntExtra("chienid", 0)).setDescription(data.getStringExtra("chien"));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgBackground:
                intent = new Intent(getActivity(), BackgroundActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                break;
            case R.id.imgShare:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = getString(R.string.share_body);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
                break;
            case R.id.tvBaiTap123:
                intent = new Intent(getActivity(), WebviewTips.class);
                startActivity(intent);
                break;
        }



    }

    private void hideShowLink() {
        if (listFavorite.size() == 0) {
            tvBaiTap123.setVisibility(View.VISIBLE);
            lvFavorite.setVisibility(View.GONE);
        } else {
            tvBaiTap123.setVisibility(View.GONE);
            lvFavorite.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {

        imgShare = (ImageButton) view.findViewById(R.id.imgShare);
        imgBackground = (ImageButton) view.findViewById(R.id.imgBackground);
        linearFavorite = (LinearLayout) view.findViewById(R.id.linearFavorite);
        lvFavorite = (SwipeMenuListView) view.findViewById(R.id.lvFavorite);
        tvBaiTap123 = (TextView) view.findViewById(R.id.tvBaiTap123);
        tvBaiTap123.setOnClickListener(this);
        lvFavorite.setOnItemClickListener(this);
        lvFavorite.setOnMenuItemClickListener(this);
        imgShare.setOnClickListener(this);
        imgBackground.setOnClickListener(this);

        linkDescription = "";
        creator = new SwipeMenuCreatorListView(getActivity(), R.drawable.ic_delete);
        lvFavorite.setMenuCreator(creator);
        lvFavorite.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        if (Build.VERSION.SDK_INT >= 21) {
            linearFavorite.setPadding(0, getStatusBarHeight(), 0, 0);
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
