package com.example.leaktest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private MyHandler mHandler ;
    private static class MyHandler extends  Handler{
        WeakReference<MainActivity> mActivity ;
        MyHandler(MainActivity activity){
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
    private static class DelayHandler implements Runnable{
        WeakReference<MainActivity> weakReference = null;
        DelayHandler(MainActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void run() {
            if (weakReference!=null){
                MainActivity activity = weakReference.get();
                if (activity!=null){
                    Toast.makeText(activity,"hello",Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
        mHandler.postDelayed(new DelayHandler(this),10*1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
