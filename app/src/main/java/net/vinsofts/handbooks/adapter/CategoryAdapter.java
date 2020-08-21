package net.vinsofts.handbooks.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

import java.util.ArrayList;
import java.util.List;

import net.vinsofts.handbooks.R;
import net.vinsofts.handbooks.common.Constant;
import net.vinsofts.handbooks.object.CategoryObject;
import net.vinsofts.handbooks.object.PageDetailObject;
import net.vinsofts.handbooks.sqlite.Database;

/**
 * Created by AT on 4/20/2016.
 */
public class CategoryAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<CategoryObject> listCategory;
    private List<CategoryObject> orig;
    private List<PageDetailObject> listPageDetail;
    private int resource;
    private Database database;
    int result;
    int resultFinal;
    int resultOverview;


    public CategoryAdapter(Context mContext, int resource, List<CategoryObject> listCategory, Database database) {

        this.mContext = mContext;
        this.resource = resource;
        this.listCategory = listCategory;
        this.database = database;

    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public CategoryObject getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder holder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_category_listview, parent, false);
            holder = new ViewHolder();
            holder.tvCategoryName = (TextView) rowView.findViewById(R.id.tvCategoryName);
            holder.circle_progress = (CircleProgress) rowView.findViewById(R.id.circle_progress);
            rowView.setTag(holder);
        }
        holder = (ViewHolder) rowView.getTag();
        holder.tvCategoryName.setText(listCategory.get(position).getName());
        holder.circle_progress.setProgress(excuteProgress(position));
        updateProgressIntoCate(position);
        if (listCategory.get(position).getPercentComplete() == 100) {
            holder.tvCategoryName.setTextColor(Color.GREEN);
        } else {
            holder.tvCategoryName.setTextColor(Color.WHITE);
        }
        return rowView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<CategoryObject> results = new ArrayList<>();
                if (orig == null)
                    orig = listCategory;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final CategoryObject category : orig) {
                            if (category.getName().toLowerCase().contains(constraint.toString())) {
                                results.add(category);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCategory = (List<CategoryObject>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public int excuteProgress(int position) {
        result=0;
        listPageDetail = database.listPageDb(Constant.PAGE_DETAIL_TABLE,listCategory.get(position).getId());
        database.closeDatabase();
        for (int i = 0; i < listPageDetail.size(); i++) {
            result += listPageDetail.get(i).getComplete();
            resultFinal = result/(listPageDetail.size());
        }
        return resultFinal;

    }
//
    public void updateProgressIntoCate(int position) {
        listCategory.get(position).setPercentComplete(excuteProgress(position));
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvCategoryName;
        CircleProgress circle_progress;
    }
}
