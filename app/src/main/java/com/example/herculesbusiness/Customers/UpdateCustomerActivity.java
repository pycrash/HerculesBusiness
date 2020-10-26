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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateCustomerActivity extends AppCompatActivity {
    LinearLayout root;
    Handler handler;
    AlertDialog dialog;
    ImageView back;
    TextInputLayout til_name,  til_phone, til_email, til_address, til_pincode, til_state,
            til_contact_name, til_contact_number, til_gstin;
    EditText ed_name, ed_phone, ed_email, ed_address, ed_pincode,ed_contact_name,
            ed_contact_number, ed_gstin;
    AutoCompleteTextView ed_state;
    String customer_name, customer_phone, customer_address, customer_pincode, customer_state, customer_contact_name
            , customer_number, customer_gstin;

    boolean nameCorrect = false, phoneCorrect = false, emailCorrect = false,
            addressCorrect = false, pincodeCorrect = false, stateCorrect = false, contactNameCorrect = false,
            contactPhoneCorrect = false, gstinCorrect = false;
    boolean emailexists =  false;

    TextView customer_details;
    Button btnContinue;

    LinearLayout update_customer_linear_layout;

    String[] states = {  "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh" , "Assam", "Bihar", "Chandigarh",
            "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh",
            "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
            "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal" };

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean updateCustomer = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_update);

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
        til_address = findViewById(R.id.til_address);
        til_pincode = findViewById(R.id.til_pincode);
        til_state = findViewById(R.id.til_state);
        til_contact_name = findViewById(R.id.til_contact_name);
        til_contact_number = findViewById(R.id.til_contact_number);
        til_gstin = findViewById(R.id.til_gstin);


        //Edit Texts
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_email = findViewById(R.id.ed_email);
        ed_address = findViewById(R.id.ed_address);
        ed_pincode = findViewById(R.id.ed_pincode);
        ed_contact_name = findViewById(R.id.ed_contact_name);
        ed_contact_number = findViewById(R.id.ed_contact_number);
        ed_gstin = findViewById(R.id.ed_gstin);

        //AutoComplete Text View
        ed_state = findViewById(R.id.ed_state);

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



        //onclicklistener on btnContinue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnContinue.setEnabled(false);

                boolean isInternet = CheckInternetConnection.checkInternet(UpdateCustomerActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(UpdateCustomerActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Checking Customer in database .....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DocumentReference phoneCheck = db.collection("Users").document(ed_email.getText().toString().toLowerCase());
        phoneCheck.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ed_email.setEnabled(false);
                    customer_name = documentSnapshot.getString("name");
                    customer_phone = documentSnapshot.getString("phone");
                    customer_address = documentSnapshot.getString("address");
                    customer_pincode = documentSnapshot.getString("pincode");
                    customer_state = documentSnapshot.getString("state");
                    customer_contact_name = documentSnapshot.getString("contactName");
                    customer_number = documentSnapshot.getString("contactNumber");
                    customer_gstin = documentSnapshot.getString("gstin");
                    customer_details.setText("Company Name: " + customer_name + "\n\n" +
                            "ID: " + documentSnapshot.get("mailingName") + "\n\n"+
                            "Phone: "+customer_phone  +"\n\n" +
                            "Address: " + customer_address +"\n\n" +
                            "Pincode: " + customer_pincode +"\n\n" +
                            "State: " + customer_state +"\n\n" +
                            "Contact Name: " + customer_contact_name +"\n\n" +
                            "Contact Number: " + customer_number +"\n\n" +
                            "Gstin: " + customer_gstin +"\n\n"
                    );
                    ed_name.setText(customer_name);
                    ed_phone.setText(customer_phone.replace("+91", ""));
                    ed_address.setText(customer_address);
                    ed_pincode.setText(customer_pincode);
                    ed_state.setText(customer_state);
                    ed_contact_name.setText(customer_contact_name);
                    ed_contact_number.setText(customer_number.replace("+91", ""));
                    ed_gstin.setText(customer_gstin);
                    stateCorrect = true;



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
    }

    private boolean showErrors() {

        if (nameCorrect && phoneCorrect && emailCorrect
                 && addressCorrect && pincodeCorrect && stateCorrect && contactNameCorrect &&
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


    }


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void updateCustomer() {
        checkErrors();

        if (!showErrors()) {
            btnContinue.setEnabled(true);
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(UpdateCustomerActivity.this, R.style.ProgressDialog);
            progressDialog.setMessage("Updating customer .....");
            progressDialog.setCancelable(false);
            progressDialog.show();


            final Map<String, Object> User = new HashMap<>();
            User.put("name", ed_name.getText().toString());
            User.put("email", ed_email.getText().toString().toLowerCase());
            User.put("phone", "+91" + ed_phone.getText().toString());
            User.put("address", ed_address.getText().toString());
            User.put("pincode", ed_pincode.getText().toString());
            User.put("state", ed_state.getText().toString());
            User.put("contactName", ed_contact_name.getText().toString());
            User.put("contactNumber", "+91" + ed_contact_number.getText().toString());
            User.put("gstin", ed_gstin.getText().toString());


            DocumentReference documentReference = db.collection("Users").document(ed_email.getText().toString().toLowerCase());
            documentReference.update(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Updated customer", Toast.LENGTH_SHORT).show();
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
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(UpdateCustomerActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(UpdateCustomerActivity.this);
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