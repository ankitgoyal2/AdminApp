package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adminapp.models.Mechanic;

import org.parceler.Parcels;

import java.text.DecimalFormat;

public class MechanicProfileActivity extends AppCompatActivity {

    private static DecimalFormat df = new DecimalFormat("0.00");
    ImageView profilepic;
    TextView mechanicName,mechanicEmail,mechanicPhone,mechanicRating,ratingNumber;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_profile);

        Mechanic mechanic = Parcels.unwrap(getIntent().getParcelableExtra("mechanic"));

        profilepic = findViewById(R.id.mechanic_profilepic);
        mechanicName = findViewById(R.id.mechanic_name);
        mechanicEmail = findViewById(R.id.mechanic_email);
        mechanicPhone = findViewById(R.id.mechanic_phone);
        mechanicRating = findViewById(R.id.mechanic_rating);
        ratingNumber = findViewById(R.id.no_of_rating);

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


    }
}