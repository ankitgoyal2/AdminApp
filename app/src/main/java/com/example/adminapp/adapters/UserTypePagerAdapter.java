package com.example.adminapp.adapters;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.adminapp.R;
import com.example.adminapp.fragments.PersonalDetailsFragment;
import com.example.adminapp.fragments.UserListFragment;

public class UserTypePagerAdapter extends FragmentStateAdapter {

    public UserTypePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                UserListFragment userListFragment1 = new UserListFragment("Manager");
                return userListFragment1;
            default:
                UserListFragment userListFragment2 = new UserListFragment("Mechanic");
                return userListFragment2;

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
