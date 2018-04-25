package com.domobile.httplib;

import java.util.Map;

/**
 * Created by maikel on 2018/3/31.
 */

public interface IHttpManager {

    void submmitRequest(HttpRequest request);

    void get(String url, String headKey, String headValue);

    void post(String url, Map<String, String> params);

    void post(String url, String json);

    void shutdown();

    void shutdownAll();
}
