package com.nepalese.virgolib.mainbody.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_GridView_Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2022/3/21.
 * Usage:  gridview + 数据显示 + 数据自带点击监听任务（删除）
 */
public class FragmentGridView extends Fragment implements Adapter_GridView_Data.InnerClickListener {
    private static final String TAG = "FragmentGridView";
    private View view;
    private GridView gridView;
    private List<String> data;
    private Adapter_GridView_Data adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gridview, container, false);

        init();
        setData();
        setListener();
        return view;
    }

    private void init() {
        gridView = view.findViewById(R.id.gridview_data);
    }

    private void setData() {
        data = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            data.add("item_" + i);
        }

        adapter = new Adapter_GridView_Data(view.getContext(), data, this);
        gridView.setAdapter(adapter);
    }

    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //todo 确认弹窗！
        Log.i(TAG, "删除: " + position);
        data.remove(position);
        adapter.notifyDataSetChanged();//通知适配器数据变化
    }
}
