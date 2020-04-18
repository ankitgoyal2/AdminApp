package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class BroadcastActivity extends AppCompatActivity {

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

//        final TextView broadcastMessage = findViewById(R.id.broadcast_message);
//        final Animation a = AnimationUtils.loadAnimation(this, R.anim.paint_animation);
//        broadcastMessage.setVisibility(View.VISIBLE);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        final MaterialCardView managerCardView  = findViewById(R.id.manager);
        managerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerCardView.setChecked(!managerCardView.isChecked());
            }
        });

        final MaterialCardView mechanicCardView  = findViewById(R.id.mechanic);
        mechanicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mechanicCardView.setChecked(!mechanicCardView.isChecked());
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //broadcastMessage.startAnimation(a);
            }
        },500);


    }
}
