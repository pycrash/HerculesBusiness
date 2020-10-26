package com.example.herculesbusiness.Customers;

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

import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {

    LinearLayout root;
    Handler handler;
    AlertDialog dialog;

    ImageView back;
    TextInputLayout til_name,  til_phone, til_email, til_password, till_reenter, til_mailing_name, til_address, til_pincode, til_state,
            til_contact_name, til_contact_number, til_gstin, til_discount;
    EditText ed_name, ed_phone, ed_email, ed_password, ed_reenter, ed_mailing_name, ed_address, ed_pincode,ed_contact_name,
            ed_contact_number, ed_gstin, ed_discount;
    AutoCompleteTextView ed_state;
    TextView password_visibility, reenter_password_visibility;

    boolean passwordVisible = false, renterPasswordVisible = false;
    boolean nameCorrect = false, phoneCorrect = false, emailCorrect = false, passwordCorrect = false, reenterCorrect = false,
    mailingNameCorrect = false, addressCorrect = false, pincodeCorrect = false, stateCorrect = false, contactNameCorrect = false,
    contactPhoneCorrect = false, gstinCorrect = false, discountCorrect = false;
    boolean emailexists =  false;

    Button btnContinue;

    String[] states = {  "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh" , "Assam", "Bihar", "Chandigarh",
            "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
            "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal" };

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_add);

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
        til_mailing_name = findViewById(R.id.til_mailing_name);
        til_address = findViewById(R.id.til_address);
        til_pincode = findViewById(R.id.til_pincode);
        til_state = findViewById(R.id.til_state);
        til_contact_name = findViewById(R.id.til_contact_name);
        til_contact_number = findViewById(R.id.til_contact_number);
        til_gstin = findViewById(R.id.til_gstin);
        til_discount = findViewById(R.id.til_discount);


        //Edit Texts
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_reenter = findViewById(R.id.ed_reenter);
        ed_mailing_name = findViewById(R.id.ed_mailing_name);
        ed_address = findViewById(R.id.ed_address);
        ed_pincode = findViewById(R.id.ed_pincode);
        ed_contact_name = findViewById(R.id.ed_contact_name);
        ed_contact_number = findViewById(R.id.ed_contact_number);
        ed_gstin = findViewById(R.id.ed_gstin);
        ed_discount = findViewById(R.id.ed_discount);

        //AutoComplete Text View
        ed_state = findViewById(R.id.ed_state);

        //TextViews
        password_visibility = findViewById(R.id.password_visibility);
        reenter_password_visibility = findViewById(R.id.reenter_password_visibility);

        //Buttons
        btnContinue = findViewById(R.id.buttonContinue);

        //Set autocomplete textView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, states);
        ed_state.setThreshold(1);//will start working from first character
        ed_state.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        ed_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_state.showDropDown();
            }
        });
        ed_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stateCorrect = true;
            }
        });

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
                boolean isInternet = CheckInternetConnection.checkInternet(AddCustomerActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Checking Customer in database .....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DocumentReference phoneCheck = db.collection("Users").document(ed_email.getText().toString().toLowerCase());
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    emailexists = true;
                    til_email.setError("This email is associated with another account");
                    btnContinue.setEnabled(true);
                    progressDialog.hide();
                } else {
                    emailexists = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomerActivity.this, R.style.MyAlertDialogStyle);
                    String discount;
                    if (ed_discount.getText().toString().length() == 0) {
                        discount = String.valueOf(0);
                    } else  {
                        discount =   ed_discount.getText().toString();
                    }
                    builder.setMessage("Add the following customer :\n" + ed_name.getText().toString() + "\n" + ed_phone.getText().toString() + "\n"
                            + ed_email.getText().toString() + "\n" + "Discount : " + discount + "\n\n" + "Continue ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.hide();
                            uploadData();
                        }
                    }).setNegativeButton("Review Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            btnContinue.setEnabled(true);
                            progressDialog.hide();
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
                progressDialog.dismiss();
                btnContinue.setEnabled(true);
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });;
        return emailexists;
    }

    private void uploadData() {
        final ProgressDialog progressDialog = new ProgressDialog(AddCustomerActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Adding customer .....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final Map<String, Object> User = new HashMap<>();
        User.put("name", ed_name.getText().toString());
        User.put("email", ed_email.getText().toString().toLowerCase());
        User.put("phone", "+91" + ed_phone.getText().toString());
        User.put("password", ed_password.getText().toString());
        User.put("mailingName", ed_mailing_name.getText().toString());
        User.put("address", ed_address.getText().toString());
        User.put("pincode", ed_pincode.getText().toString());
        User.put("state", ed_state.getText().toString());
        User.put("contactName", ed_contact_name.getText().toString());
        User.put("contactNumber","+91" + ed_contact_number.getText().toString());
        User.put("gstin", ed_gstin.getText().toString());
        if (ed_discount.getText().toString().length() == 0) {
            User.put("discount", 0);
        } else {
            User.put("discount", Integer.parseInt(ed_discount.getText().toString().trim().replaceAll("[^\\d]", "")));
        }

        DocumentReference documentReference = db.collection("Users").document(ed_email.getText().toString().toLowerCase());
        documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Added customer", Toast.LENGTH_SHORT).show();
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
        if ((ed_mailing_name.getText().toString().length() > 3)) {
            mailingNameCorrect = true;
        }
        if ((ed_address.getText().toString().length() > 9)) {
            addressCorrect = true;
        }
        if ((ed_pincode.getText().toString().length() > 5)) {
            pincodeCorrect = true;
        }
        if ((ed_contact_name.getText().toString().length() > 3)) {
            contactNameCorrect = true;
        }
        if ((ed_contact_number.getText().toString().length() > 9)) {
            contactPhoneCorrect = true;
        }
        if ((ed_gstin.getText().toString().length() > 14)) {
            gstinCorrect = true;
        }
        if ((ed_discount.getText().toString().length() > 9)) {
            discountCorrect = true;
        }




    }

    private boolean showErrors() {

        if (nameCorrect && phoneCorrect && emailCorrect && passwordCorrect&& reenterCorrect &&
                mailingNameCorrect && addressCorrect && pincodeCorrect && stateCorrect && contactNameCorrect &&
                contactPhoneCorrect && gstinCorrect) {
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
        if (!mailingNameCorrect) {
            til_mailing_name.setError("Enter a valid mailing name");
        }
        if (!addressCorrect) {
            til_address.setError("Enter a valid address");
        }
        if (!pincodeCorrect) {
            til_pincode.setError("Enter a valid pincode");
        }
        if (!stateCorrect) {
            til_state.setError("Choose a state from the list");
        }
        if (!contactNameCorrect) {
            til_contact_name.setError("Enter a valid contact name");
        }
        if (!contactPhoneCorrect) {
            til_contact_number.setError("Enter a valid phone number");
        }
        if (!gstinCorrect) {
            til_gstin.setError("GSTIN should be 15 characters long");
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
        ed_mailing_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_mailing_name.isErrorEnabled()) {
                    til_mailing_name.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_address.isErrorEnabled()) {
                    til_address.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_pincode.isErrorEnabled()) {
                    til_pincode.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stateCorrect = false;
                if (til_state.isErrorEnabled()) {
                    til_state.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_contact_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_contact_name.isErrorEnabled()) {
                    til_contact_name.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_contact_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_contact_number.isErrorEnabled()) {
                    til_contact_number.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_gstin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_gstin.isErrorEnabled()) {
                    til_gstin.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (til_discount.isErrorEnabled()) {
                    til_discount.setErrorEnabled(false);
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
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(AddCustomerActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(AddCustomerActivity.this);
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