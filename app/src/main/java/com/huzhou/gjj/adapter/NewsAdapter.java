package com.huzhou.gjj.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.News;

import java.util.List;


public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<News> list;
    private int selectedPosition = -1;

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public NewsAdapter(Context context, List<News> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_guide, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (selectedPosition == position + 1) {
            convertView.setBackgroundColor(Color.parseColor("#EAEEF4"));
        } else
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        String title = list.get(position).getInfoTitle();
        if (title.length() > 17) title = title.substring(0, 16) + "...";
        holder.text.setText(title);
        return convertView;
    }

    private class ViewHolder {
        TextView text;
    }
}
