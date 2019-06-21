package com.roaim.roaimlib.rx;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTask<T> implements DisposableTask<T> {
    public static final String API_ERROR_MSG_NULL_OBJECT = "Api returned null object.";

    private Call<T> apiCall;

    public ApiTask(Call<T> apiCall) {
        this.apiCall = apiCall;
    }

    @Override
    public void dispose() {
        if (apiCall != null) {
            apiCall.cancel();
            apiCall = null;
        }
    }

    @Override
    public void run(final Simple.Subscriber<T> subscriber) {
        if (apiCall != null) {
            apiCall.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    T body = response.body();
                    if (body != null) {
                        subscriber.onSuccess(body);
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            subscriber.onError(errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                            subscriber.onError(response.message());
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    subscriber.onError(t.getMessage());
                }
            });
        } else {
            subscriber.onError("API call object null");
        }

    }
}
