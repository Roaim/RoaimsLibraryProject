package com.roaim.roaimlib.rx;

import android.os.Handler;
import android.os.Looper;

public class EmitableTask<T> implements DisposableTask<T> {
    private static final String ERROR_MSG_SINGLE_DATA_NULL = "Simple AuthorityData null";

    private boolean isRunning;
    private Simple.Emitable<T> callable;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private Runnable uiRunnable;
    private Handler bgHandler = new Handler();
    private Runnable bgRunnable;

    EmitableTask(Simple.Emitable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void dispose() {
        if (uiRunnable != null) {
            uiHandler.removeCallbacks(uiRunnable);
        }
        if (bgRunnable != null) {
            bgHandler.removeCallbacks(bgRunnable);
        }
        callable = null;
        isRunning = false;
    }

    @Override
    public void run(Simple.Subscriber<T> subscriber) {
        if (callable != null && !isRunning) {
            execute(subscriber);
        }
    }

    private void execute(final Simple.Subscriber<T> subscriber) {
        isRunning = true;
        bgRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    callable.call(new Simple.Emitter<T>() {
                        @Override
                        public void onEmit(final T data) {
                            uiRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    subscriber.onSuccess(data);
                                }
                            };
                            uiHandler.post(uiRunnable);
                        }

                        @Override
                        public void onError(final String msg) {
                            uiRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    subscriber.onError(msg);
                                }
                            };
                            uiHandler.post(uiRunnable);
                        }
                    });
                } catch (Simple.Error error) {
                    subscriber.onError(error.getMessage());
                }
            }
        };
        bgHandler.post(bgRunnable);
    }
}
