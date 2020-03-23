package com.example.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.adapters.UserListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    String type;
    public UserListFragment() {
        // Required empty public constructor
    }

    public UserListFragment(String type)
    {
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_list, container, false);
        TextView userType= view.findViewById(R.id.user_type);
        userType.setText(type);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListAdapter adapter = new UserListAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }
}
