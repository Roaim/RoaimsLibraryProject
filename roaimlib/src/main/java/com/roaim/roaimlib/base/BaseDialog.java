package com.roaim.roaimlib.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.roaim.roaimlib.R;

public abstract class BaseDialog extends Dialog {
    private static final String TAG = "BaseDialog";
    private LayoutInflater layoutInflater;

    public BaseDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.getAttributes().windowAnimations = R.style.BaseDialog;
        }
        layoutInflater = LayoutInflater.from(context);
    }

    protected abstract int getContentView();

    protected abstract boolean getCancelable();

    public <V extends View> V getChildView(int id, Class<V> viewType) {
        return findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = layoutInflater.inflate(getContentView(), null);
        setContentView(view);
        setCancelable(getCancelable());
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow() called");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG, "dismiss() called");
        if (layoutInflater != null) {
            layoutInflater = null;
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        Log.d(TAG, "cancel() called");
    }

    @Override
    public void hide() {
        super.hide();
        Log.d(TAG, "hide() called");
    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "showAuthDialog() called");
        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
