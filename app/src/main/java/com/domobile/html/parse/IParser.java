package com.domobile.html.parse;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maikel on 2018/3/30.
 */

public interface IParser {

    String xmlParse(String xml) throws XmlPullParserException, IOException;
    String xmlParse(InputStream xmlIs)throws XmlPullParserException, IOException ;
    String htmlParse(String html);
    String htmlparse(InputStream htmlIs);

}
