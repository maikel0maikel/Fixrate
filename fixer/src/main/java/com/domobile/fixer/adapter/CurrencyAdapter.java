package com.domobile.fixer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.domobile.fixer.R;
import com.domobile.fixer.bean.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maikel on 2018/3/31.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder> {
    private LayoutInflater mInflater;
    private int index = 0;
    private List<Currency> currencyList = new ArrayList<>();
    public CurrencyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    private CurrencyInputListener inputListener;

    public CurrencyAdapter(Context context, List<Currency> currencyList) {
        mInflater = LayoutInflater.from(context);
        this.currencyList = currencyList;
    }

    public void setInputListener(CurrencyInputListener inputListener) {
        this.inputListener = inputListener;
    }

    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mInflater.inflate(R.layout.item_fixer, parent, false);

        return new CurrencyHolder(item);
    }

    @Override
    public void onBindViewHolder(CurrencyHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.mCurrencyTv.setText(currency.getName());
        holder.mFixerTv.setText(currency.getRate());
        holder.mFixerTv.setOnEditorActionListener(new EditorAction(holder.mFixerTv, position));
        holder.mFixerTv.setCursorVisible(false);
        holder.mFixerTv.setOnTouchListener(new TouchListener(holder.mFixerTv));
        holder.mFixerTv.setOnFocusChangeListener(new FocusListener(holder.mFixerTv,position));
    }

    @Override
    public int getItemCount() {
        return currencyList == null ? 0 : currencyList.size();
    }


    public void addCurrency(Currency currency) {
        if (currencyList == null) {
            currencyList = new ArrayList<>();
            realAdd(currency);
            return;
        }
        if (currencyList.contains(currency)) {
            return;
        }
        realAdd(currency);
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        if (currencyList == null) {
            this.currencyList = currencyList;
        } else {
            this.currencyList.addAll(currencyList);
        }
        notifyDataSetChanged();
    }

    private void realAdd(Currency currency) {
        currencyList.add(index, currency);
        notifyItemInserted(index);
        index++;
    }

    public void removeCurrency(int position) {
        if (currencyList.remove(position) != null) {
            notifyItemRemoved(position);
            index--;
        }
    }

    class EditorAction implements TextView.OnEditorActionListener {
        private EditText value;
        private int position;

        public EditorAction(EditText value, int position) {
            this.value = value;
            this.position = position;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Currency currency = currencyList.get(position);
                if (inputListener != null) {
                    inputListener.onDone(value.getText().toString(), currency.getRate(), currency.getName());
                }
                currency.setRate(value.getText().toString());
                notifyItemChanged(position);
                return true;
            }
            return false;
        }
    }

    class TouchListener implements View.OnTouchListener {
        private EditText value;

        public TouchListener(EditText value) {
            this.value = value;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                value.setCursorVisible(true);
                value.setSelection(value.getText().length());
            }
            return false;
        }
    }

    class FocusListener implements View.OnFocusChangeListener{
        private EditText value;
        private int position;
        public FocusListener(EditText value, int position) {
            this.value = value;
            this.position = position;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){

            }else {
                Currency currency = currencyList.get(position);
                String oldValue = currency.getRate();
                String newValue = value.getText().toString();
                if (!oldValue.equals(newValue)){
                    if (inputListener != null) {
                        inputListener.onDone(value.getText().toString(), currency.getRate(), currency.getName());
                    }
                    currency.setRate(value.getText().toString());
                    notifyItemChanged(position);
                }
            }
        }
    }

    public interface CurrencyInputListener {
        void onDone(String rate, String olderRate, String rateName);
    }
}
