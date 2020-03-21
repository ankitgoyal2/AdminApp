package com.example.adminapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.adminapp.fragments.MachinesFragment;
import com.example.adminapp.fragments.PersonalDetailsFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new MachinesFragment();
            default:
                return new PersonalDetailsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
