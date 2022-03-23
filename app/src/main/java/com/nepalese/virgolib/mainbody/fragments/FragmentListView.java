package com.nepalese.virgolib.mainbody.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nepalese.virgolib.R;
import com.nepalese.virgolib.mainbody.Adapter.Adapter_ListView_Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Administrator on 2022/3/21.
 * Usage: listview + 简单数据显示
 */

public class FragmentListView extends Fragment {
    private static final String TAG = "FragmentListView";
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listview, container, false);

        init();
        return view;
    }

    private void init() {
        ListView listView = view.findViewById(R.id.listview_data);
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            data.add("item_" + i);
        }

        listView.setAdapter(new Adapter_ListView_Data(view.getContext(), data));

        //项目点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + position);
            }
        });
    }
}
