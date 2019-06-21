package com.roaim.roaimlib.rx;


import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public class CompositeDisposer implements Disposable {

    private boolean disposed;
    private Set<Disposable> disposables = new HashSet<>();

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        Set<Disposable> stack;
        synchronized (this) {
            if (disposed) {
                return;
            }
            disposed = true;
            stack = disposables;
            disposables = null;
        }

        dispose(stack);
    }

    private void dispose(Set<Disposable> stack) {
        if (stack == null) {
            return;
        }
        for (Disposable d : stack) {
            d.dispose();
        }
    }

    public boolean add(@NonNull Disposable disposable) {
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    Set<Disposable> stack = disposables;
                    if (stack == null) {
                        stack = new HashSet<>();
                        disposables = stack;
                    }
                    stack.add(disposable);
                    return true;
                }
            }
        }
        disposable.dispose();
        return false;
    }
}
