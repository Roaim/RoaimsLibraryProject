package com.roaim.roaimlib.rx;

import android.os.AsyncTask;

public class BackgroundTask<T> extends AsyncTask<String, Integer, T> implements DisposableTask<T> {
    private static final String ERROR_MSG_SINGLE_DATA_NULL = "Simple AuthorityData null";

    private String errorMessage;
    private Simple.Callable<T> callable;
    private Simple.Subscriber<T> subscriber;

    BackgroundTask(Simple.Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    protected T doInBackground(String... strings) {
        try {
            T data = callable.call();
            if (data != null) {
                return data;
            } else {
                errorMessage = ERROR_MSG_SINGLE_DATA_NULL;
            }
        } catch (Simple.Error error) {
            errorMessage = error.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (t != null) {
            if (subscriber != null) {
                subscriber.onSuccess(t);
            }
        } else {
            if (subscriber != null) {
                subscriber.onError(errorMessage);
            }
        }
    }

    @Override
    public void dispose() {
        cancel(true);
        subscriber = null;
        callable = null;
    }

    @Override
    public void run(Simple.Subscriber<T> subscriber) {
        if (callable != null && getStatus() != Status.RUNNING) {
            this.subscriber = subscriber;
            execute();
        }
    }
}
