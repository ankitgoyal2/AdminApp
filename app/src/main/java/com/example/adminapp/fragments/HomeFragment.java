package com.example.adminapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adminapp.AddEmployeeActivity;
import com.example.adminapp.BroadcastActivity;
import com.example.adminapp.GenerateQRActivity;
import com.example.adminapp.R;
import com.example.adminapp.UsersActivity;
import com.example.adminapp.ZoomCenterCardLayoutManager;
import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.adapters.MechanicHomepageListAdapter;
import com.example.adminapp.adapters.ViewPagerAdapter;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class HomeFragment extends Fragment {




    Activity activity;
    FirebaseDatabase firebaseDatabase;
    ZoomCenterCardLayoutManager HorizontalLayout,Horizontallayout1;

    public HomeFragment() {
        activity = getActivity();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();

        ImageView generateQr = view.findViewById(R.id.generate_qr);
        ImageView addEmployee = view.findViewById(R.id.add_employee);
        ImageView broadcast = view.findViewById(R.id.broadcast);

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity.getApplicationContext(), GenerateQRActivity.class);
                startActivity(i);
            }
        });

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), AddEmployeeActivity.class);
                startActivity(i);

            }
        });
        //Image Slider
        SliderView sliderView = view.findViewById(R.id.imageSlider);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#275F73"));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), BroadcastActivity.class);
                startActivity(i);
            }
        });

        RecyclerView recyclerView_mechanic,recyclerView_manager;

        recyclerView_mechanic=view.findViewById(R.id.mechanic_list_rv);
        HorizontalLayout = new ZoomCenterCardLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_mechanic.setLayoutManager(HorizontalLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();

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

        MechanicHomepageListAdapter mechanicHomepageListAdapter= new MechanicHomepageListAdapter(options,getActivity().getApplicationContext());
        recyclerView_mechanic.setAdapter(mechanicHomepageListAdapter);
        mechanicHomepageListAdapter.startListening();

        recyclerView_manager = view.findViewById(R.id.manager_list_rv);
        Horizontallayout1 =  new ZoomCenterCardLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_manager.setLayoutManager(Horizontallayout1);
        Query baseQuery1 = firebaseDatabase.getReference("Users").child("Manager");
        PagedList.Config config1 = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Manager> options1 = new DatabasePagingOptions.Builder<Manager>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery1,config1,Manager.class)
                .build();
        ManagerHomepageListAdapter managerHomepageListAdapter = new ManagerHomepageListAdapter(options1,getActivity().getApplicationContext());
        recyclerView_manager.setAdapter(managerHomepageListAdapter);
        managerHomepageListAdapter.startListening();

        return view;
    }


}
