package com.domobile.html.utils;

import java.util.regex.Pattern;

/**
 * Created by maikel on 2018/3/31.
 */

public class URLPattern {
    private static final String REGEX = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    private URLPattern() {
    }

    /**
     * 判断是否为url
     *
     * @param url 要判断的url
     * @return true为url
     */
    public static boolean isURL(String url) {
        Pattern pattern = Pattern.compile(REGEX);

        return pattern.matcher(url).matches();
    }

}
