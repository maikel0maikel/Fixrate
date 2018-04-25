package com.domobile.fixer;

import android.text.TextUtils;
import android.util.Log;

import com.domobile.fixer.Constants.Api;
import com.domobile.fixer.Constants.Constants;
import com.domobile.fixer.adapter.CurrencyAdapter;
import com.domobile.fixer.bean.Currency;
import com.domobile.fixer.utils.PreferenceUtils;
import com.domobile.fixer.utils.NumberDealUtils;
import com.domobile.httplib.HttpRequest;
import com.domobile.httplib.HttpStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

/**
 * Created by maikel on 2018/3/31.
 */

public class CurrencyController implements ICurrencyControll.Controller {
    private static final String TAG = "CurrencyController";

    private WeakReference<ICurrencyControll.View> mView;


    public CurrencyController(ICurrencyControll.View v) {
        mView = new WeakReference<ICurrencyControll.View>(v);
        v.setPresenter(this);
    }

    private ICurrencyControll.View getView() {
        return mView == null ? null : mView.get();
    }

    @Override
    public void start() {
        ICurrencyControll.View view = getView();
        if (view != null) {
            view.loading();
        }
        sendRequest(false);
    }

    @Override
    public void refresh() {
        ICurrencyControll.View view = getView();
        if (view != null) {
            view.loading();
        }
        sendRequest(true);
    }

    @Override
    public void closeRequest() {
    }

    private void sendRequest(final boolean isUpdate) {
        HttpStringRequest request = new HttpStringRequest(Api.API_HOST);
        request.setResponseListener(new HttpRequest.ResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "response:" + response);
                try {
                    parseString(response, isUpdate);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ICurrencyControll.View view = getView();
                    if (view != null) {
                        view.loadOrUpdateFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "error:" + error);
                ICurrencyControll.View view = getView();
                if (view != null) {
                    view.loadOrUpdateFailure(error);
                }
            }
        }).setResponseOnMainThread(true).submitRequest();
    }


    @Override
    public void recalculate(String rateSrc, String oldRateSrc, String rateName) {
        ICurrencyControll.View view = getView();
        if (view == null) {
            return;
        }
        if (!NumberDealUtils.isNumber(rateSrc)) {
            view.errorInput();
            return;
        }
        view.closeSoft();
        float oldRate = Float.parseFloat(oldRateSrc);
        float newRate = Float.parseFloat(rateSrc);
        float extendRate = newRate / oldRate;
        List<Currency> currencyList = ((CurrencyAdapter) view.getAdapter()).getCurrencyList();
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(6);
        for (Currency currency : currencyList) {
            if (currency.getName().equalsIgnoreCase(rateName)) {
                continue;
            }
            float currencyRate = Float.parseFloat(currency.getRate()) * extendRate;
            String resultRate = nf.format(currencyRate);
            currency.setRate(resultRate);
        }
        view.notifyDataSetChanged();
    }

    @Override
    public void saveOrder() {
        ICurrencyControll.View view = getView();
        if (view == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        List<Currency> currencyList = ((CurrencyAdapter) view.getAdapter()).getCurrencyList();
        for (Currency currency : currencyList) {
            builder.append(currency.getName()).append(",");
        }
        String orderResult = builder.toString();
        builder.delete(0, builder.length());
        PreferenceUtils.setPrefString(FixerApplication.getmContext(), Constants.ORDER_KEY, orderResult);
    }


    private void parseString(String json, boolean isUpdate) throws JSONException {
        ICurrencyControll.View view = getView();
        if (view == null) {
            return;
        }
        if (TextUtils.isEmpty(json)) {
            view.loadOrUpdateFailure("return empty");
            return;
        }
        JSONObject jsonObject = new JSONObject(json);
        String base = jsonObject.getString("base");
        if (TextUtils.isEmpty(base)) {
            view.loadOrUpdateFailure("base is empty");
            return;
        }
        String date = jsonObject.getString("date");
        if (isUpdate) {
            String rateUpdate = PreferenceUtils.getPrefString(FixerApplication.getmContext(), Constants.RATE_DATE_KEY, "");
            if (rateUpdate.equalsIgnoreCase(date)) {
                view.loadOrUpdateSuccess(isUpdate);
                return;
            } else {
                ((CurrencyAdapter) view.getAdapter()).getCurrencyList().clear();
            }
        } else {
            PreferenceUtils.setPrefString(FixerApplication.getmContext(), Constants.RATE_DATE_KEY, date);
        }
        String rates = jsonObject.getString("rates");
        if (TextUtils.isEmpty(rates)) {
            view.loadOrUpdateFailure("rates is empty");
            return;
        }
        JSONObject object = new JSONObject(rates);
        Iterator iterator = object.keys();
        float defaultRate = 0;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(6);
        if (!base.equalsIgnoreCase("USD")) {
            defaultRate = Float.parseFloat(object.getString("USD"));
        }
        String orderKey = PreferenceUtils.getPrefString(FixerApplication.getmContext(), Constants.ORDER_KEY, "");
        if (orderKey == null || orderKey.length() == 0) {
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                readAddItem(object, defaultRate, nf, key,view);
            }
            addBaseItem(base, defaultRate, nf,view);
        } else {
            String[] keys = orderKey.split(",");
            for (String key : keys) {
                if (key.equalsIgnoreCase(base)) {
                    addBaseItem(base, defaultRate, nf,view);
                    continue;
                }
                readAddItem(object, defaultRate, nf, key,view);
            }
        }
        view.loadOrUpdateSuccess(isUpdate);
    }

    private void addBaseItem(String base, float defaultRate, NumberFormat nf,ICurrencyControll.View view) {
        Currency currency = new Currency();
        currency.setName(base);
        currency.setRate(NumberDealUtils.subZeroAndDot(nf.format(1 / defaultRate)));
        view.notifyItem(currency);
    }

    private void readAddItem(JSONObject object, float defaultRate, NumberFormat nf, String key,ICurrencyControll.View view) {

        if (key == null || key.length() == 0) {
            return;
        }
        String value = null;
        try {
            value = object.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }
        Currency currency = new Currency();
        currency.setName(key);
        float currencyRate = Float.parseFloat(value);
        if (defaultRate != 0) {
            String result = nf.format(currencyRate / defaultRate);
            currency.setRate(NumberDealUtils.subZeroAndDot(result));
        }
        view.notifyItem(currency);
    }

}
