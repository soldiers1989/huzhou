package com.huzhou.gjj.adapter;

import java.util.ArrayList;

import com.huzhou.gjj.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> nameList;
	private int[] imgArray;

	public AccountAdapter(Context context,int[] imgArray,ArrayList<String> nameList) {
		this.context = context;
		this.nameList = nameList;
		this.imgArray = imgArray;
	}

	@Override
	public int getCount() {
		return imgArray.length;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.account_adapter,
					null);
			holder = new ViewHolder();
			holder.ivDrug = (ImageView) convertView
					.findViewById(R.id.item_account_iv);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.item_name_tv);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		holder.ivDrug.setImageResource(imgArray[position]);
		holder.tvName.setText(nameList.get(position));

		return convertView;
	}

	class ViewHolder {
		ImageView ivDrug;
		TextView tvName;
	}

}
