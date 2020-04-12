package com.example.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recycler.pinned.IPinnedHolder;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class ParentViewHolder extends AbsRecyclerViewHolder<ParentModel> implements IPinnedHolder {

    private final TextView button, text;

    public ParentViewHolder(final ViewGroup parent, final MyAdapter myAdapter) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_parent, parent, false));
        button = itemView.findViewById(R.id.button);
        text = itemView.findViewById(R.id.text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParentModel model = getBoundData();
                if (model != null) {
                    model.expanded = !model.expanded;
                    if (model.expanded) {
                        myAdapter.insert(getAdapterPosition() + 1, model.childModelList);
                    } else {
                        myAdapter.remove(model.childModelList);
                    }
                }
            }
        });
    }

    @Override
    public void onBind(ParentModel data, int dataIndex) {
        super.onBind(data, dataIndex);
        text.setText(data.title);
        Log.i("ParentViewHolder", "onBind,title = " + data.title);
    }

    @Override
    public boolean isPinned() {
        final ParentModel data = getBoundData();
        return data != null && data.expanded;
    }
}
