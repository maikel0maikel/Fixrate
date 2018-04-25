package com.domobile.html.fakeurl;

import android.graphics.Bitmap;

import com.domobile.html.BasePresenter;
import com.domobile.html.BaseView;

/**
 * Created by maikel on 2018/3/30.
 */

public interface IFackURLControl {

    interface View extends BaseView{

        void illegitimateUrl();

        void onResponseError(String msg);


        void onResponseSuccess(Bitmap result);

    }


    interface Controller extends BasePresenter{

        void start(String url);

    }

}
