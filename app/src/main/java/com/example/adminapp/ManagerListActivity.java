package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class ManagerListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);

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
        recyclerView = findViewById(R.id.manager_list_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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
        ManagerHomepageListAdapter managerHomepageListAdapter = new ManagerHomepageListAdapter(options1,this);
        recyclerView.setAdapter(managerHomepageListAdapter);
        managerHomepageListAdapter.startListening();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}