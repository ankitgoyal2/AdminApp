 package com.example.adminapp.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.adminapp.fragments.VerifyManagerAccountsFragment;
import com.example.adminapp.fragments.VerifyMechanicAccountsFragment;


 public class TabAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private Context myContext;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                VerifyManagerAccountsFragment verifyManagerAccountsFragment = new VerifyManagerAccountsFragment();
                return verifyManagerAccountsFragment;
            case 1:
                VerifyMechanicAccountsFragment verifyMechanicAccountsFragment = new VerifyMechanicAccountsFragment();
                return verifyMechanicAccountsFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
