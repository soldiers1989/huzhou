package com.huzhou.gjj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huzhou.gjj.R;

import java.util.List;


public class SpinnerAdapter2 extends BaseAdapter {
    private Context context;
    private List<String> nameList;

    public SpinnerAdapter2(Context context, List<String> nameList) {
        this.context = context;
        this.nameList = nameList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.adapter_spinner, null);
        if (convertView != null) {
            TextView _TextView1 = (TextView) convertView.findViewById(R.id.text);
            _TextView1.setText(nameList.get(position));
        }
        return convertView;
    }
}
