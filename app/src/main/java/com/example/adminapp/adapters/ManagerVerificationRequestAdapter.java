package com.example.adminapp.adapters;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import com.example.adminapp.utility.CaesarCipherUtil;
import com.example.adminapp.utility.SendMail;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
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

import xyz.hasnat.sweettoast.SweetToast;

public class ManagerVerificationRequestAdapter extends FirebaseRecyclerPagingAdapter<Manager, ManagerVerificationRequestAdapter.RequestHolder> {
    private final int[] mColors = {R.color.list_color_1, R.color.list_color_2, R.color.list_color_3, R.color.list_color_4, R.color.list_color_5,
            R.color.list_color_6, R.color.list_color_7, R.color.list_color_8, R.color.list_color_9, R.color.list_color_10, R.color.list_color_11};
    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */

    Context c;

    public ManagerVerificationRequestAdapter(@NonNull DatabasePagingOptions<Manager> options, Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManagerVerificationRequestAdapter.RequestHolder viewHolder, int position, @NonNull Manager model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public ManagerVerificationRequestAdapter.RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_manager_item, parent, false);
        return new ManagerVerificationRequestAdapter.RequestHolder(view);
    }

    public class RequestHolder extends RecyclerView.ViewHolder {

        TextView name, phoneNumber, email, address, empId, department, designation;
        CardView cardView;
        Button accept, decline;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            phoneNumber = itemView.findViewById(R.id.phone_text);
            email = itemView.findViewById(R.id.email_text);
            address = itemView.findViewById(R.id.address_text);
            empId = itemView.findViewById(R.id.employee_id_text);
            department = itemView.findViewById(R.id.department_text);
            designation = itemView.findViewById(R.id.designation_text);
            cardView = itemView.findViewById(R.id.cardview);
            accept = itemView.findViewById(R.id.accept_button);
            decline = itemView.findViewById(R.id.decline_button);
        }

        public void bind(final Manager manager) {
            name.setText(manager.getUserName());
            phoneNumber.setText(manager.getPhoneNumber());
            email.setText(manager.getEmail());
            address.setText(manager.getSavedAddress());
            empId.setText(manager.getEmpId());
            department.setText(manager.getDepartment());
            designation.setText(manager.getDesignation());

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = manager.getEmail();
                    final String password = manager.getPassword();

                    final FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                final FirebaseUser[] user = {auth.getCurrentUser()};
                                final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");


                                HashMap<String, String> data = new HashMap<>();

                                data.put("claim", "manager");
                                data.put("email", user[0].getEmail());

                                FirebaseFunctions firebaseFunctions = FirebaseFunctions.getInstance();
                                firebaseFunctions.getHttpsCallable("setCustomClaim")
                                        .call(data)
                                        .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                            @Override
                                            public void onSuccess(HttpsCallableResult httpsCallableResult) {

                                                user[0] = auth.getCurrentUser();
                                                HashMap<String, String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                                if (hashMap.get("status").equals("Successful")) {

                                                    userReference.child("Manager").child(user[0].getUid()).setValue(manager);
                                                    String plainText = email + "-" + password;

                                                    try {
                                                        generateBarCode(plainText);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    sendEmail(email,manager.getUserName());
                                                    SweetToast.success(c, "SuccesFully Registered");

                                                } else {
                                                    user[0].delete();
                                                    SweetToast.error(c, "Some Error Occured \n Please try again");
                                                }
                                            }
                                        });

                            } else {
                                SweetToast.error(c, "Some Error Occured");
                            }
                        }
                    });
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }



        void generateBarCode(String plainText) throws Exception {
            try {

                String encryptedString = CaesarCipherUtil.encode(plainText);
                Log.i("sudahanshu encrypted", encryptedString);

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(encryptedString, BarcodeFormat.CODE_128, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("/barcode/" + user.getUid() + ".jpg");

                UploadTask uploadTask = storageReference.putBytes(data);

            } catch (Exception e) {
                Log.i("Error", e.getMessage());
            }
        }

        private void sendEmail(String email,String name) {
        //Getting content for email
        String subject = "Account Verification";
        String message = "Congratulations " + name+ ", \n Your Account has been verified by Admin . Now you use user credentials to log in the app.";

        //Creating SendMail object
        SendMail sm = new SendMail(c, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
    }
}
