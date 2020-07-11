package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.example.adminapp.R;
import com.example.adminapp.dialogbox.GenerateQRDialogBox;
import com.example.adminapp.fragments.FormFragment1;
import com.example.adminapp.fragments.FormFragment2;
import com.example.adminapp.fragments.FormFragment3;
import com.example.adminapp.models.ComparisonMachine;
import com.example.adminapp.models.Machine;
import com.example.adminapp.models.Manager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class GenerateQRActivity extends AppCompatActivity {

    EditText serviceTime, serialNumber, typeOfMachine, machineCompany, modelNumber, machinePrice,department, scrapValue, life, managerMail;// Serial Number mentioned on Machine
    TextView installationDate;

    long generationCodeValue = 0;

    String[] descriptionData = {"Basic\nDetails", "Specific\nDetails", "Commercial\nDetails"};
    private int count = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference generationCodeReference, machineReference, managerListReference, managerReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference machineQRCodeRefernce;
    String managerUid="";

    HashMap<String, Machine> myMachines;
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.TitleTextAppearance);
        final FormFragment1 fragment1 = new FormFragment1();
        setOurFragment(fragment1);
        final StepperIndicator indicator = findViewById(R.id.stepper_indicator);
        indicator.setCurrentStep(0);
//        final StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_stare_progress_id_bar);
//        stateProgressBar.setStateDescriptionData(descriptionData);
//
//        stateProgressBar.setStateDescriptionTypeface("fonts/imprima.ttf");
//        stateProgressBar.setStateNumberTypeface( "fonts/imprima.ttf");

        firebaseDatabase  = FirebaseDatabase.getInstance();
        generationCodeReference = firebaseDatabase.getReference("GenerationCode");
        machineReference = firebaseDatabase.getReference("Machines");
        managerListReference = firebaseDatabase.getReference("Users").child("Manager");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        generationCodeReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                generationCodeValue = (long) Objects.requireNonNull(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        final FloatingActionButton nextarrow =  findViewById(R.id.next_arrow);
        final FloatingActionButton prevarrow = findViewById(R.id.prev_arrow);


        nextarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                count++;
                if (count == 2) {

                    FormFragment1 formFragment1 = (FormFragment1) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment1 != null) {
                        department = formFragment1.getView().findViewById(R.id.department);
                        typeOfMachine = formFragment1.getView().findViewById(R.id.machine_type);
                    }
                    final String department1,machine_type;
                    department1 = department.getText().toString().trim();
                    machine_type = typeOfMachine.getText().toString().trim();

                    if(department1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Department", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        department.setError("Field can't be empty");
                        department.requestFocus();
                        count--;
                    }
                    else if(machine_type.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Machine Type", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        typeOfMachine.setError("");
                        typeOfMachine.requestFocus();
                        count--;
                    }
                    else {
                        indicator.setCurrentStep(1);
                        indicator.animate();
//                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        setOurFragment(new FormFragment2());
                        prevarrow.setVisibility(View.VISIBLE);
                    }

                } else if (count == 3) {
                    FormFragment2 formFragment2 = (FormFragment2) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment2 != null) {
                        serialNumber = formFragment2.getView().findViewById(R.id.serialNumber);
                        machineCompany = formFragment2.getView().findViewById(R.id.company);
                        modelNumber = formFragment2.getView().findViewById(R.id.model_number);
                        serviceTime = formFragment2.getView().findViewById(R.id.serviceTime);
                    }
                    final String serialNumber1,machineCompany1,modelNumber1,serviceTime1;
                    serialNumber1=serialNumber.getText().toString().trim();
                    machineCompany1=machineCompany.getText().toString().trim();
                    modelNumber1=modelNumber.getText().toString().trim();
                    serviceTime1=serviceTime.getText().toString().trim();

                    if(serialNumber1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Serial Number", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        serialNumber.setError("");
                        serialNumber.requestFocus();
                        count--;
                    }
                    else if(machineCompany1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Company", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        machineCompany.setError("");
                        machineCompany.requestFocus();
                        count--;
                    }
                    else if(modelNumber1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Model Number", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        modelNumber.setError("");
                        modelNumber.requestFocus();
                        count--;
                    }
                    else if(serviceTime1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter Service Time", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        serviceTime.setError("");
                        serviceTime.requestFocus();
                        count--;
                    }
                    else {
                        indicator.setCurrentStep(2);
//                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        setOurFragment(new FormFragment3());
                    }

                } else {

                    FormFragment3 formFragment3 = (FormFragment3) getSupportFragmentManager().findFragmentById(R.id.mainframe);
                    if (formFragment3 != null) {
                        managerMail = formFragment3.getView().findViewById(R.id.mail);
                        machinePrice = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.price);
                        installationDate = formFragment3.getView().findViewById(R.id.installation_date);
                        scrapValue = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.scrap_value);
                        life = Objects.requireNonNull(formFragment3.getView()).findViewById(R.id.life);
                    }
                    final String machinePrice1,installationDate1,scrapValue1,life1,managerMail1;
                    machinePrice1 = machinePrice.getText().toString().trim();
                    installationDate1=installationDate.getText().toString().trim();
                    scrapValue1=scrapValue.getText().toString().trim();
                    life1=life.getText().toString().trim();
                    managerMail1 = managerMail.getText().toString().trim();

                    if(managerMail1.isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(v,"Enter mail id of manager", Snackbar.LENGTH_LONG);
                        TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                        tv.setTypeface(font);
                        snackbar.show();
                        managerMail.setError("");
                        managerMail.requestFocus();
                        count--;
                    }
                    else
                    {
                        managerListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot manager1 : dataSnapshot.getChildren())
                                {
                                    Manager manager2 = manager1.getValue(Manager.class);
                                    if(manager2.getEmail().equals(managerMail1))
                                    {
                                        managerUid=managerUid+manager2.getUid();

                                    }
                                }
                                if(managerUid.equals(""))
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Enter correct mail id of manager", Snackbar.LENGTH_LONG);
                                    TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                                    tv.setTypeface(font);
                                    snackbar.show();
                                    managerMail.setError("");
                                    managerMail.requestFocus();
                                    count--;
                                }
                                else if(installationDate1.isEmpty())
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Enter Installation Date", Snackbar.LENGTH_LONG);
                                    TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                                    tv.setTypeface(font);
                                    snackbar.show();
                                    installationDate.setError("");
                                    installationDate.requestFocus();
                                    count--;
                                }
                                else if(machinePrice1.isEmpty())
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Enter Machine Price", Snackbar.LENGTH_LONG);
                                    TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                                    tv.setTypeface(font);
                                    snackbar.show();
                                    machinePrice.setError("");
                                    machinePrice.requestFocus();
                                    count--;
                                }
                                else if(life1.isEmpty())
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Enter Expected Life", Snackbar.LENGTH_LONG);
                                    TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                                    tv.setTypeface(font);
                                    snackbar.show();
                                    life.setError("");
                                    life.requestFocus();
                                    count--;
                                }
                                else if(scrapValue1.isEmpty())
                                {
                                    Snackbar snackbar = Snackbar.make(v,"Enter Scrap Value", Snackbar.LENGTH_LONG);
                                    TextView tv = (TextView) (snackbar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
                                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/imprima.ttf");
                                    tv.setTypeface(font);
                                    snackbar.show();
                                    scrapValue.setError("");
                                    scrapValue.requestFocus();
                                    count--;
                                }
                                else {
                                    if (generationCodeValue != 0) {

                                        // update value to database.
                                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                        try {
                                            BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(generationCodeValue), BarcodeFormat.QR_CODE, 200, 200);
                                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);// bitmap contains QRCode image.
                                            GenerateQRDialogBox generateQRDialogBox = new GenerateQRDialogBox(GenerateQRActivity.this,generationCodeValue,bitmap);
                                            generateQRDialogBox.show();

                                            //qrcode.setImageBitmap(bitmap);

                                            managerReference = firebaseDatabase.getReference("Users").child("Manager").child(managerUid);

                                            managerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    manager = dataSnapshot.getValue(Manager.class);
                                                    myMachines = manager != null ? manager.getMyMachines() :  null;
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            uploadQR(bitmap); // upload QRcode image to FirebaseStorage

                                        } catch (WriterException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(GenerateQRActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }


                                    installationDate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Calendar cal = Calendar.getInstance();
                                            int year = cal.get(Calendar.YEAR);
                                            int month = cal.get(Calendar.MONTH);
                                            int day = cal.get(Calendar.DAY_OF_MONTH);

                                            DatePickerDialog dialog = new DatePickerDialog(
                                                    GenerateQRActivity.this,
                                                    //android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                    mDateSetListener,
                                                    year, month, day);
                                            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.show();
                                        }
                                    });

                                    mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                            month = month + 1;
                                            String date = day + "/" + month + "/" + year;
                                            installationDate.setText(date);
                                        }
                                    };

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                }
            }
        });



        prevarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                if(count<=0)
                {
                    finish();
                }
                else if (count == 1) {
//                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    indicator.setCurrentStep(0);
                    setOurFragment(fragment1);
                    prevarrow.setVisibility(View.INVISIBLE);

                } else if (count == 2) {

                    indicator.setCurrentStep(1);
//                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    setOurFragment(new FormFragment2());
                } else {

                    finish();
                }
            }
        });



    }

    private void setOurFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }

    private void uploadQR(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        machineQRCodeRefernce = storageReference.child(generationCodeValue+".jpg");

        UploadTask uploadTask = machineQRCodeRefernce.putBytes(data); // QRCode image has been uploaded to Storage at this line.

        addMachineToDatabase(uploadTask); // Now retrieve URL of Image and upload Machine data to Database.


    }

    public void addMachineToDatabase(UploadTask uploadTask)
    {

        final String serialNo, dept, type, model, company;
        final float price, scrap, machineLife;
        final int servicetime;
        final String installationdate;



        // Retrieve Data of Machine to be saved.
        serialNo = serialNumber.getText().toString();
        dept = department.getText().toString();
        servicetime = Integer.parseInt(serviceTime.getText().toString());
        installationdate = installationDate.getText().toString();
        type = typeOfMachine.getText().toString();
        model = modelNumber.getText().toString();
        company = machineCompany.getText().toString();
        price = Float.parseFloat(machinePrice.getText().toString());
        scrap = Float.parseFloat(scrapValue.getText().toString());
        machineLife = Float.parseFloat(life.getText().toString());


        // QRCode image url is fetched and on Completion Machine Data is uploaded to database.
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                return machineQRCodeRefernce.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {


                    String machineId = String.valueOf(generationCodeValue);
                    Manager tempManager = null;
                    try {
                        tempManager = (Manager) manager.clone();
                    } catch (Exception ignored) {

                    }
                    Log.i("mail" , manager.getEmail().toString());
                    if (tempManager != null) {
                        tempManager.setMyMachines(null);
                    }
                    Machine machine = new Machine(serialNo, installationdate, dept, machineId, type, company, model,
                            Objects.requireNonNull(task.getResult()).toString(), servicetime, null, price, true, tempManager, null, scrap, machineLife);

                    //TODO: Check once

                    // [ ReplacementAlgo   starts -->]

                    String[] arrOfStr = installationdate.split("/", 4);
                    int  date = Integer.parseInt(arrOfStr[0]);
                    int month = Integer.parseInt(arrOfStr[1]);
                    int year = Integer.parseInt(arrOfStr[2]);

                    int serviceTime = servicetime;
                    month += serviceTime;
                    month = (month+11)%12;
                    year += month/12;
                    month %= 12;
                    float Tavg = 2*price;


                    String nextServiceDate = String.valueOf(date)+'/'+String.valueOf(month)+'/'+String.valueOf(year);
                    ComparisonMachine comparisonMachine = new ComparisonMachine(dept, type,0,nextServiceDate,manager.getUid(),Tavg,0,0,(int)price,serviceTime,machineId);

                    DatabaseReference reference1 = firebaseDatabase.getReference();
                    reference1.child("comparisonMachine").child(dept).child(type).child(machineId).setValue(comparisonMachine);
                    reference1.child("comparisonMachine").child(dept).child(type).child(machineId).child("OverallCost").child("0").setValue("0");

                    // [ ReplacementAlgo   end -->]





                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("/Machines/" + machineId, machine);

                    Machine tempMachine=null;
                    try {
                        tempMachine = (Machine) machine.clone();
                    } catch (Exception ignored) {

                    }
                    if (tempMachine != null) {
                        tempMachine.setManager(null);
                    }
                    hashMap.put("/Users/Manager/"+managerUid+"/myMachines/"+machineId,tempMachine);

                    firebaseDatabase.getReference().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(GenerateQRActivity.this, "File Updated", Toast.LENGTH_SHORT).show();
                                generationCodeValue = generationCodeValue+1; // increase Value of generationCode Everytime a new machine is entered.
                                generationCodeReference.setValue(generationCodeValue);
                            }
                        }
                    });

                }
            }
        });

    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}