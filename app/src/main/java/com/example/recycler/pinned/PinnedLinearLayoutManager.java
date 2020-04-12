package com.example.recycler.pinned;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycler.AbsRecyclerViewHolder;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class PinnedLinearLayoutManager extends LinearLayoutManager {

    private RecyclerView recyclerView;
    private final PinnedInfo pinnedInfo = new PinnedInfo();

    public PinnedLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        updatePinnedLayout();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int verticallyBy = super.scrollVerticallyBy(dy, recycler, state);
        updatePinnedLayout();
        return verticallyBy;
    }

    private void updatePinnedLayout() {
        if (recyclerView == null) {
            return;
        }

        final int firstVisibleItemPosition = findFirstVisibleItemPosition();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (firstVisibleItemPosition == RecyclerView.NO_POSITION || adapter == null) {
            pinnedInfo.clearPinned();
            return;
        }

        pinnedInfo.resetState();
        final int firstVisibleType = adapter.getItemViewType(firstVisibleItemPosition);

        if (firstVisibleType == IPinnedHolder.TYPE_PARENT) {
            final RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(firstVisibleItemPosition);
            if (holder instanceof IPinnedHolder) {
                if (((IPinnedHolder) holder).isPinned()) {
                    pinnedInfo.tryCreateViewHolder(recyclerView, firstVisibleItemPosition);
                }
            }
        } else if (firstVisibleType == IPinnedHolder.TYPE_CHILD) {
            int index = firstVisibleItemPosition - 1;
            while (index >= 0) {
                if (adapter.getItemViewType(index) == IPinnedHolder.TYPE_PARENT) {
                    pinnedInfo.tryCreateViewHolder(recyclerView, index);
                    break;
                }
                index--;
            }
        }

        if (pinnedInfo.isAvailable()) {
            int secondVisibleItem = firstVisibleItemPosition + 1;
            while (secondVisibleItem <= firstVisibleItemPosition + 3) {
                if (adapter.getItemViewType(secondVisibleItem) == IPinnedHolder.TYPE_PARENT) {
                    final AbsRecyclerViewHolder secondParent = (AbsRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(secondVisibleItem);
                    if (secondParent != null) {
                        pinnedInfo.setTopOffset(secondParent.itemView.getY());
                    }
                    break;
                }
                secondVisibleItem++;
            }
        }

        if (pinnedInfo.isAvailable()) {
            pinnedInfo.attachToPinnedGroup(recyclerView);
        } else {
            pinnedInfo.clearPinned();
        }
    }

}
