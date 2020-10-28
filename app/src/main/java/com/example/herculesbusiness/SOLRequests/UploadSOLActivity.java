package com.example.herculesbusiness.SOLRequests;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.herculesbusiness.Adapters.ViewTradingAdapter;
import com.example.herculesbusiness.Home.HomeActivity;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.RequestsActivity;
import com.example.herculesbusiness.Trading.TradingConstants;
import com.example.herculesbusiness.Trading.UploadPDF;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UploadSOLActivity extends AppCompatActivity {
    AlertDialog dialog;
    EditText mPassword;
    Button mAuthenticate, mCancel;

    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //list to store uploads data
    List<UploadPDF> uploadList;
    final static int PICK_PDF_CODE = 2342;
    String filename;
    //these are the views
    ProgressDialog progressDialog;
    CardView upload, done;
    ViewTradingAdapter adapter;
    //the firebase objects for storage and database
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    String name, email;

    AlertDialog dialog1;
    Handler handler;
    ProgressBar progressBar;
    LinearLayout no_trading;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trading_sol);

        checkInternet();
        initDialog();

        no_trading = findViewById(R.id.no_trading);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadSOLActivity.this, SOLRequestActivity.class));
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                handler.removeCallbacksAndMessages(null);
                finish();
            }
        });
        //getting firebase objects

        uploadList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_sol);
        //adding a clicklistener on listview
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        uploadList = new ArrayList<>();
        loadOrders();

    }
    void initDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UploadSOLActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_file_name, null);
        mPassword = (EditText) mView.findViewById(R.id.etPassword);
        mAuthenticate = (Button) mView.findViewById(R.id.btnLogin);
        mCancel = (Button) mView.findViewById(R.id.btnCancel);

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dialog.hide();

            }
        });

        mAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename = mPassword.getText().toString().trim();
                getPDF();
                dialog.dismiss();
            }
        });
    }
    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDf"), PICK_PDF_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void deleteFromLeisure() {
        DocumentReference documentReference = db.collection("SOL").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    documentReference.delete();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        progressDialog.hide();
    }
    private void uploadFile(Uri data) {
        progressDialog = new ProgressDialog(UploadSOLActivity.this, R.style.ProgressDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading FIle...");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mStorageReference = FirebaseStorage.getInstance().getReference();

        StorageReference sRef = mStorageReference.child(TradingConstants.STORAGE_PATH_UPLOADS_SOL + System.currentTimeMillis() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UploadPDF upload = new UploadPDF(filename, uri.toString(), DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime()));
                                DatabaseReference request ;
                                request = database.getReference(name).child("SOL");
                                request.child(String.valueOf(System.currentTimeMillis())).setValue(upload);
                                Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                                deleteFromLeisure();
                                progressDialog.dismiss();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(progress);
                    }
                });

    }
    private void loadOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name).child("SOL");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UploadPDF newOrderModel = ds.getValue(UploadPDF.class);
                    uploadList.add(newOrderModel);
                    adapter = new ViewTradingAdapter(uploadList, UploadSOLActivity.this);
                    recyclerView.setAdapter(adapter);

                }

                progressBar.setVisibility(View.GONE);
                if (uploadList.size() == 0) {
                    no_trading.setVisibility(View.VISIBLE);
                } else {
                    no_trading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }
        });
    }
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(UploadSOLActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_no_internet, null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog1 = mBuilder.create();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(UploadSOLActivity.this);
                if (!isInternet) {
                    dialog1.show();
                } else {
                    dialog1.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadSOLActivity.this, SOLRequestActivity.class));
        finish();
        handler.removeCallbacksAndMessages(null);
    }
}