package com.nepalese.virgolib.mainbody.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.virgolib.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 分两种布局
 */
public class Adapter_RecycleView_Data extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<String> data;

    public Adapter_RecycleView_Data(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(i%2==0){
            view = layoutInflater.inflate(R.layout.layout_recycleview_data2, null);
            return new ViewHolder2(view);
        }else{
            view = layoutInflater.inflate(R.layout.layout_recycleview_data, null);
            return new ViewHolder1(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder1){
            ((ViewHolder1) holder).imgCover1.setImageResource(R.mipmap.img_big);
            ((ViewHolder1) holder).tvData1.setText(data.get(position));
        }else if( holder instanceof ViewHolder2){
            ((ViewHolder2) holder).imgCover2.setImageResource(R.mipmap.img_samll);
            ((ViewHolder2) holder).tvData2.setText(data.get(position));
        }
    }

    //self define holder
    static class ViewHolder1 extends RecyclerView.ViewHolder{
        //according to the layout layout_recycler_view
        private TextView tvData1;
        private ImageView imgCover1;

        public ViewHolder1(View view) {
            super(view);
            tvData1 = view.findViewById(R.id.tv_data_recycleview1);
            imgCover1 = view.findViewById(R.id.img_cover_recycleview1);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder{
        //according to the layout layout_recycler_view
        private TextView tvData2;
        private ImageView imgCover2;

        public ViewHolder2(View view) {
            super(view);
            tvData2 = view.findViewById(R.id.tv_data_recycleview2);
            imgCover2 = view.findViewById(R.id.img_cover_recycleview2);
        }
    }
}