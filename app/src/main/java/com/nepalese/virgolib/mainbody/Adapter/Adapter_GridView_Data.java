package com.nepalese.virgolib.mainbody.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.virgolib.R;

import java.util.List;

/**
 * Created by Administrator on 2022/3/23.
 * Usage:
 */

public class Adapter_GridView_Data extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<String> data;
    private final InnerClickListener clickListener;

    public Adapter_GridView_Data(Context context, List<String> data, InnerClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
        private TextView tvData;
        private ImageView imgDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_gridview_data, null);
            holder = new ViewHolder();

            holder.tvData = convertView.findViewById(R.id.tv_gridview);
            holder.imgDel = convertView.findViewById(R.id.img_gridview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvData.setText(data.get(position));
        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击监听
                if(clickListener!=null){
                    clickListener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    public interface InnerClickListener{
        void onItemClick(int position);
    }
}
