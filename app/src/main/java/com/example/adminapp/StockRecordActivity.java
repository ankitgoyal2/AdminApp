package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
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
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.animator.FiltersListItemAnimator;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockRecordActivity extends AppCompatActivity implements FilterListener<Tag> {

    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    private int[] mColors;
    private String[] mTitles;
    private List<Machine> mAllMachines= new ArrayList<>();
    private Filter<Tag> mFilter;
    StockRecordAdpater adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_record);



        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.job_titles);

        mFilter = (Filter<Tag>) findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(this);

        mFilter.setNoSelectedItemText("All Categories");
        mFilter.build();

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
        adapter = new StockRecordAdpater(mAllMachines,StockRecordActivity.this);


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
                mAllMachines = machineList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    private void calculateDiff(final List<Machine> oldList, final List<Machine> newList) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        }).dispatchUpdatesTo(adapter);
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
    public void onFilterSelected(Tag item) {
        if (item.getText().equals(mTitles[0])) {
            mFilter.deselectAll();
            mFilter.collapse();
        }
        else {
            List<Tag> filter = new ArrayList<>();
            filter.add(item);
            List<Machine> newList = findByTags(filter);
            List<Machine> oldList = adapter. getMachine();
            adapter.setMachine(newList);
            calculateDiff(oldList, newList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Tag> filters) {
        List<Machine> newList = findByTags(filters);
        List<Machine> oldList = adapter. getMachine();
        adapter.setMachine(newList);
        calculateDiff(oldList, newList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected() {
        if (recyclerView != null) {
            adapter.setMachineAll();
            adapter.notifyDataSetChanged();
        }
    }


    private List<Machine> findByTags(List<Tag> tags) {
        List<Machine> machines = new ArrayList<>();
        List<Machine> filtermachine = new ArrayList<>();
        int flag = 0;
        boolean working,not_working;
        working=false;
        not_working =false;

        for (Machine machine : mAllMachines) {
            for (Tag tag : tags) {
                if(tag.getText().equals("All")){
                    filtermachine.addAll(mAllMachines);
                    flag =1;
                    break;
                }else if(tag.getText().equals("Working") && machine.isWorking()){
                    filtermachine.add(machine);
                    working = true;

                }else if(tag.getText().equals("Not Working") && !machine.isWorking()){
                    filtermachine.add(machine);
                    not_working = true;

                }
            }
            if(flag == 1){
                break;
            }
        }
        boolean other = false;
        if(working || not_working){
            for (Machine machine : filtermachine) {
                for (Tag tag : tags) {
                    if(tag.getText().equals("Explosive Trace Detector") && machine.getType().toLowerCase().equals("etd")){
                        machines.add(machine);
                        other = true;

                    }else if(tag.getText().equals("Bomb Detection System")&& machine.getType().toLowerCase().equals("bdds")){
                        machines.add(machine);
                        other = true;
                    }else if(tag.getText().equals("X-Ray For Baggage Scanning")&& machine.getType().toLowerCase().equals("xbis")){
                        machines.add(machine);
                        other = true;
                    }else if(tag.getText().equals("Hand Held Metal Detector")&& machine.getType().toLowerCase().equals("hhmd")){
                        machines.add(machine);
                        other = true;
                    }else if(tag.getText().equals("Door Frame Metal Detector")&& machine.getType().toLowerCase().equals("dfmd")){
                        machines.add(machine);
                        other = true;
                    }else if(tag.getText().equals("Flight Information Display System")&& machine.getType().toLowerCase().equals("fids")){
                        machines.add(machine);
                        other = true;
                    }else if(tag.getText().equals("Laptop")&& machine.getType().toLowerCase().equals("laptop")){
                        machines.add(machine);
                        other = true;
                    }
                }
            }
        }else{
            for (Machine machine : mAllMachines) {
                for (Tag tag : tags) {
                    if (tag.getText().equals("Explosive Trace Detector") && machine.getType().toLowerCase().equals("etd")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("Bomb Detection System") && machine.getType().toLowerCase().equals("bdds")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("X-Ray For Baggage Scanning") && machine.getType().toLowerCase().equals("xbis")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("Hand Held Metal Detector") && machine.getType().toLowerCase().equals("hhmd")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("Door Frame Metal Detector") && machine.getType().toLowerCase().equals("dfmd")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("Flight Information Display System") && machine.getType().toLowerCase().equals("fids")) {
                        machines.add(machine);
                        other = true;
                    } else if (tag.getText().equals("Laptop") && machine.getType().toLowerCase().equals("laptop")) {
                        machines.add(machine);
                        other = true;
                    }
                }
            }
        }
        if(!other){
            machines.addAll(filtermachine);
        }

        return machines;
    }
    class Adapter extends FilterAdapter<Tag> {

        Adapter(@NotNull List<? extends Tag> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Tag item) {
            FilterItem filterItem = new FilterItem(StockRecordActivity.this);

            filterItem.setStrokeColor(mColors[8]);
            filterItem.setTextColor(mColors[8]);
            filterItem.setCornerRadius(100);
            filterItem.setCheckedTextColor(ContextCompat.getColor(StockRecordActivity.this, android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(StockRecordActivity.this, android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }
}