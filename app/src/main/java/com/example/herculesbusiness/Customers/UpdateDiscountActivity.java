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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UpdateDiscountActivity extends AppCompatActivity {
    LinearLayout root;
    Handler handler;
    AlertDialog dialog;

    ImageView back;
    EditText email, discount;
    TextInputLayout til_email;
    boolean emailCorrect = false;
    boolean emailexists =  true;
    Button btnContinue;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String customer_name, customer_phone;
    double customer_discount;
    LinearLayout update_discount_linear;
    TextView customer_details, current_discount;
    boolean updateDiscount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_update_discount);

        checkInternet();
        til_email = findViewById(R.id.email_layout);
        root = findViewById(R.id.root);
        btnContinue = findViewById(R.id.buttonContinue);
        update_discount_linear =  findViewById(R.id.update_discount_linear_layout);
        update_discount_linear.setVisibility(View.GONE);
        discount = findViewById(R.id.discount_esit);
        current_discount = findViewById(R.id.current_discount);
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
                boolean isInternet = CheckInternetConnection.checkInternet(UpdateDiscountActivity.this);
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    btnContinue.setEnabled(true);
                    return;
                }
                checkErrors();

                if (!showErrors()) {
                    btnContinue.setEnabled(true);
                    return;
                }
                if (updateDiscount) {
                    if (discount.getText().toString().length() == 0) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDiscountActivity.this, R.style.MyAlertDialogStyle);
                     builder.setMessage("There is nothing in the discount. Do you want to set the discount to 0 ?");
                     builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             updateDiscount(0);
                         }
                     }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             dialogInterface.cancel();
                             btnContinue.setEnabled(true);
                         }
                     });

                     AlertDialog alertDialog = builder.create();
                     alertDialog.show();
                    } else {
                        updateDiscount(Double.parseDouble(discount.getText().toString().trim()));
                    }
                } else {
                    checkEmailInDatabase();
                }


            }
        });


    }
    private void checkEmailInDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateDiscountActivity.this, R.style.ProgressDialog);
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
                    customer_discount = documentSnapshot.getDouble("discount");
                    customer_details.setText("Name: " + customer_name + "\n" + "Phone: "+customer_phone);
                    current_discount.setText("Current Discount: "+ customer_discount +" %");
                    update_discount_linear.setVisibility(View.VISIBLE);
                    updateDiscount = true;
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

        if (isValidEmail(email.getText().toString())) {
            emailCorrect = true;
        } else {
            emailCorrect = false;
        }
    }

    private boolean showErrors() {

        if (emailCorrect) {
            return true;
        }

        til_email.setError("Enter a valid email address");
        return false;
    }

    private void updateDiscount(double discount) {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateDiscountActivity.this, R.style.ProgressDialog);
        progressDialog.setMessage("Updating discount .....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, Object> user = new HashMap<>();
        user.put("discount", discount);

        DocumentReference documentReference = db.collection("Users").document(email.getText().toString().toLowerCase());
        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Updated "+customer_name+ "'s discount", Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();


            }
        });
        btnContinue.setEnabled(true);
        progressDialog.hide();


    }
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(UpdateDiscountActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(UpdateDiscountActivity.this);
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