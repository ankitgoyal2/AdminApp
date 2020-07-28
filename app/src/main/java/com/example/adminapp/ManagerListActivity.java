package com.example.adminapp;

import androidx.annotation.NonNull;
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
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.example.adminapp.adapters.ManagerBasicAdapter;
import com.example.adminapp.adapters.ManagerHomepageListAdapter;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ManagerListActivity extends AppCompatActivity {

    ManagerBasicAdapter managerBasicdapter;

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


        final RecyclerView recyclerView;
        recyclerView = findViewById(R.id.manager_list_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Manager");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Manager> userList = new ArrayList<Manager>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    userList.add(ds.getValue(Manager.class));
                }

                managerBasicdapter = new ManagerBasicAdapter(userList,getApplicationContext());
                recyclerView.setAdapter(managerBasicdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

                managerBasicdapter.getFilter().filter(query);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.length()==0)
                {
                    managerBasicdapter.getFilter().filter(null);

                    return true;
                }
                else {
                    managerBasicdapter.getFilter().filter(newText);
                    return true;
                }


            }




        });
        return super.onCreateOptionsMenu(menu);
    }



}