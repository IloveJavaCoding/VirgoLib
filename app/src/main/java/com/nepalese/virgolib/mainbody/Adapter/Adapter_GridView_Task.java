package com.nepalese.virgolib.mainbody.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.bean.TaskBean;

import java.util.List;

/**
 * Created by Administrator on 2022/3/10.
 * Usage:
 */

public class Adapter_GridView_Task extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<TaskBean> data;

    public Adapter_GridView_Task(Context context, List<TaskBean> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data==null? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView tvName;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_grid_view_task, null);
            holder = new ViewHolder();

            holder.tvName = convertView.findViewById(R.id.tvTaskName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(data.get(i).getName());
        return convertView;
    }

}
