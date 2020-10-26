package com.example.herculesbusiness.Customers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class UpdatePasswordActivity extends AppCompatActivity {
    LinearLayout root;
    Handler handler;
    AlertDialog dialog;
    ImageView back;
    EditText email, ed_password, ed_reenter;
    TextInputLayout til_email, til_password, till_reenter;
    boolean emailCorrect = false;
    boolean emailexists =  true;
    Button btnContinue;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String customer_name, customer_phone;
    LinearLayout update_password_linear;
    TextView customer_details;
    boolean updatePassword = false;
    TextView password_visibility, reenter_password_visibility;
    boolean passwordVisible = false, renterPasswordVisible = false;
    boolean passwordCorrect = false, reenterCorrect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_update_password);
        til_email = findViewById(R.id.email_layout);
        root = findViewById(R.id.root);
        btnContinue = findViewById(R.id.buttonContinue);
        til_password = findViewById(R.id.til_password);
        till_reenter = findViewById(R.id.till_reenter);

        update_password_linear =  findViewById(R.id.update_password_linear_layout);
        update_password_linear.setVisibility(View.GONE);
        ed_password = findViewById(R.id.ed_password);
        ed_reenter = findViewById(R.id.ed_reenter);
        //TextViews
        password_visibility = findViewById(R.id.password_visibility);
        reenter_password_visibility = findViewById(R.id.reenter_password_visibility);
        customer_details = findViewById(R.id.customer_details);
        email = findViewById(R.id.email_edit);
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

        //handling Password visibility
        passwordVisibleOptions();
        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_password.isErrorEnabled()) {
                    til_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_reenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (till_reenter.isErrorEnabled()) {
                    till_reenter.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
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


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnContinue.setEnabled(false);
                boolean isInternet = CheckInternetConnection.checkInternet(UpdatePasswordActivity.this);
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    return;
                }
                if (!isValidEmail(email.getText().toString())) {
                    til_email.setError("Enter a valid email address");
                    btnContinue.setEnabled(true);
                    return;
                }
                if (updatePassword) {
                   updatePassword();
                } else {
                    checkEmailInDatabase();
                }


            }
        });


    }
    private void checkEmailInDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdatePasswordActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Checking Customer in database .....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DocumentReference phoneCheck = db.collection("Users").document(email.getText().toString().toLowerCase());
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    email.setEnabled(false);
                    customer_name = documentSnapshot.getString("name");
                    customer_phone = documentSnapshot.getString("phone");
                    customer_details.setText("Name: " + customer_name + "\n" + "Phone: "+customer_phone);
                    update_password_linear.setVisibility(View.VISIBLE);
                    updatePassword = true;
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


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void checkErrors() {


        if ((ed_password.getText().toString().length() > 3)) {
            passwordCorrect = true;
        } else {
            passwordCorrect = false;
        }
        if ((ed_reenter.getText().toString().matches(ed_password.getText().toString()))) {
            reenterCorrect = true;
        } else {
            reenterCorrect = false;
        }
    }

    private boolean showErrors() {

        if (passwordCorrect&& reenterCorrect) {
            return true;
        }
        if (!passwordCorrect) {
            til_password.setError("Password should be between 4 and 15 chars");
        }
        if (!reenterCorrect) {
            till_reenter.setError("Passwords don't match");
        }


        return false;
    }

    private void updatePassword() {
        checkErrors();

        if (!showErrors()) {
            btnContinue.setEnabled(true);
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(UpdatePasswordActivity.this, R.style.ProgressDialog);
            progressDialog.setMessage("Updating password .....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Map<String, Object> user = new HashMap<>();
            user.put("password", ed_password.getText().toString());

            DocumentReference documentReference = db.collection("Users").document(email.getText().toString().toLowerCase());
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Updated " + customer_name + "'s password", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                    handler.removeCallbacksAndMessages(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();

                }
            });
            progressDialog.hide();

        }
    }
    private void passwordVisibleOptions() {
        password_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!passwordVisible) {
                    passwordVisible = true;
                    password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_24), null, null, null);
                    ed_password.setTransformationMethod(null);

                } else {
                    passwordVisible = false;
                    password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_off_24), null, null, null);
                    ed_password.setTransformationMethod(new PasswordTransformationMethod());

                }
                ed_password.setSelection(ed_password.getText().length());

            }
        });
        reenter_password_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!renterPasswordVisible) {
                    renterPasswordVisible = true;
                    reenter_password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_24), null, null, null);
                    ed_reenter.setTransformationMethod(null);
                    ed_reenter.setSelection(ed_reenter.getText().length());

                } else {
                    renterPasswordVisible = false;
                    reenter_password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_off_24), null, null, null);
                    ed_reenter.setTransformationMethod(new PasswordTransformationMethod());
                    ed_reenter.setSelection(ed_reenter.getText().length());

                }
            }
        });
    }
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(UpdatePasswordActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(UpdatePasswordActivity.this);
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

}