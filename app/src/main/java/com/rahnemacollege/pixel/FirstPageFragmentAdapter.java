package com.rahnemacollege.pixel;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FirstPageFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public FirstPageFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        addFragment(new LoginFragment(), mContext.getString(R.string.firstpage_b_login));
        addFragment(new SignUpFragment(), mContext.getString(R.string.firstpage_b_signup));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void removeFragment(String title) {
        int fragmentIndex = mFragmentTitleList.indexOf(title);
        mFragmentList.remove(fragmentIndex);
        mFragmentTitleList.remove(fragmentIndex);
    }
}