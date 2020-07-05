package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adminapp.adapters.MachineAdapter;
import com.example.adminapp.models.Machine;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

public class ManagerProfileActivity extends AppCompatActivity {

    ImageView profilepic;
    TextView managerName,managerEmail,managerPhone;
    RecyclerView recyclerView_machine;
    LinearLayoutManager HorizontalLayout;
    FirebaseDatabase firebaseDatabase;
    MachineAdapter machineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        profilepic = findViewById(R.id.circleImageView);
        managerName = findViewById(R.id.manager_name);
        managerEmail = findViewById(R.id.manager_email);
        managerPhone = findViewById(R.id.manager_phone);
        recyclerView_machine = findViewById(R.id.machines_manager_rv);

        Manager manager = Parcels.unwrap(getIntent().getParcelableExtra("manager"));
        managerName.setText(manager.getUserName());
        managerEmail.setText(manager.getEmail());
        managerPhone.setText(manager.getPhoneNumber());

        Glide.with(this)
                .load(manager.getProfilePicLink())
                .fitCenter()
                .placeholder(R.drawable.profilepicdemo)
                .into(profilepic);

        //Horizontal recycler view

        recyclerView_machine.setLayoutManager(new LinearLayoutManager(this));
        HorizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView_machine.setLayoutManager(HorizontalLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(manager.getUid()).child("myMachines");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Machine> options = new DatabasePagingOptions.Builder<Machine>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Machine.class)
                .build();

        machineAdapter = new MachineAdapter(options,ManagerProfileActivity.this);
        recyclerView_machine.setAdapter(machineAdapter);
        machineAdapter.startListening();

    }
}