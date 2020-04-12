package com.example.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rex.Wei on 2018/11/23.
 *
 * @author 韦玉庭 weiyuting
 */
public abstract class AbsRecyclerAdapter<T> extends RecyclerView.Adapter<AbsRecyclerViewHolder<T>> {

    private final ArrayList<T> dataList = new ArrayList<>();

    public void appendDataList(List<T> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void insert(int index, List<T> list) {
        dataList.addAll(index, list);
        notifyItemRangeInserted(index, list.size());
    }

    public void remove(List<T> list) {
        final int index = dataList.indexOf(list.get(0));
        dataList.removeAll(list);
        notifyItemRangeRemoved(index, list.size());
    }

    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> list) {
        dataList.clear();
        if (list != null && !list.isEmpty()) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (0 <= position && position < getItemCount()) {
            dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public T getItemData(int position) {
        if (0 <= position && position < dataList.size()) {
            return dataList.get(position);
        }
        return null;
    }

    public int getItemType(int position) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        return getItemType(position);
    }

    @Override
    public final void onBindViewHolder(@NonNull AbsRecyclerViewHolder<T> holder, int position) {
        final T data = dataList.get(position);
        holder.setBoundData(data);
        holder.onBind(data, position);
    }
}
