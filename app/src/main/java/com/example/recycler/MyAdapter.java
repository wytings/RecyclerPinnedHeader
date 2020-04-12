package com.example.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.recycler.pinned.IPinnedHolder;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class MyAdapter extends AbsRecyclerAdapter {


    @NonNull
    @Override
    public AbsRecyclerViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == IPinnedHolder.TYPE_PARENT) {
            return new ParentViewHolder(parent, this);
        } else if (viewType == IPinnedHolder.TYPE_CHILD) {
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
            return IPinnedHolder.TYPE_PARENT;
        } else if (data instanceof ChildModel) {
            return IPinnedHolder.TYPE_CHILD;
        }
        return super.getItemType(position);
    }
}
