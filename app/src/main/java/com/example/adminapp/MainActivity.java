package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseFunctions firebaseFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFunctions = FirebaseFunctions.getInstance();
        HashMap<String,String> data = new HashMap<>();
        data.put("uid","eaehkreESngvqdt0kyqTG7vfS3A2");
        firebaseFunctions.getHttpsCallable("deleteUser")
                .call(data)
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                        Log.i("sudhanshu","User Deleted");
                    }
                });


    }
}
