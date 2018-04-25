package com.domobile.httplib;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by maikel on 2018/3/31.
 */

public abstract class HttpRequest {
    private static final int HTTP_RESULT = 1;
    private ResponseHandler mHandler = new ResponseHandler(Looper.getMainLooper());
    private ResponseListener listener;
    private boolean isResponseOnMainThread = false;
    private String url;
    private HttpHead mHead;
    private HttpMethod mHttpMethod = HttpMethod.GET;
    IHttpManager httpManager = HttpManagerImpl.getInstance();
    private BaseTask task ;
    private String paramsJson;
    public HttpRequest(String url) {
        this.url = url;
        task = buildTask();
    }
    public HttpRequest(String url,String params) {
        this.url = url;
        paramsJson = params;
        task = buildTask();
    }
    public HttpRequest setHead(HttpHead head) {
        mHead = head;
        return this;
    }

    public HttpHead getHead() {
        return mHead;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequest setMethod(HttpMethod method) {
        mHttpMethod = method;
        return this;
    }

    public HttpMethod getHttpMethod() {
        return mHttpMethod;
    }

    public HttpRequest setResponseOnMainThread(boolean onMainThread) {
        isResponseOnMainThread = onMainThread;
        return this;
    }

    public HttpRequest setResponseListener(ResponseListener l) {
        this.listener = l;
        mHandler.setListener(listener);
        return this;
    }

    public BaseTask getTask(){
        return task;
    }

    public void submitRequest() {
        httpManager.submmitRequest(this);
    }

    public void onResponse(HttpResponse response) {
        HttpResponse tem = response;
        if (isResponseOnMainThread) {
            mHandler.obtainMessage(HTTP_RESULT, tem).sendToTarget();
        } else {
            if (tem.getCode() == HttpCodes.HTTP_OK) {
                if (listener != null) {
                    listener.onSuccess(response.getResponse());
                }
            } else if (tem.getCode() == HttpCodes.HTTP_FAILURE) {
                listener.onFailure(response.getError());
            }
        }
    }


    protected abstract BaseTask buildTask();

    public interface ResponseListener<T> {
        void onSuccess(T response);

        void onFailure(String error);
    }

    static class ResponseHandler extends Handler {
        WeakReference<ResponseListener> listener;

        ResponseHandler(Looper looper) {
            super(looper);
        }
        public void setListener(ResponseListener listener){
            this.listener = new WeakReference<>(listener);
        }
        @Override
        public void handleMessage(Message msg) {
            HttpResponse response = (HttpResponse) msg.obj;
            if (response == null) {
                response = new HttpResponse(null, HttpCodes.HTTP_FAILURE,"server return null");
            }
            if (response.getCode() == HttpCodes.HTTP_OK) {
                if (listener != null) {
                    listener.get().onSuccess(response.getResponse());
                }
            } else if (response.getCode() == HttpCodes.HTTP_FAILURE) {
                if (listener != null) {
                    listener.get().onFailure(response.getError());
                }
            }
        }
    }

    public void shutDown(){
        httpManager.shutdownAll();
    }
}
