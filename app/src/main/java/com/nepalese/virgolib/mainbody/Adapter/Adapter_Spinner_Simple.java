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
 * Created by Administrator on 2022/3/17.
 * Usage:自定义spinner 适配器
 */

public class Adapter_Spinner_Simple extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> data;

    public Adapter_Spinner_Simple(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data==null? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);//需要
    }

    @Override
    public long getItemId(int position) {
        return position;//需要
    }

    static class ViewHolder {
        public TextView tvData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_spinner_simple, null);
            holder = new ViewHolder();

            holder.tvData = convertView.findViewById(R.id.tvData);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvData.setText(data.get(position));
        return convertView;
    }
}
