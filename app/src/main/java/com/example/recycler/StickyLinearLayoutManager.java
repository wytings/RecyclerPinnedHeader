package com.example.recycler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

/**
 * Created by Rex.Wei on 2020-04-10
 *
 * @author 韦玉庭
 */
public class StickyLinearLayoutManager extends LinearLayoutManager {

    private static final String TAG = "StickyManager";
    private RecyclerView recyclerView;
    private PinnedInfo pinnedInfo = new PinnedInfo();

    public StickyLinearLayoutManager(Context context) {
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
        Log.i(TAG, "onLayoutCompleted, state=" + state);
        updateStickyHeader();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int verticallyBy = super.scrollVerticallyBy(dy, recycler, state);
        Log.i(TAG, "scrollVerticallyBy, scroll=" + verticallyBy + ",verticalScrollOffset=" + computeVerticalScrollOffset(state));
        updateStickyHeader();
        return verticallyBy;
    }

    private void updateStickyHeader() {
        if (recyclerView == null) {
            return;
        }

        int firstVisibleItemPosition = findFirstVisibleItemPosition();
        final View view = findViewByPosition(firstVisibleItemPosition);
        if (view == null) {
            pinnedInfo.clearPinned();
            return;
        }

        pinnedInfo.resetState();
        final RecyclerView.ViewHolder holder = recyclerView.findContainingViewHolder(view);
        if (holder instanceof ParentViewHolder) {
            if (((ParentViewHolder) holder).getBoundData().expanded) {
                pinnedInfo.tryCreateViewHolder(recyclerView, firstVisibleItemPosition);
            }
        } else if (holder instanceof ChildViewHolder) {
            int index = firstVisibleItemPosition - 1;
            while (index >= 0) {
                if (recyclerView.getAdapter().getItemViewType(index) == MyAdapter.TYPE_PARENT) {
                    pinnedInfo.tryCreateViewHolder(recyclerView, index);
                    log("item view parent holder index = %s ", index);
                    break;
                }
                index--;
            }
        }

        if (pinnedInfo.available) {
            int secondVisibleItem = firstVisibleItemPosition + 1;
            while (secondVisibleItem <= firstVisibleItemPosition + 3) {
                if (recyclerView.getAdapter().getItemViewType(secondVisibleItem) == MyAdapter.TYPE_PARENT) {
                    AbsRecyclerViewHolder secondParent = (AbsRecyclerViewHolder) recyclerView.findViewHolderForAdapterPosition(secondVisibleItem);
                    pinnedInfo.nullableTopOffset = secondParent.itemView.getY();
                    log("item view second parent holder model = %s,topOffset = %s", secondParent.getBoundData(), pinnedInfo.nullableTopOffset);
                    break;
                }
                secondVisibleItem++;
            }
        }


        if (pinnedInfo.available) {
            log("available true attach to pinned,firstVisibleItemPosition = " + firstVisibleItemPosition);
            pinnedInfo.attachToPinnedGroup(recyclerView);
        } else {
            log("available false clear pinned, firstVisibleItemPosition = " + firstVisibleItemPosition);
            pinnedInfo.clearPinned();
        }

        if (pinnedInfo.viewHolder != null) {
            Log.i(TAG, "updateStickyHeader =" + firstVisibleItemPosition + ", top=" + pinnedInfo.viewHolder.itemView.getTop());

        }
    }

    private static class PinnedInfo {
        private RecyclerView.ViewHolder viewHolder;
        private boolean available;
        private Float nullableTopOffset;

        private int getViewType() {
            if (viewHolder == null) {
                return RecyclerView.INVALID_TYPE;
            }
            return viewHolder.getItemViewType();
        }

        public void tryCreateViewHolder(RecyclerView recyclerView, int position) {
            int viewType = recyclerView.getAdapter().getItemViewType(position);
            if (getViewType() != viewType) {
                clearPinned();
                this.viewHolder = recyclerView.getAdapter().createViewHolder((FrameLayout) recyclerView.getParent(), viewType);
            }
            this.available = true;
            if (viewHolder != null) {
                recyclerView.getAdapter().onBindViewHolder(viewHolder, position);
            }
        }

        private void attachToPinnedGroup(RecyclerView recyclerView) {
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
                    log("item view null, y = %s, height=%s", y, viewHolder.itemView.getMeasuredHeight());
                    viewHolder.itemView.setTranslationY(0);
                } else {
                    if (nullableTopOffset < viewHolder.itemView.getMeasuredHeight()) {
                        final int top = (int) (nullableTopOffset - viewHolder.itemView.getMeasuredHeight());
                        viewHolder.itemView.setTranslationY(top);
                        log("item view not null, holder =%s, top = %s, height=%s, top = %s, topOffset =%s", ((AbsRecyclerViewHolder) viewHolder).getBoundData(), viewHolder.itemView.getTop(), viewHolder.itemView.getMeasuredHeight(), top, nullableTopOffset);
                    } else {
                        viewHolder.itemView.setTranslationY(0);
                        log("item view not null,holder =%s, y = %s, height=%s", ((AbsRecyclerViewHolder) viewHolder).getBoundData(), viewHolder.itemView.getY(), viewHolder.itemView.getMeasuredHeight());
                    }
                }
            }
        }

        public void resetState() {
            available = false;
            nullableTopOffset = null;
        }

        public void clearPinned() {
            if (viewHolder != null && viewHolder.itemView.getParent() instanceof ViewGroup) {
                ((ViewGroup) viewHolder.itemView.getParent()).removeView(viewHolder.itemView);
            }
        }
    }

    private static void log(String format, Object... args) {
        Log.i(TAG, String.format(Locale.getDefault(), format, args));
    }

}
