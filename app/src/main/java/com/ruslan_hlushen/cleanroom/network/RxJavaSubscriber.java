package com.ruslan_hlushen.cleanroom.network;

import android.content.Context;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.R;

import rx.Subscriber;

/**
 * Created by Руслан on 05.01.2017.
 */
public class RxJavaSubscriber<T extends ResponseFromServer> extends Subscriber<T> {

    private static Context context;
    private OnGoodListener<T> goodListener;
    private OnErrorListener errorListener;
    private OnStartListener startListener;
    private OnEndListener endListener;


    public RxJavaSubscriber(OnGoodListener<T> goodListener) {

        this.goodListener = goodListener;
    }


    public RxJavaSubscriber(OnGoodListener<T> goodListener, OnErrorListener errorListener) {

        this.goodListener = goodListener;
        this.errorListener = errorListener;
    }


    public RxJavaSubscriber(OnGoodListener<T> goodListener,
                            OnErrorListener errorListener,
                            OnEndListener endListener) {

        this.goodListener = goodListener;
        this.errorListener = errorListener;
        this.endListener = endListener;
    }


    public RxJavaSubscriber(OnGoodListener<T> goodListener,
                            OnErrorListener errorListener,
                            OnEndListener endListener,
                            OnStartListener startListener) {

        this.goodListener = goodListener;
        this.errorListener = errorListener;
        this.endListener = endListener;
        this.startListener = startListener;
    }


    public static void setContext(Context context1) { context = context1; }


    @Override
    public void onError(Throwable e) {

        if (startListener != null) {

            startListener.onStart();
        }

        if (errorListener != null) {

            errorListener.onError();
        }

        showErrorToast(context.getResources().getString(R.string.connection_error));

        if (endListener != null) {

            endListener.onEnd();
        }
    }


    @Override
    public void onNext(T t) {

        if (startListener != null) {

            startListener.onStart();
        }

        if (t.getStatus().equals(ResponseFromServer.OK_STATUS)) {

            try {

                goodListener.onGood(t);

            } catch (Exception e) {

                if (errorListener != null) {

                    errorListener.onError();
                }
            }

        } else if (t.getStatus().equals(ResponseFromServer.ERROR_STATUS)) {

            if (errorListener != null) {

                errorListener.onError();
            }

            showErrorToast(t.getError_message());
        }

        if (endListener != null) {

            endListener.onEnd();
        }
    }


    @Override
    public void onCompleted() {

    }


    private void showErrorToast(String errorMessage) {

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }


    public interface OnGoodListener<T> {

        public void onGood(T t);
    }

    public interface OnErrorListener {

        public void onError();
    }

    public interface OnStartListener {

        public void onStart();
    }

    public interface OnEndListener {

        public void onEnd();
    }
}