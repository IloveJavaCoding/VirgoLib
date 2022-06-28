package com.nepalese.virgocomponent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nepalese.virgocomponent.R;
import com.nepalese.virgocomponent.component.bean.CheckBean;
import com.nepalese.virgocomponent.view.VirgoFileSelectorDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListView_FileSelector_Adapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<File> data;
    private final List<CheckBean> beans = new ArrayList<>();//记录checkbox的选中情况
    private final FileInterListener interListener;//供外部引用接口
    private final int flag;//可选类型 0：dir 1:file

    public ListView_FileSelector_Adapter(Context context, List<File> data, FileInterListener interListener, int flag) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.interListener = interListener;
        this.flag = flag;
        for (int i = 0; i < data.size(); i++) {
            CheckBean bean = new CheckBean(i, false);
            beans.add(bean);
        }
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
        public LinearLayout layout;
        public TextView tvData;
        public ImageView imageView;
        public CheckBox checkBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_file_selector_list, null);
            holder = new ViewHolder();

            holder.layout = convertView.findViewById(R.id.layout_all);
            holder.tvData = convertView.findViewById(R.id.tvFilePath);
            holder.imageView = convertView.findViewById(R.id.imgFileDir);
            holder.checkBox = convertView.findViewById(R.id.cbChoose);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        String path = data.get(position).getPath();
        holder.tvData.setText(path.substring(path.lastIndexOf("/") + 1));//show the last layer

        if (data.get(position).isDirectory()) {
            Glide.with(context).load(R.mipmap.icon_dir).into(holder.imageView);
//            holder.imageView.setImageResource(R.mipmap.icon_dir);
            holder.checkBox.setEnabled(flag != VirgoFileSelectorDialog.FLAG_FILE);
        } else {
            //file
            holder.checkBox.setEnabled(flag != VirgoFileSelectorDialog.FLAG_DIR);
            String tail = path.substring(path.lastIndexOf(".") + 1);
            switch (tail.toLowerCase()) {
                case "mp3":
                case "wav":
                case "mp4":
                    Glide.with(context).load(R.mipmap.icon_media).into(holder.imageView);
//                    holder.imageView.setImageResource(R.mipmap.icon_media);
                    break;
                case "jpg":
                case "png":
                    //仅加载缩略图，否则可能爆掉
                    Glide.with(context).load(path).thumbnail(0.1f).into(holder.imageView);
                    break;
                default:
                    Glide.with(context).load(R.mipmap.icon_file).into(holder.imageView);
//                    holder.imageView.setImageResource(R.mipmap.icon_file);
                    break;
            }
        }

        //make component be able click from outside
        //防止CheckBox因滚动ListView时混乱
        holder.checkBox.setOnClickListener(view -> {
            beans.get(position).setChecked(!beans.get(position).isChecked());
            interListener.itemClick(view, beans.get(position).isChecked());
            holder.checkBox.setChecked(beans.get(position).isChecked());
        });

        holder.checkBox.setChecked(beans.get(position).isChecked());
        holder.checkBox.setTag(position);
        return convertView;
    }

    public interface FileInterListener {
        void itemClick(View v, boolean isChecked);
    }
}
