package com.example.herculesbusiness.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herculesbusiness.Customers.UpdateCustomerActivity;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminUpdate extends AppCompatActivity {
    LinearLayout root;
    Handler handler;
    AlertDialog dialog;
    ImageView back;
    TextInputLayout til_name,  til_phone, til_email;
    EditText ed_name, ed_phone, ed_email;
    String customer_name, customer_phone;
    boolean nameCorrect = false, phoneCorrect = false, emailCorrect = false;
    boolean emailexists =  false;

    TextView customer_details;
    Button btnContinue;

    LinearLayout update_customer_linear_layout;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean updateCustomer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);
        update_customer_linear_layout = findViewById(R.id.update_customer_linear_layout);
        update_customer_linear_layout.setVisibility(View.GONE);
        root = findViewById(R.id.root);
        checkInternet();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });
        customer_details = findViewById(R.id.customer_details);

        //Text Input Layout
        til_name = findViewById(R.id.til_name);
        til_phone = findViewById(R.id.til_phone);
        til_email = findViewById(R.id.til_email);



        //Edit Texts
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_email);



        //Buttons
        btnContinue = findViewById(R.id.buttonContinue);



        // Adding Text Watchers on all
        addTextWatchers();



        //onclicklistener on btnContinue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnContinue.setEnabled(false);

                boolean isInternet = CheckInternetConnection.checkInternet(AdminUpdate.this);
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    return;
                }
                if (!isValidEmail(ed_email.getText().toString())) {
                    til_email.setError("Enter a valid email address");
                    btnContinue.setEnabled(true);
                    return;
                }
                if (updateCustomer) {
                    ed_email.setEnabled(false);
                    updateCustomer();
                } else {
                    checkEmailInDatabase();
                }

            }
        });



    }

    private void checkEmailInDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(AdminUpdate.this, R.style.ProgressDialog);
        progressDialog.setMessage("Checking Admin in database .....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DocumentReference phoneCheck = db.collection("Admin").document(ed_email.getText().toString().toLowerCase());
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ed_email.setEnabled(false);
                    customer_name = documentSnapshot.getString("name");
                    customer_phone = documentSnapshot.getString("phone");

                    customer_details.setText("Name: " + customer_name + "\n\n" +
                            "Phone: "+customer_phone + "\n\n"
                    );
                    ed_name.setText(customer_name);
                    ed_phone.setText(customer_phone.replace("+91", ""));
                    update_customer_linear_layout.setVisibility(View.VISIBLE);
                    updateCustomer = true;
                    btnContinue.setEnabled(true);

                } else {
                    emailexists = false;
                    til_email.setError("No account found with that email !!");
                    btnContinue.setEnabled(true);
                }
                progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Log.d("No", "onSuccess: " + emailexists);
    }

    private void uploadData() {

    }

    private void checkErrors() {
        if ((ed_name.getText().toString().length() > 2)) {
            nameCorrect = true;
        }
        if (isValidEmail(ed_email.getText().toString())) {
            emailCorrect = true;
        }
        if ((ed_phone.getText().toString().length() > 9)) {
            phoneCorrect = true;
        }

    }

    private boolean showErrors() {

        if (nameCorrect && phoneCorrect && emailCorrect) {
            return true;
        }

        if (!nameCorrect) {
            til_name.setError("Enter a valid name");
        }
        if (!phoneCorrect) {
            til_phone.setError("Enter a valid phone number");
        }
        if (!emailCorrect) {
            til_email.setError("Enter a valid email address");
        }

        if (emailexists) {
            til_email.setError("This email is associated with another account");
        }
        return false;
    }

    private void addTextWatchers() {

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_name.isErrorEnabled()) {
                    til_name.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_phone.isErrorEnabled()) {
                    til_phone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_email.isErrorEnabled()) {
                    til_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(AdminUpdate.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_no_internet, null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(AdminUpdate.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }
    private void updateCustomer() {
        checkErrors();

        if (!showErrors()) {
            btnContinue.setEnabled(true);
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(AdminUpdate.this, R.style.ProgressDialog);
            progressDialog.setMessage("Updating admin .....");
            progressDialog.setCancelable(false);
            progressDialog.show();


            final Map<String, Object> User = new HashMap<>();
            User.put("name", ed_name.getText().toString());
            User.put("email", ed_email.getText().toString().toLowerCase());
            User.put("phone", "+91" + ed_phone.getText().toString());

            DocumentReference documentReference = db.collection("Admin").document(ed_email.getText().toString().toLowerCase());
            documentReference.update(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Updated admin", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    onBackPressed();
                    handler.removeCallbacksAndMessages(null);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btnContinue.setEnabled(true);
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
                }
            });

            progressDialog.hide();
        }
    }
}