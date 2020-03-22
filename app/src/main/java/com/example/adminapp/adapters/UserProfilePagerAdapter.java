package com.example.adminapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.adminapp.fragments.MachinesFragment;
import com.example.adminapp.fragments.PersonalDetailsFragment;
import com.example.adminapp.fragments.UserListFragment;

public class UserProfilePagerAdapter extends FragmentStateAdapter {

    public UserProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position)
        {
            case 0:
                return new MachinesFragment();
            case 1:
                return new PersonalDetailsFragment();
            default:
                return new UserListFragment();
        }
    }



    @Override
    public int getItemCount() {

        return 3;
    }
}
