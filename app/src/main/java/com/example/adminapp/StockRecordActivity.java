package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;

import com.example.adminapp.adapters.MachineAdapter;
import com.example.adminapp.adapters.ManagerBasicAdapter;
import com.example.adminapp.adapters.StockRecordAdpater;
import com.example.adminapp.models.Machine;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Tag;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yalantis.filter.animator.FiltersListItemAnimator;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockRecordActivity extends AppCompatActivity implements FilterListener<Tag> {

    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    private int[] mColors;
    private String[] mTitles;
    private List<Machine> mAllMachines;
    private Filter<Tag> mFilter;
    StockRecordAdpater adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_record);



        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.job_titles);

        mFilter = (Filter<Tag>) findViewById(R.id.filter);

        mFilter.setListener(this);

        final Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       getSupportActionBar().setDisplayUseLogoEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_home_toolbar);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.horizontal_progress_bar).setVisibility(View.GONE);
            }
        },4000);

        recyclerView = findViewById(R.id.stock_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Machines");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Machine> machineList = new ArrayList<Machine>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    machineList.add(ds.getValue(Machine.class));
                }

                adapter = new StockRecordAdpater(machineList,StockRecordActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setItemAnimator(new FiltersListItemAnimator());


    }

    private List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Tag(mTitles[i], mColors[i]));
        }
        return tags;
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

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.getFilter().filter(query);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.length()==0)
                {
                    adapter.getFilter().filter(null);

                    return true;
                }
                else {
                    adapter.getFilter().filter(newText);
                    return true;
                }


            }




        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFilterDeselected(Tag tag) {

    }

    @Override
    public void onFilterSelected(Tag tag) {

    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> arrayList) {

    }

    @Override
    public void onNothingSelected() {
        if (recyclerView != null) {
            adapter.setMachine(mAllMachines);
            adapter.notifyDataSetChanged();
        }
    }
    private List<Machine> findByTags(List<Tag> tags) {
        List<Machine> machines = new ArrayList<>();

        for (Machine machine : mAllMachines) {
            for (Tag tag : tags) {

            }
        }

        return machines;
    }
}