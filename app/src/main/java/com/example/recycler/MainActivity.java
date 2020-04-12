package com.example.recycler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.recycler.databinding.ActivityMainBinding;
import com.example.recycler.pinned.PinnedLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ActivityMainBinding binding;
    MyAdapter myAdapter = new MyAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new PinnedLinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(myAdapter);
        myAdapter.setDataList(createDataList());
    }

    private List createDataList() {
        int parentCount = 50;
        int childCount = 30;
        List<Object> list = new ArrayList<>(parentCount);
        for (int i = 1; i <= parentCount; i++) {
            ParentModel parentModel = new ParentModel();
            parentModel.title = "Parent." + i;
            List<ChildModel> childModelList = new ArrayList<>(childCount);
            for (int j = 1; j <= childCount; j++) {
                childModelList.add(new ChildModel("Child." + j));
            }
            parentModel.childModelList = childModelList;
            list.add(parentModel);
        }
        for (int i = 0;i<50;i++){
            list.add(String.valueOf(i));
        }
        return list;
    }
}
