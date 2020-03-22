package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.w3c.dom.Text;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseFunctions firebaseFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        firebaseFunctions = FirebaseFunctions.getInstance();
//        HashMap<String,String> data = new HashMap<>();
//        data.put("uid","eaehkreESngvqdt0kyqTG7vfS3A2");
//        firebaseFunctions.getHttpsCallable("deleteUser")
//                .call(data)
//                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
//                    @Override
//                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
//                        Log.i("sudhanshu","User Deleted");
//                    }
//                });

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final TextView userName = findViewById(R.id.user_name);
        final ImageView profile = findViewById(R.id.profile);

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                Pair<View,String> p1 = Pair.create((View)userName,ViewCompat.getTransitionName(userName));
                Pair<View,String> p2 = Pair.create((View)profile,ViewCompat.getTransitionName(profile));
                ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p1,p2);
                startActivity(intent,optionsCompat.toBundle());
            }
        });



    }
}
