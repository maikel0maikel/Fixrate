package com.domobile.httplib;

/**
 * Created by maikel on 2018/3/31.
 */

public class HttpBitmapRequest extends HttpRequest {
    public HttpBitmapRequest(String url) {
        super(url);
    }


    @Override
    protected BaseTask buildTask() {
        return new HttpBitmapTask(this);
    }
}
