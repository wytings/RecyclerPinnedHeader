package com.example.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recycler.pinned.IPinnedHolder;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class ChildViewHolder extends AbsRecyclerViewHolder<ChildModel> implements IPinnedHolder {
    private final TextView button, text;

    public ChildViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_child, parent, false));
        button = itemView.findViewById(R.id.button);
        text = itemView.findViewById(R.id.text);
    }

    @Override
    public void onBind(ChildModel data, int dataIndex) {
        super.onBind(data, dataIndex);
        text.setText(data.title);
    }

    @Override
    public boolean isPinned() {
        return false;
    }
}
