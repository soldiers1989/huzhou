package com.huzhou.gjj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Plan;

import java.util.List;

import static com.huzhou.gjj.R.id.account_balance;


public class PlanAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Plan> list;

    public PlanAdapter(Context context, List<Plan> list) {
        this.mContext = context;
        this.list = list;
    }

    /*
     * Gets the data associated with the given child within the given group
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // 我们这里返回一下每个item的名称，以便单击item时显示
        return list.get(groupPosition);
    }

    /*
     * 取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /*
     * Gets a View that displays the data for the given child within the given
     * group
     */
    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_childitem, null);
            childHolder = new ChildHolder();
            childHolder.account_balance = (TextView) convertView
                    .findViewById(account_balance);
            childHolder.account_incurred = (TextView) convertView
                    .findViewById(R.id.account_incurred);
            childHolder.account_sign = (TextView) convertView
                    .findViewById(R.id.account_sign);
            childHolder.account_number = (TextView) convertView
                    .findViewById(R.id.account_number);
            childHolder.account_code = (TextView) convertView
                    .findViewById(R.id.account_code);
            childHolder.account_year = (TextView) convertView
                    .findViewById(R.id.account_year);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        childHolder.account_number.setText("期次:  "
                + list.get(groupPosition).getQC());
        childHolder.account_sign.setText("应还本金:  "
                + list.get(groupPosition).getYHBJ());
        childHolder.account_code.setText("应还利息:  "
                + list.get(groupPosition).getYHLX());


        childHolder.account_year.setText("本息合计:  "
                + list.get(groupPosition).getBXHJ());
        childHolder.account_incurred.setText("累计本金:  "
                + list.get(groupPosition).getLJBJ());
        childHolder.account_balance.setText("累计利息：" + list.get(groupPosition).getLJLX());

        return convertView;
    }

    /*
     * 取得指定分组的子元素数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }

    /**
     * 取得与给定分组关联的数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    /**
     * 取得分组数
     */
    @Override
    public int getGroupCount() {
        return list.size();
    }

    /**
     * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /*
     * Gets a View that displays the given groupreturn: the View corresponding
     * to the group at the specified position
     */
    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_groupitem, null);
            groupHolder = new GroupHolder();
            groupHolder.groupImg = (ImageView) convertView
                    .findViewById(R.id.img_indicator);
            groupHolder.account_date = (TextView) convertView
                    .findViewById(R.id.account_date);
            groupHolder.account_type = (TextView) convertView
                    .findViewById(R.id.account_type);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupHolder.groupImg.setBackgroundResource(R.mipmap.downarrow);
        } else {
            groupHolder.groupImg.setBackgroundResource(R.mipmap.rightarrow);
        }
        groupHolder.account_type.setText(list.get(groupPosition).getNY() + "   本金余额" +
                list.get(groupPosition).getBJYE()
        );

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // Indicates whether the child and group IDs are stable across changes
        // to the underlying data
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // Whether the child at the specified position is selectable
        return true;
    }

    /**
     * show the text on the child and group item
     */
    private class GroupHolder {
        ImageView groupImg;
        TextView account_type, account_date;
    }

    private class ChildHolder {
        TextView account_number, account_sign, account_incurred,
                account_balance, account_year, account_code;
    }

}
