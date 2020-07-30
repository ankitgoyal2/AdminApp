package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adminapp.adapters.MechanicHistoryAdapter;
import com.example.adminapp.models.Complaint;
import com.example.adminapp.models.Mechanic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MechanicProfileActivity extends AppCompatActivity {

    private static DecimalFormat df = new DecimalFormat("0.00");
    ImageView profilepic;
    RecyclerView recyclerView;
    MechanicHistoryAdapter adapter;
    TextView mechanicName,mechanicEmail,mechanicPhone,mechanicRating,ratingNumber;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference complaintReference, responsibleManReference, pendingComplaintListReference;
    List<Complaint> completedComplaintObjectList;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_profile);
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

        Mechanic mechanic = Parcels.unwrap(getIntent().getParcelableExtra("mechanic"));

        profilepic = findViewById(R.id.mechanic_profilepic);
        mechanicName = findViewById(R.id.mechanic_name);
        mechanicEmail = findViewById(R.id.mechanic_email);
        mechanicPhone = findViewById(R.id.mechanic_phone);
        mechanicRating = findViewById(R.id.mechanic_rating);
        ratingNumber = findViewById(R.id.no_of_rating);
        recyclerView = findViewById(R.id.past_record_mechanic_rv);

        mechanicName.setText(mechanic.getUserName());
        mechanicEmail.setText(mechanic.getEmail());
        float var = mechanic.getOverallRating();
        mechanicRating.setText(df.format(var));
        ratingNumber.setText(String.valueOf(mechanic.getNumberOfRating()));
        Glide.with(this)
                .load(mechanic.getProfilePicLink())
                .fitCenter()
                .placeholder(R.drawable.profilepicdemo1)
                .into(profilepic);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        completedComplaintObjectList = new ArrayList<>();
        adapter = new MechanicHistoryAdapter(this,completedComplaintObjectList);
        recyclerView.setAdapter(adapter);

        firebaseDatabase =  FirebaseDatabase.getInstance();
        responsibleManReference = firebaseDatabase.getReference("Users").child("Mechanic").child(mechanic.getUid());
        pendingComplaintListReference = responsibleManReference.child("completedComplaints");
        complaintReference = firebaseDatabase.getReference("Complaints");


        pendingComplaintListReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                complaintReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Complaint pendingComplaint = new Complaint();
                        pendingComplaint = dataSnapshot.getValue(Complaint.class);

                        completedComplaintObjectList.add(0,pendingComplaint);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}