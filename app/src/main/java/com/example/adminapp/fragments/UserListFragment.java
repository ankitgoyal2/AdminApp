package com.example.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.adapters.ManagerListAdapter;
import com.example.adminapp.adapters.MechanicListAdapter;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if(type.equals("Manager"))
        {
            Query baseQuery = firebaseDatabase.getReference("Users").child("Manager");

            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPrefetchDistance(10)
                    .setPageSize(20)
                    .build();

            DatabasePagingOptions<Manager> options = new DatabasePagingOptions.Builder<Manager>()
                    .setLifecycleOwner(this)
                    .setQuery(baseQuery,config,Manager.class)
                    .build();

            ManagerListAdapter adapter = new ManagerListAdapter(options,getActivity().getApplicationContext());
            recyclerView.setAdapter(adapter);
            adapter.startListening();

        }
        else if(type.equals("Mechanic"))
        {
            Query baseQuery = firebaseDatabase.getReference("Users").child("Mechanic");

            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPrefetchDistance(10)
                    .setPageSize(20)
                    .build();

            DatabasePagingOptions<Mechanic> options = new DatabasePagingOptions.Builder<Mechanic>()
                    .setLifecycleOwner(this)
                    .setQuery(baseQuery,config,Mechanic.class)
                    .build();

            MechanicListAdapter adapter = new MechanicListAdapter(options,getActivity().getApplicationContext() );
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }

        return view;
    }
}
