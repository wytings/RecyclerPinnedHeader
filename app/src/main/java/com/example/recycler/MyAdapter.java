package com.example.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class MyAdapter extends AbsRecyclerAdapter {

    public static final int TYPE_PARENT = 1;
    public static final int TYPE_CHILD = 2;

    @NonNull
    @Override
    public AbsRecyclerViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARENT) {
            return new ParentViewHolder(parent, this);
        } else if (viewType == TYPE_CHILD) {
            return new ChildViewHolder(parent);
        }
        return new AbsRecyclerViewHolder<Object>(new View(parent.getContext())) {
            @Override
            public Object getBoundData() {
                return super.getBoundData();
            }
        };
    }


    @Override
    public int getItemType(int position) {
        final Object data = getItemData(position);
        if (data instanceof ParentModel) {
            return TYPE_PARENT;
        } else if (data instanceof ChildModel) {
            return TYPE_CHILD;
        }
        return super.getItemType(position);
    }
}
