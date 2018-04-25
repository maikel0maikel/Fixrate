package com.domobile.fixer;

import android.app.Application;
import android.content.Context;

/**
 * Created by maikel on 2018/3/31.
 */

public class FixerApplication extends Application{
    private static  Context mContext ;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static Context getmContext(){
        return mContext;
    }
}
