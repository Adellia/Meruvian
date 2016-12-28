package com.lolaadellia.meruvian.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lolaadellia.meruvian.R;
import com.lolaadellia.meruvian.entity.Category;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hanum on 28/12/2016.
 */

public class CatAdapter extends BaseAdapter {

    private List<Category> newses = new ArrayList<Category>();
    // private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private Context context;
    private LayoutInflater inflater;

    public CatAdapter(Context context, List<Category> newses) {
        this.context = context;
        this.newses = newses;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addNews(Category news) {
        newses.add(news);
        notifyDataSetChanged();
    }

    public void addNews(List<Category> categories) {
        this.newses.addAll(categories);
        notifyDataSetChanged();
    }

    public void clear() {
        newses.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return newses.size();
    }

    @Override
    public Object getItem(int position) {
        return newses.get(position);
    }

    @Override
    public long getItemId(int id) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        CatAdapter.ViewHolder holder;
        if (view == null) {
            holder = new NewsAdapter.ViewHolder();
            view = inflater.inflate(R.layout.adapter_news, viewGroup, false);
            holder.name = (TextView) view.findViewById(R.id.text_title);
            holder.description = (TextView) view.findViewById(R.id.text_content);
            holder.date = (TextView) view.findViewById(R.id.text_date);
            view.setTag(holder);
        } else {
            holder = (NewsAdapter.ViewHolder) view.getTag();
        }
        holder.name.setText(newses.get(position).getTitle());
        holder.description.setText(newses.get(position).getContent());
        holder.date.setText(dateFormat.format(new Date(newses.get(position).getCreateDate())));
        return view;
    }

    private class ViewHolder {
        public TextView name;
        public TextView description;
        public TextView date;
    }

}
