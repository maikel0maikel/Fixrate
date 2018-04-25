package com.example.leaktest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.leaktest.fragment.FragmentOne;
import com.example.leaktest.fragment.FragmentThree;
import com.example.leaktest.fragment.FragmentTwo;
import com.example.leaktest.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TestListFragment extends FragmentActivity {


    private List<Fragment> fragments = new ArrayList<>();

    private Fragment currentFragment;
    FragmentManager fragmentManager;
    private int currentIndex = 0;
    private static final String CURRENT_INDEX = "CURRENT_INDEX";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LogUtils.e("TestListFragment","onCreate");
        if (savedInstanceState != null) {
            FragmentOne fragmentOne = (FragmentOne) fragmentManager.findFragmentByTag("TAG" + 0);
            FragmentTwo fragmentTwo = (FragmentTwo) fragmentManager.findFragmentByTag("TAG" + 1);
            FragmentThree fragmentThree = (FragmentThree) fragmentManager.findFragmentByTag("TAG" + 2);
            fragments.add(fragmentOne);
            fragments.add(fragmentTwo);
            fragments.add(fragmentThree);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX, 0);
            restoreFragment();
        } else {
            fragments.add(new FragmentOne());
            fragments.add(new FragmentTwo());
            fragments.add(new FragmentThree());
            showFragment(0);
        }
    }

    private void restoreFragment() {
        int size = fragments.size();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < size; i++) {
            Fragment fragment = fragments.get(i);
            if (i == currentIndex) {
                transaction.show(fragment);
                currentFragment = fragment;
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    private void showFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragments.get(index);
        if (fragment == null||fragment == currentFragment) {
            return;
        }
        if (fragment.isAdded()) {
            transaction.show(fragment).hide(currentFragment);
        } else {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.content, fragment, "TAG" + index);
        }
        currentFragment = fragment;
        transaction.commit();
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.one:
                currentIndex = 0;
                break;
            case R.id.two:
                currentIndex = 1;
                break;
            case R.id.three:
                currentIndex = 2;
                break;
        }
        LogUtils.traceStack();
        showFragment(currentIndex);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, currentIndex);
    }
}
