package com.roaim.roaimlib.rx;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import retrofit2.Call;

public class Simple<T> implements Disposable {
    private DisposableTask<T> mTask;

    private Simple(DisposableTask<T> task) {
        this.mTask = task;
    }

    public static <T> Simple<T> just(final T data) {
        return from(new Callable<T>() {
            @Override
            public T call() throws Error {
                return data;
            }
        });
    }

    public static <T> Simple<T> error(final String errorMsg) {
        return from(new Callable<T>() {
            @Override
            public T call() throws Error {
                throw new java.lang.Error(errorMsg);
            }
        });
    }

    public static <T> Simple<T> from(@NonNull Call<T> apiCall) {
        return new Simple<>(new ApiTask<>(apiCall));
    }

    public static <T> Simple<T> from(@NonNull Callable<T> callable) {
        return new Simple<>(new BackgroundTask<>(callable));
    }

    public static <T> Simple<T> create(@Nullable Emitable<T> emitable) {
        return new Simple<>(new EmitableTask<>(emitable));
    }

    public <R> Simple<R> map(@NonNull Mapper<R, T> mapper) {
        return new Simple<>(new MapTask<R, T>(mTask, mapper));
    }

    public Simple<T> subscribe(Simple.Subscriber<T> subscriber) {
        if (mTask != null) {
            mTask.run(subscriber);
        }
        return this;
    }

    @Override
    public void dispose() {
        if (mTask != null) {
            mTask.dispose();
            mTask = null;
        }
    }

    public interface Emitter<T> {
        void onEmit(T data);
        void onError(String msg);
    }

    public interface Subscriber<T> {
        void onSuccess(T data);

        void onError(String msg);
    }

    public interface Callable<T> {
        T call() throws Error;
    }

    public interface Emitable<T> {
        void call(Emitter<T> emitter) throws Error;
    }

    public interface Mapper<R, T> {
        R map(T response) throws Error;
    }

    public static class Error extends Exception {
        public Error(String message) {
            super(message);
        }
    }
}
