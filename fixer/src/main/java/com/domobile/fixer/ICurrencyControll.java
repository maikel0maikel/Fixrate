package com.domobile.fixer;

import android.support.v7.widget.RecyclerView;

import com.domobile.fixer.bean.Currency;


/**
 * Created by maikel on 2018/3/31.
 */

public interface ICurrencyControll {

    interface View {
        void setPresenter(Controller controller);

        void loading();

        void loadOrUpdateSuccess(boolean isUpdate);

        void loadOrUpdateFailure(String error);

        void notifyItem(Currency currency);

        void notifyDataSetChanged();

        RecyclerView.Adapter getAdapter();

        void errorInput();

        void closeSoft();
    }

    interface Controller {
        void start();

        void recalculate(String rate,String oldRat,String rateName);

        void saveOrder();

        void refresh();

        void closeRequest();
    }

}
