package com.roaim.roaimlib.rx;

public class MapTask<O, I> implements DisposableTask<O> {

    private DisposableTask<I> apiTask;
    private Simple.Mapper<O, I> mapper;

    public MapTask(DisposableTask<I> apiTask, Simple.Mapper<O, I> mapper) {
        this.apiTask = apiTask;
        this.mapper = mapper;
    }

    @Override
    public void dispose() {
        if (apiTask != null) {
            apiTask.dispose();
            apiTask = null;
        }
        if (mapper != null) {
            mapper = null;
        }
    }

    @Override
    public void run(final Simple.Subscriber<O> subscriber) {
        if (apiTask != null) {
            apiTask.run(new Simple.Subscriber<I>() {
                @Override
                public void onSuccess(I data) {
                    try {
                        O map = mapper.map(data);
                        subscriber.onSuccess(map);
                    } catch (Simple.Error error) {
                        subscriber.onError(error.getMessage());
                    }
                }

                @Override
                public void onError(String msg) {
                    subscriber.onError(msg);
                }
            });
        } else {
            subscriber.onError("API call object in MapTask null");
        }

    }
}
