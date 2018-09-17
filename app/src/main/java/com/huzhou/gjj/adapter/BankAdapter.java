package com.huzhou.gjj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Bank;
import com.huzhou.gjj.bean.Number;

import java.util.List;

import static com.huzhou.gjj.R.id.kzyt;
import static com.huzhou.gjj.R.id.yhkbdzt;

public class BankAdapter extends BaseAdapter {
    private Context context;
    private List<Bank> nameList;
    private List<Number> list_zd;

    public BankAdapter(Context context, List<Bank> nameList, List<Number> list_zd) {
        this.context = context;
        this.nameList = nameList;
        this.list_zd = list_zd;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adaper_bank,
                    null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.name);
            holder.kzyt = (TextView) convertView
                    .findViewById(kzyt);
            holder.yhkbdzt = (TextView) convertView
                    .findViewById(yhkbdzt);
            holder.num = (TextView) convertView
                    .findViewById(R.id.num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String khyhlx = null, kzyt = null, yhkbdzt = null;
        Number number;
        for (int i = 0; i < list_zd.size(); i++) {
            number = list_zd.get(i);
            if ("khyhlx".equals(number.getDictType()) && nameList.get(position).getKHYH().equals(number.getCode()))
                khyhlx = number.getName();
            if ("kzyt".equals(number.getDictType()) && nameList.get(position).getKZYT().equals(number.getCode()))
                kzyt = number.getName();
            if ("yhkbdzt".equals(number.getDictType()) && nameList.get(position).getYHKBDZT().equals(number.getCode()))
                yhkbdzt = number.getName();
        }
        holder.name.setText(khyhlx);
        holder.kzyt.setText(kzyt);
        holder.yhkbdzt.setText(yhkbdzt);
        String yhzh = nameList.get(position).getYHZH();
        if (yhzh.length() >12)
        holder.num.setText("**************" + yhzh.substring(yhzh.length() - 5, yhzh.length()));
        else
            holder.num.setText(yhzh);
        return convertView;
    }

    private class ViewHolder {
        TextView name, kzyt, yhkbdzt, num;
    }

}
