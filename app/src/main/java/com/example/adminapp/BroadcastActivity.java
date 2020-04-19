package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;

public class BroadcastActivity extends AppCompatActivity {

    ImageView back;
    EditText subject,message;
    Button send;
    String receiver;

    FirebaseFunctions firebaseFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

//        final TextView broadcastMessage = findViewById(R.id.broadcast_message);
//        final Animation a = AnimationUtils.loadAnimation(this, R.anim.broadcast_slide_fade);
//        broadcastMessage.setVisibility(View.VISIBLE);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        back = findViewById(R.id.back);

        firebaseFunctions = FirebaseFunctions.getInstance();

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
                receiver = "manager";
            }
        });

        final MaterialCardView mechanicCardView  = findViewById(R.id.mechanic);
        mechanicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mechanicCardView.setChecked(!mechanicCardView.isChecked());
                receiver = "mechanic";
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //broadcastMessage.startAnimation(a);
            }
        },500);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String subjectText = subject.getText().toString();
                final String messageText = message.getText().toString();

                HashMap<String,String> data = new HashMap<>();
                data.put("subject",subjectText);
                data.put("message",messageText);
                if(receiver==null){
                    Toast.makeText(BroadcastActivity.this, "Please select Receiver", Toast.LENGTH_SHORT).show();
                    return;
                }
                data.put("receiver",receiver);

                firebaseFunctions.getHttpsCallable("broadcastMessage")
                        .call(data)
                        .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                            @Override
                            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                HashMap<String,String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                if(hashMap.get("status").equals("successful")){
                                    Log.d("successful","successfully sent");
                                    Toast.makeText(BroadcastActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Log.d("error occured","error");
                                    Toast.makeText(BroadcastActivity.this, "some error occured", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }
}
