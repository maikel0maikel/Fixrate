package com.zbq.focus.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zbq.focus.MyAdapterFragment;
import com.zbq.focus.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends BaseFragment {

    private View mRootView;
    private Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TestListFragment","FragmentTwo");
        if (mRootView == null){
            mRootView =  inflater.inflate(R.layout.fragment_list,container,false);
            ListView listView = mRootView.findViewById(R.id.listSeetings);
            List<String> datas = initData();
            MyAdapterFragment<String> myAdapter = new MyAdapterFragment<>(datas,mActivity);
            listView.setAdapter(myAdapter);
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();

        if (parent!=null){
            parent.removeView(mRootView);
        }
        return mRootView;
    }
    private List<String> initData(){
        List<String> list = new ArrayList<>();


        list.add("设置1");
        list.add("视频1");
        list.add("磁盘1");
        list.add("驾驶室设置1");
        list.add("模拟设置1");
        list.add("按键模拟设置1");
        list.add("软件下载1");
        list.add("投诉建议1");
        list.add("很好很好1");

        return list;
    }

    @Override
    public ViewGroup getContentGroup() {
        return (ViewGroup)  mRootView.findViewById(R.id.listSeetings);
    }
}
