package com.domobile.httplib;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by maikel on 2018/3/31.
 */

public class HttpBitmapTask extends BaseTask {
    private static final String TAG = "HttpBitmapTask";

    HttpBitmapTask(HttpRequest request) {
        super(request);
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public HttpResponse call() throws Exception {

        Object object = super.call();
        if (object == null) {
            return new HttpResponse(null,HttpCodes.HTTP_FAILURE,"server return null");
        }else if (object instanceof HttpResponse){
            HttpResponse response = (HttpResponse) object;
            return new HttpResponse(null,HttpCodes.HTTP_FAILURE,response.error);
        }
        if (object instanceof InputStream) {
            InputStream inputStream = (InputStream) object;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return new HttpResponse(bitmap,HttpCodes.HTTP_OK,null);
        } else {
            return null;
        }
    }
}
