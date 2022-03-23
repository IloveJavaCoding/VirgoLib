package com.nepalese.virgolib.mainbody.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nepalese.virgolib.R;

import java.util.List;

/**
 * Created by Administrator on 2022/3/23.
 * Usage:
 */

public class Adapter_ListView_Data extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> data;

    public Adapter_ListView_Data(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_listview_data, null);
            holder = new ViewHolder();

            holder.tvName = convertView.findViewById(R.id.tv_data_listview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(data.get(position));
        return convertView;
    }
}
