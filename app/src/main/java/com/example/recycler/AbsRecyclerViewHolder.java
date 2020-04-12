package com.example.recycler;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Rex.Wei on 2018/11/23.
 *
 * @author 韦玉庭 weiyuting
 */
public abstract class AbsRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private T boundData;

    public AbsRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public T getBoundData() {
        return boundData;
    }

    public void setBoundData(T boundData) {
        this.boundData = boundData;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public void onBind(T data, int dataIndex) {
        // prepare for future
    }
}
