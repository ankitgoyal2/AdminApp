package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adminapp.models.Manager;
import com.example.adminapp.utility.CaesarCipherUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddEmployeeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        auth = FirebaseAuth.getInstance();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
        final String userName = "Sudhanshu Gupta",
                phoneNumber = "8813880964",
                email = "manager9@gmail.com",
                address = "haryana",
                password = "123456";

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    final DatabaseReference userReference = firebaseDatabase.getReference("Users");

                    HashMap<String, String> data = new HashMap<>();
                    data.put("claim", "manager");
                    data.put("email", user.getEmail());

                    firebaseFunctions.getHttpsCallable("setCustomClaim").
                            call(data)
                            .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(HttpsCallableResult httpsCallableResult) {

                                    HashMap<String, String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                    if (hashMap.get("status").equals("Successful")) {
                                        Manager manager = new Manager();
                                        manager.setEmail(email);
                                        manager.setUserName(userName);
                                        manager.setUid(user.getUid());
                                        manager.setPhoneNumber(phoneNumber);
                                        manager.setSavedAddress(address);
                                        userReference.child("Manager").child(user.getUid()).setValue(manager);
                                        String plainText = email + "-" + password;

                                        try {
                                            generateBarCode(plainText);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        user.delete();
                                        Toast.makeText(AddEmployeeActivity.this, "Some Error Occured \n Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });

    }


    void generateBarCode(String plainText) throws Exception {
        try {

            String encryptedString = CaesarCipherUtil.encode(plainText);
            Log.i("sudahanshu encrypted",encryptedString);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(encryptedString, BarcodeFormat.CODE_128, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            ImageView barcode = findViewById(R.id.barcode);

            barcode.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("/barcode/" + user.getUid() + ".jpg");

            UploadTask uploadTask = storageReference.putBytes(data);

        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
    }


}
