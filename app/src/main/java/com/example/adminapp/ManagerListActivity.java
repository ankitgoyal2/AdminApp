package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class ManagerListActivity extends AppCompatActivity {

    ManagerHomepageListAdapter managerHomepageListAdapter;
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
       managerHomepageListAdapter = new ManagerHomepageListAdapter(options1,this);
        recyclerView.setAdapter(managerHomepageListAdapter);
        managerHomepageListAdapter.startListening();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                managerHomepageListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}