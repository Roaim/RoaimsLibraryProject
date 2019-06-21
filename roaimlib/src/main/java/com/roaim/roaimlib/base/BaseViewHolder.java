package com.roaim.roaimlib.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Roaim on 22-Jul-18.
 */

public abstract class BaseViewHolder<B extends ViewDataBinding, D> extends RecyclerView.ViewHolder {
    public B binding;

    public BaseViewHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public abstract void bind(D data);

    public void unBind() {
        if (binding != null) {
            binding.unbind();
        }
    }
}
