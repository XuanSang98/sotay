package net.vinsofts.handbooks.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.fragment.PageDetailFragment;
import net.vinsofts.handbooks.object.PageDetailObject;

/**
 * Created by HongChien on 25/04/2016.
 */
public class PageDetailPagerAdapter extends FragmentStatePagerAdapter {
    private List<PageDetailObject> listPageDetail;

    public PageDetailPagerAdapter(FragmentManager fm, List<PageDetailObject> listPageDetail) {
        super(fm);
        this.listPageDetail = listPageDetail;
    }

    @Override
    public Fragment getItem(int position) {
        PageDetailFragment pageFragment = new PageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.PASS_CONTENT_PG,listPageDetail.get(position).getContent());
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public int getCount() {
        return listPageDetail.size();
    }
}
