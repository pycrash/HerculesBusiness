package com.example.herculesbusiness.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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

import com.example.herculesbusiness.Customers.AddCustomerActivity;
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

public class AddAdminActivity extends AppCompatActivity {
    LinearLayout root;

    ImageView back;
    TextInputLayout til_name,  til_phone, til_email, til_password, till_reenter;
    EditText ed_name, ed_phone, ed_email, ed_password, ed_reenter;
    TextView password_visibility, reenter_password_visibility;

    boolean passwordVisible = false, renterPasswordVisible = false;
    boolean nameCorrect = false, phoneCorrect = false, emailCorrect = false, passwordCorrect = false, reenterCorrect = false;
    boolean emailexists =  false;
    androidx.appcompat.app.AlertDialog dialog;
    Handler handler;
    Button btnContinue;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
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

        //Text Input Layout
        til_name = findViewById(R.id.til_name);
        til_phone = findViewById(R.id.til_phone);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        till_reenter = findViewById(R.id.till_reenter);


        //Edit Texts
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_reenter = findViewById(R.id.ed_reenter);

        //TextViews
        password_visibility = findViewById(R.id.password_visibility);
        reenter_password_visibility = findViewById(R.id.reenter_password_visibility);

        //Buttons
        btnContinue = findViewById(R.id.buttonContinue);



        // Adding Text Watchers on all
        addTextWatchers();

        //handling Password visibility
        passwordVisibleOptions();


        //onclicklistener on btnContinue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnContinue.setEnabled(false);
                checkErrors();

                if (!showErrors()) {
                    btnContinue.setEnabled(true);
                    return;
                }
                boolean isInternet = CheckInternetConnection.checkInternet(AddAdminActivity.this);
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    return;
                }
                checkEmailInDatabase();
            }
        });



    }

    private boolean checkEmailInDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(AddAdminActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Checking Customer in database .....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DocumentReference phoneCheck = db.collection("Admin").document(ed_email.getText().toString().toLowerCase());
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    emailexists = true;
                    til_email.setError("This email is associated with another account");
                    btnContinue.setEnabled(false);
                    progressDialog.hide();
                } else {
                    progressDialog.hide();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddAdminActivity.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("Add the following admin :\n" + ed_name.getText().toString() + "\n" + ed_phone.getText().toString() + "\n"
                            + ed_email.getText().toString() + "\n\n" + "Continue ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadData();
                        }
                    }).setNegativeButton("Review Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            btnContinue.setEnabled(true);
                        }
                    });
                    builder.setCancelable(false);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });;
        return emailexists;
    }

    private void uploadData() {
        final ProgressDialog progressDialog = new ProgressDialog(AddAdminActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Adding customer .....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final Map<String, Object> User = new HashMap<>();
        User.put("name", ed_name.getText().toString());
        User.put("email", ed_email.getText().toString().toLowerCase());
        User.put("phone", "+91" + ed_phone.getText().toString());
        User.put("password", ed_password.getText().toString());


        DocumentReference documentReference = db.collection("Admin").document(ed_email.getText().toString().toLowerCase());
        documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Added admin", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);

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
        if ((ed_password.getText().toString().length() > 3)) {
            passwordCorrect = true;
        }
        if ((ed_reenter.getText().toString().matches(ed_password.getText().toString()))) {
            reenterCorrect = true;
        }
    }

    private boolean showErrors() {

        if (nameCorrect && phoneCorrect && emailCorrect && passwordCorrect&& reenterCorrect) {
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
        if (!passwordCorrect) {
            til_password.setError("Password should be between 4 and 15 chars");
        }
        if (!reenterCorrect) {
            till_reenter.setError("Passwords don't match");
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

    }

    private void passwordVisibleOptions() {
        password_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!passwordVisible) {
                    passwordVisible = true;
                    password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_24), null, null, null);
                    ed_password.setTransformationMethod(null);
                    ed_password.setSelection(ed_password.getText().length());

                } else {
                    passwordVisible = false;
                    password_visibility.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_visibility_off_24), null, null, null);
                    ed_password.setTransformationMethod(new PasswordTransformationMethod());
                    ed_password.setSelection(ed_password.getText().length());

                }

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

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(AddAdminActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(AddAdminActivity.this);
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