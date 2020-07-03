package com.example.adminapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.adminapp.BroadcastActivity;
import com.example.adminapp.R;
import com.example.adminapp.UsersActivity;
import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.adapters.MechanicHomepageListAdapter;
import com.example.adminapp.adapters.MechanicListAdapter;
import com.example.adminapp.adapters.ViewPagerAdapter;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {


    private ViewPager viewPager;
    private int dotscount;
    private ImageView[] dots;
    Timer timer;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    LinearLayoutManager HorizontalLayout,Horizontallayout1;

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
        ImageView manageUsers = view.findViewById(R.id.manage_users);
        ImageView broadcast = view.findViewById(R.id.broadcast);

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        manageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), UsersActivity.class);
                startActivity(i);

            }
        });

        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity.getApplicationContext(), BroadcastActivity.class);
                startActivity(i);
            }
        });

        viewPager= (ViewPager) view.findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity.getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);



        //dots in viewpager
        LinearLayout sliderdotspanel = (LinearLayout) view.findViewById(R.id.slider_dots);

        dotscount=viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getActivity().getApplicationContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderdotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){

                    Activity activity = getActivity();
                    if(activity!=null)
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
                }

                Activity activity = getActivity();
                if(activity!=null)
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //timer in viewpager
        autoScroll();

        RecyclerView recyclerView_mechanic,recyclerView_manager;

        recyclerView_mechanic=view.findViewById(R.id.mechanic_list_rv);
        recyclerView_mechanic.setLayoutManager(new LinearLayoutManager(getActivity()));
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
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
        recyclerView_manager.setLayoutManager(new LinearLayoutManager(getActivity()));
        Horizontallayout1
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
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
    final long DELAY = 1000;//delay in milliseconds before auto sliding starts.
    final long PERIOD = 4000; //time in milliseconds between sliding.



    private void autoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if(viewPager.getCurrentItem()==0)
                {
                    viewPager.setCurrentItem(1);
                }
                else if(viewPager.getCurrentItem()==1)
                {
                    viewPager.setCurrentItem(0);
                }
                else
                {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        timer = new Timer(); // creating a new thread

        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY, PERIOD);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
