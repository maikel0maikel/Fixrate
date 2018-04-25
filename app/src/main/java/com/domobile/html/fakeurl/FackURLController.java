package com.domobile.html.fakeurl;

import android.graphics.Bitmap;
import android.util.Log;

import com.domobile.html.task.FackURLRequest;
import com.domobile.html.utils.URLPattern;
import com.domobile.httplib.HttpBitmapRequest;
import com.domobile.httplib.HttpHead;
import com.domobile.httplib.HttpMethod;
import com.domobile.httplib.HttpRequest;
import com.domobile.httplib.HttpStringRequest;

/**
 * Created by maikel on 2018/3/31.
 */

public class FackURLController implements IFackURLControl.Controller {
    private static final String TAG = "FackURLController";

    public FackURLController(IFackURLControl.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    IFackURLControl.View mView;

    @Override
    public void start(String url) {
        if (!URLPattern.isURL(url)) {
            mView.illegitimateUrl();
            return;
        }
        final HttpRequest request = new FackURLRequest(url);
        request.setHead(new HttpHead("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.3 Mobile/14E277 Safari/603.1.30"))
                .setMethod(HttpMethod.GET).setResponseOnMainThread(true)
                .setResponseListener(new HttpRequest.ResponseListener() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d(TAG, "" + response);
                        if (response != null) {
                            String imageUrl = (String) response;
                            if (!imageUrl.startsWith("http")) {
                                imageUrl = "http:" + imageUrl;
                            }
                            getBitmap(imageUrl);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d(TAG, "" + error);
                        mView.onResponseError(error);
                    }
                }).submitRequest();

    }

    private void getBitmap(String imageUrl) {
        new HttpBitmapRequest(imageUrl).setResponseListener(new HttpRequest.ResponseListener() {
            @Override
            public void onSuccess(Object response) {
                Log.d(TAG, "response" + response);
                Bitmap bitmap = (Bitmap) response;
                mView.onResponseSuccess(bitmap);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "error" + error);
                mView.onResponseError(error);
            }
        }).setResponseOnMainThread(true).submitRequest();
    }
}
