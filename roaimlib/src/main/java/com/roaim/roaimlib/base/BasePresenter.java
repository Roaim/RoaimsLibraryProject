package com.roaim.roaimlib.base;


import android.support.annotation.NonNull;

import com.roaim.roaimlib.rx.CompositeDisposer;


public class BasePresenter<V, R> implements IBasePresenter {
    private CompositeDisposer compositeDisposer;
    private final R repository;
    private final V view;

    public BasePresenter(@NonNull V view, @NonNull R repository) {
        this.repository = repository;
        this.view = view;
    }

    protected R getRepository() {
        return repository;
    }

    protected V getView() {
        return view;
    }

    protected CompositeDisposer getDisposer() {
        return compositeDisposer == null ? new CompositeDisposer() : compositeDisposer;
    }

    @Override
    public void onStop() {
        if (compositeDisposer != null) {
            compositeDisposer.dispose();
            compositeDisposer = null;
        }
    }
}
