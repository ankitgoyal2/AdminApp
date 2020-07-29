package com.example.adminapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adminapp.R;
import com.example.adminapp.adapters.ManagerListAdapter;
import com.example.adminapp.adapters.UserVerificationRequestAdapter;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VerifyAccountsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerifyAccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyAccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyAccountsFragment newInstance(String param1, String param2) {
        VerifyAccountsFragment fragment = new VerifyAccountsFragment();
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


        View view =  inflater.inflate(R.layout.fragment_verify_accounts, container, false);

        final ExpandableWeightLayout expandableLayout = (ExpandableWeightLayout) view.findViewById(R.id.expandableLayout);
        final ExpandableWeightLayout expandableLayout1 = (ExpandableWeightLayout) view.findViewById(R.id.expandableLayout1);

        final ImageView image,image1;
        image = view.findViewById(R.id.image);
        image1 = view.findViewById(R.id.image1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!expandableLayout.isExpanded())
                {
                    image.animate().rotationBy(180).setDuration(200).start();
                    expandableLayout.toggle();
                    if(expandableLayout1.isExpanded()) {
                        expandableLayout1.collapse();
                        image1.animate().rotationBy(180).start();
                    }
                }
            }
        },1000);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.animate().rotationBy(180).setDuration(200).start();
                expandableLayout.toggle();
                if(expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse();
                    image1.animate().rotationBy(180).start();
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.animate().rotationBy(180).start();
                expandableLayout1.toggle();
                if(expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                    image.animate().rotationBy(180).setDuration(200).start();
                }
            }
        });

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

        UserVerificationRequestAdapter adapter = new UserVerificationRequestAdapter(options,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return view;
    }
}
