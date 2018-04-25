package com.domobile.html.task;

import com.domobile.httplib.BaseTask;
import com.domobile.httplib.HttpRequest;

/**
 * Created by maikel on 2018/3/30.
 */

public class FackURLRequest extends HttpRequest{
    public FackURLRequest(String url) {
        super(url);
    }

    @Override
    protected BaseTask buildTask() {
        return new FackURLTask(this);
    }
}
