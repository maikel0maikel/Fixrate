package com.domobile.html;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.domobile.html.fakeurl.FackURLController;
import com.domobile.html.fakeurl.IFackURLControl;
import com.domobile.widget.LoadingDialog;

/**
 * Created by maikel on 2018/3/31.
 */
public class MainActivity extends AppCompatActivity implements IFackURLControl.View {
    private IFackURLControl.Controller mController;
    private EditText mUrlEdittext;
    private TextView mFackUrlBtn;
    private ImageView mFackImageView;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new FackURLController(this);
    }

    private void initView() {
        mUrlEdittext = findViewById(R.id.url_input_et);
        mFackUrlBtn = findViewById(R.id.fack_url_btn);
        mFackImageView = findViewById(R.id.icon_url_iv);
        mUrlEdittext.setSelection(mUrlEdittext.getText().length());
    }

    public void fackUrl(View view) {
        showLoadingDialog();
        mFackUrlBtn.setEnabled(false);
        mController.start(mUrlEdittext.getText().toString());
    }


    @Override
    public void setPresenter(BasePresenter presenter) {
        mController = (IFackURLControl.Controller) presenter;
    }


    @Override
    public void illegitimateUrl() {
        if (!isFinishing()) {
            Toast.makeText(this, R.string.label_error_url, Toast.LENGTH_SHORT).show();
            mFackUrlBtn.setEnabled(true);
        }
        dismissLoadingDialog();
    }

    @Override
    public void onResponseError(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
            mFackUrlBtn.setEnabled(true);
        }
        dismissLoadingDialog();
    }

    @Override
    public void onResponseSuccess(Bitmap result) {
        if (!isFinishing()) {
            if (result!=null){
                mFackImageView.setImageBitmap(result);
            }else {
                Toast.makeText(this,R.string.label_can_not_get_image, Toast.LENGTH_SHORT).show();
            }
            mFackUrlBtn.setEnabled(true);
        }
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

    private void showLoadingDialog() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
        }
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    private void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
