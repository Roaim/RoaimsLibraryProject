package com.roaim.roaimlib.rx;

public interface DisposableTask<T> extends Disposable {
    void run(Simple.Subscriber<T> subscriber);
}
