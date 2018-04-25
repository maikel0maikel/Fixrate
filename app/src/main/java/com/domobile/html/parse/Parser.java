package com.domobile.html.parse;

import android.text.TextUtils;
import android.util.Log;

import com.domobile.httplib.HttpCodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by maikel on 2018/3/30.
 */

public class Parser implements IParser {
    private static final String TAG = "Parser";

    @Override
    public String xmlParse(String source) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(source));
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            if (!TextUtils.isEmpty(nodeName))
                if (eventType == XmlPullParser.START_TAG) {
                    if ("link".equals(nodeName)){
                        String attributeValue = parser.getAttributeValue(null, "href");
                        Log.e(TAG,""+attributeValue);
                        return attributeValue;
                    }
                }
            eventType = parser.next();
        }
        return null;
    }

    @Override
    public String xmlParse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        factory.setNamespaceAware(true);
        parser.setInput(inputStream, "utf-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            if (!TextUtils.isEmpty(nodeName))
                Log.e(TAG,"nodeName:"+nodeName);
                if (eventType == XmlPullParser.START_TAG) {
                    if ("link".equals(nodeName)){
                        String attributeValue = parser.getAttributeValue(null, "href");
                        Log.e(TAG,""+attributeValue);
                        return attributeValue;
                    }
                }
            eventType = parser.next();
        }
        return null;
    }

    @Override
    public String htmlParse(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("link");
        if (elements.isEmpty()){

            return null;
        }
        String imageUrl;
        for (Element element : elements) {
            imageUrl = element.attr("rel");
            if (imageUrl!=null&&imageUrl.startsWith("apple-touch-icon")){
                imageUrl = element.attr("href");
                return imageUrl;
            }
        }
        return null;
    }

    @Override
    public String htmlparse(InputStream htmlIs) {
        Document doc = null;
        try {
            doc = Jsoup.parse(htmlIs,"utf-8",null);
            Elements elements = doc.select("link");
            if (elements.isEmpty()){
                return null;
            }
            String imageUrl;
            for (Element element : elements) {
                imageUrl = element.attr("rel");
                if (imageUrl!=null&&imageUrl.startsWith("apple-touch-icon")){
                    imageUrl = element.attr("href");
                    return imageUrl;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
