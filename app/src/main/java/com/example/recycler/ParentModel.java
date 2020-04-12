package com.example.recycler;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class ParentModel {
    public String title;
    public boolean expanded = false;
    public List<ChildModel> childModelList = new ArrayList<>();

    @NonNull
    @Override
    public String toString() {
        return title + "->" + childModelList.size();
    }
}
