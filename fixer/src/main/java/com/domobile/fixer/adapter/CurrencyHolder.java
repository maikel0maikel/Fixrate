package com.domobile.fixer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.domobile.fixer.R;

/**
 * Created by maikel on 2018/3/31.
 */

public class CurrencyHolder extends RecyclerView.ViewHolder {
    TextView mCurrencyTv;
    EditText mFixerTv;

    public CurrencyHolder(View itemView) {
        super(itemView);
        mCurrencyTv = itemView.findViewById(R.id.currency_tv);
        mFixerTv = itemView.findViewById(R.id.fixer_tv);
    }


}
