package com.roaim.roaimlib.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<VH extends BaseViewHolder<B, D>, B extends ViewDataBinding, D> extends RecyclerView.Adapter<VH> {

    protected abstract int getViewId();

    protected abstract VH onCreateViewHolder(B binding);

    private List<D> mList = new ArrayList<>();

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        B binding = DataBindingUtil.inflate(inflater, getViewId(), viewGroup, false);
        return onCreateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        D d = getItem(i);
        vh.bind(d);
    }

    public List<D> getAllItems() {
        return mList;
    }

    public D getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void reload(List<D> list) {
        if (!mList.isEmpty()) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(D item) {
        mList.add(item);
    }

    public int addItemAndNotify(D item) {
        int position = getItemCount();
        addItem(item);
        notifyItemInserted(position);
        return position;
    }

    public void removeItem(int position) {
        if (mList.size() > position) {
            mList.remove(position);
        }
    }

    public void removeItemAndNotify(int position) {
        removeItem(position);
        notifyItemRemoved(position);
    }

    public int removeItem(D item) {
        int position = mList.indexOf(item);
        mList.remove(position);
        return position;
    }

    public void removeItemAndNotify(D item) {
        int removeItem = removeItem(item);
        notifyItemRemoved(removeItem);
    }

    public int changeItem(D item) {
        int i = mList.indexOf(item);
        mList.set(i, item);
        return i;
    }

    public void changeItemAndNotify(D item) {
        int i = changeItem(item);
        notifyItemChanged(i);
    }
}
