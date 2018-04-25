package com.domobile.httplib;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by maikel on 2018/3/31.
 */

public class BaseTask implements Callable{
    private static final String TAG = "BaseTask";
    private static final int READ_TIME_OUT = 1000 * 10;
    private static final int CONNECT_TIME_OUT = READ_TIME_OUT;
    protected HttpRequest mRequest;

    protected BaseTask(HttpRequest request) {
        this.mRequest = request;
    }

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.i(TAG, "checkServerTrusted");
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Object call() throws Exception {
        if (this.mRequest == null) {
            return new HttpResponse(null, HttpCodes.HTTP_FAILURE,"request is null");
        }
        if (this.mRequest.getUrl() == null || this.mRequest.getUrl().length() == 0) {

            return new HttpResponse(null, HttpCodes.HTTP_FAILURE,"your url is empty please check");
        }
        HttpURLConnection con = null;
        HttpResponse response = new HttpResponse();
        int code = 0;
        String result = null;
        try {
            URL url = new URL(mRequest.getUrl());
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                con = https;
            } else {
                con = (HttpURLConnection) url.openConnection();
            }
            if (mRequest.getHttpMethod() == HttpMethod.GET) {
                con.setRequestMethod("GET");
            } else if (mRequest.getHttpMethod() == HttpMethod.POST) {
                con.setRequestMethod("POST");
            }
            if (mRequest.getHead() != null) {
                con.setRequestProperty(mRequest.getHead().getKey(), mRequest.getHead().getValue());
            }
            con.setConnectTimeout(CONNECT_TIME_OUT);
            con.setReadTimeout(READ_TIME_OUT);
            InputStream is = con.getInputStream();
           return  is;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = e.getMessage();
            code = HttpCodes.HTTP_FAILURE;
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
            code = HttpCodes.HTTP_FAILURE;
        }
        response.setResponse(result);
        response.setCode(code);
        return response;
    }
}
