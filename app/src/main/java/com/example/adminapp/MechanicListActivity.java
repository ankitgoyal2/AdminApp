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

import com.example.adminapp.adapters.ManagerBasicAdapter;
import com.example.adminapp.adapters.MechanicBasicAdapter;
import com.example.adminapp.adapters.MechanicHomepageListAdapter;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MechanicListActivity extends AppCompatActivity {

    MechanicBasicAdapter mechanicBasicAdapter;

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


        final RecyclerView recyclerView;
        recyclerView = findViewById(R.id.mechanic_list_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Mechanic");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Mechanic> userList = new ArrayList<Mechanic>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    userList.add(ds.getValue(Mechanic.class));
                }

                mechanicBasicAdapter = new MechanicBasicAdapter(userList,getApplicationContext());
                recyclerView.setAdapter(mechanicBasicAdapter);
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

                if(query.length()==0)
                {
                    mechanicBasicAdapter.getFilter().filter(null);
                }
                else
                mechanicBasicAdapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.length()==0)
                {
                    mechanicBasicAdapter.getFilter().filter(null);

                    return true;
                }
                else {
                    mechanicBasicAdapter.getFilter().filter(newText);
                    return true;
                }


            }




        });
        return super.onCreateOptionsMenu(menu);
    }

}