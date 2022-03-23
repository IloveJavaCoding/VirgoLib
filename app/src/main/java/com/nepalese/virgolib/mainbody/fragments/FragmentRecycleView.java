package com.nepalese.virgolib.mainbody.fragments;

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
    }

    private void setListener() {
    }
}
