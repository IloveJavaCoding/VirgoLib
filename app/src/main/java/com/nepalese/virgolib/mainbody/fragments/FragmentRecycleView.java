package com.nepalese.virgolib.mainbody.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_RecycleView_Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by Administrator on 2022/3/21.
 * Usage:
 */

public class FragmentRecycleView extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<String> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycleview, container, false);

        init();
        setData();
        setListener();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycleview_data);
    }

    private void setData() {
        data = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            data.add("item_" + i);
        }

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new Adapter_RecycleView_Data(view.getContext(), data));

        //RecyclerView没有可以直接设置间距的属性，可以用ItemDecoration来装饰一个item，所以继承重写ItemDecoration就可以实现间距;
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration(10, 8);
        itemDecoration.setRowNum(2);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setListener() {
    }
}

class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private int top;
    private int left;
    private int right;
    private int bottom;
    private int rowNum = 1;//每行项目个数

    /**
     * 传入一个值，默认四个值相同
     * @param space 间隔值
     */
    public SpaceItemDecoration(int space) {
        this.top = space;
        this.left = space;
        this.right = space;
        this.bottom = space;
    }

    public SpaceItemDecoration(int tm, int lr) {
        this.top = tm;
        this.left = lr;
        this.right = lr;
        this.bottom = tm;
    }

    public SpaceItemDecoration(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) < rowNum){
            outRect.top = top;
        }
    }
}
