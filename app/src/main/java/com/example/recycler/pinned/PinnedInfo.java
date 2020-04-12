package com.example.recycler.pinned;

import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Rex.Wei on 2020-04-12
 *
 * @author 韦玉庭
 */
public class PinnedInfo {
    private RecyclerView.ViewHolder viewHolder;
    private boolean available;
    private Float nullableTopOffset;

    private int getViewType() {
        if (viewHolder == null) {
            return RecyclerView.INVALID_TYPE;
        }
        return viewHolder.getItemViewType();
    }

    @SuppressWarnings("unchecked")
    public void tryCreateViewHolder(RecyclerView recyclerView, int position) {
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final ViewParent parent = recyclerView.getParent();
        if (adapter != null && parent instanceof ViewGroup) {
            available = true;
            int viewType = adapter.getItemViewType(position);
            if (getViewType() != viewType) {
                clearPinned();
                viewHolder = adapter.createViewHolder((ViewGroup) parent, viewType);
            }

            if (viewHolder != null) {
                adapter.onBindViewHolder(viewHolder, position);
            }
        }

    }

    public void attachToPinnedGroup(RecyclerView recyclerView) {
        final ViewParent parent = recyclerView.getParent();
        if (parent instanceof ViewGroup) {
            if (viewHolder.itemView.getParent() != parent) {
                if (viewHolder.itemView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) viewHolder.itemView.getParent()).removeView(viewHolder.itemView);
                }
                ((ViewGroup) parent).addView(viewHolder.itemView);
            }

            if (nullableTopOffset == null) {
                final float y = viewHolder.itemView.getY();
                viewHolder.itemView.setTranslationY(0);
            } else {
                if (nullableTopOffset < viewHolder.itemView.getMeasuredHeight()) {
                    final int top = (int) (nullableTopOffset - viewHolder.itemView.getMeasuredHeight());
                    viewHolder.itemView.setTranslationY(top);
                } else {
                    viewHolder.itemView.setTranslationY(0);
                }
            }
        }
    }

    public void setTopOffset(Float nullableTopOffset) {
        this.nullableTopOffset = nullableTopOffset;
    }

    public boolean isAvailable() {
        return available;
    }

    public void resetState() {
        available = false;
        nullableTopOffset = null;
    }

    public void clearPinned() {
        resetState();
        if (viewHolder != null && viewHolder.itemView.getParent() instanceof ViewGroup) {
            ((ViewGroup) viewHolder.itemView.getParent()).removeView(viewHolder.itemView);
        }
    }
}