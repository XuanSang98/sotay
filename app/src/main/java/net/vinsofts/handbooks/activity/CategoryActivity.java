package net.vinsofts.handbooks.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.vinsofts.handbooks.R;

/**
 * Created by MyPC on 14/03/2017.
 */

public class CategoryActivity extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
//        ButterKnife.bind(this);

//        initRecyclerView();

    }


//    private void initRecyclerView() {
//        int spanCount = 3;
//        int spacing = 12;
//        boolean includeEdge = true;
//
//        RecyclerView.LayoutManager manager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
//        revCategory.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//        revCategory.setLayoutManager(manager);
//
//    }
}
