package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.adminapp.adapters.MechanicHomepageListAdapter;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MechanicListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_list);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.horizontal_progress_bar).setVisibility(View.GONE);
            }
        },4000);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.mechanic_list_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

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

        MechanicHomepageListAdapter mechanicHomepageListAdapter= new MechanicHomepageListAdapter(options,this);
        recyclerView.setAdapter(mechanicHomepageListAdapter);
        mechanicHomepageListAdapter.startListening();

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}