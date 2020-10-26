package com.example.herculesbusiness.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.herculesbusiness.Customers.AddCustomerActivity;
import com.example.herculesbusiness.Customers.DeleteCustomerActivity;
import com.example.herculesbusiness.Customers.UpdateCustomerActivity;
import com.example.herculesbusiness.Customers.UpdateDiscountActivity;
import com.example.herculesbusiness.Customers.UpdatePasswordActivity;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.ViewCustomer.ViewCustomerActivity;

public class AdminOperationsActivity extends AppCompatActivity {
    Button add_customer, view_customer, delete_customer, update_customer_discount, update_password, update_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_operations);

        add_customer= findViewById(R.id.add_customer);
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddAdminActivity.class));
            }
        });

        view_customer = findViewById(R.id.view_customer);
        view_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewAdminsActivity.class));
            }
        });
        delete_customer = findViewById(R.id.delete_customer);
        delete_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteAdminActivity.class));
            }
        });


        update_password = findViewById(R.id.change_password);
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateAdminPassword.class));

            }
        });
        update_customer = findViewById(R.id.update_customer);
        update_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminUpdate.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}