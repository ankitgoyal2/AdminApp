package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.adminapp.adapters.ShowDetailsAdapter;
import com.example.adminapp.models.PastRecord;
import com.example.adminapp.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ShowDetailsAdapter showDetailsAdapter;
    String machineId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference historyReference,pastRecordsReference;
    SwipeRefreshLayout swipeRefereshLayout;
    //ShimmerFrameLayout shimmerFrameLayout;

    List<PastRecord> pastRecords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        final Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_machine_type);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);

        machineId = getIntent().getStringExtra("machine_id");

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        // shimmerFrameLayout.startShimmerAnimation();


        swipeRefereshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefereshLayout.setColorSchemeColors(Color.BLUE);

                swipeRefereshLayout.setRefreshing(false);

            }
        });

        Query baseQuery = firebaseDatabase.getReference("Machines").child(machineId).child("pastRecords");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Request> options = new DatabasePagingOptions.Builder<Request>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Request.class)
                .build();

        showDetailsAdapter = new ShowDetailsAdapter(options, ShowDetailsActivity.this);
        recyclerView.setAdapter(showDetailsAdapter);
        showDetailsAdapter.startListening();



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}