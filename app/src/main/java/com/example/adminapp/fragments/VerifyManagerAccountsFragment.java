package com.example.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminapp.R;
import com.example.adminapp.adapters.ManagerVerificationRequestAdapter;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VerifyManagerAccountsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerifyManagerAccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyManagerAccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyManagerAccountsFragment newInstance(String param1, String param2) {
        VerifyManagerAccountsFragment fragment = new VerifyManagerAccountsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_verify_manager_accounts, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.manager_pending_request);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query baseQuery = firebaseDatabase.getReference("UnverifiedAccounts").child("Manager");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Manager> options = new DatabasePagingOptions.Builder<Manager>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Manager.class)
                .build();

        ManagerVerificationRequestAdapter adapter = new ManagerVerificationRequestAdapter(options,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return view;
    }
}
