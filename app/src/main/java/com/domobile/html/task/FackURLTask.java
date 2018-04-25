package com.domobile.html.task;

import android.util.Log;

import com.domobile.httplib.HttpCodes;
import com.domobile.httplib.HttpRequest;
import com.domobile.httplib.HttpResponse;
import com.domobile.httplib.HttpStringTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by maikel on 2018/3/30.
 */

public class FackURLTask extends HttpStringTask {
    private static final String TAG = "FackURLTask";
    FackURLTask(HttpRequest request) {
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
            return new HttpResponse(null, HttpCodes.HTTP_FAILURE, "request is null");
        }
        HttpResponse response = new HttpResponse();
        if (object instanceof HttpResponse) {
            HttpResponse<String> httpResponse = (HttpResponse<String>) object;
            String html = httpResponse.getResponse();
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("link");
            if (elements.isEmpty()){
                response.setResponse(null);
                response.setCode(HttpCodes.HTTP_FAILURE);
                response.setError("can't find link tag");
                return response;
            }

            for (Element element : elements) {
                String imageUrl = element.attr("rel");
                if (imageUrl!=null&&imageUrl.startsWith("apple-touch-icon")){
                    imageUrl = element.attr("href");
                    response.setCode(HttpCodes.HTTP_OK);
                    response.setResponse(imageUrl);
                    return response;
                }
            }

            return (HttpResponse)object;
        } else {
            return null;
        }
    }
}
