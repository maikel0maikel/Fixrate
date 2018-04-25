package com.domobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by maikel on 2018/3/31.
 */

public class LoadingDialog extends Dialog {
    private ProgressBar mBar;
    private TextView mBodyTv;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        View rootView = getLayoutInflater().inflate(R.layout.view_dialog, null);
        mBar = rootView.findViewById(R.id.dialog_bar);
        mBodyTv = rootView.findViewById(R.id.dailog_body_tv);
        mBodyTv.setVisibility(View.GONE);
        setContentView(rootView);
    }

    public void setBody(CharSequence text) {
        if (mBodyTv != null && text != null) {
            mBodyTv.setText(text);
            mBodyTv.setVisibility(View.VISIBLE);
        }
    }

    public void setBody(int text) {
        if (mBodyTv != null) {
            mBodyTv.setText(text);
            mBodyTv.setVisibility(View.VISIBLE);
        }
    }
}
