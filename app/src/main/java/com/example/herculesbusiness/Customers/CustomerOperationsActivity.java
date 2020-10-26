package com.example.herculesbusiness.Customers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.herculesbusiness.R;
import com.example.herculesbusiness.ViewCustomer.ViewCustomerActivity;
import com.google.gson.internal.$Gson$Preconditions;

public class CustomerOperationsActivity extends AppCompatActivity {
    Button add_customer, view_customer, delete_customer, update_customer_discount, update_password, update_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_operation);

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        add_customer= findViewById(R.id.add_customer);
        add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddCustomerActivity.class));
            }
        });

        view_customer = findViewById(R.id.view_customer);
        view_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewCustomerActivity.class));
            }
        });
        delete_customer = findViewById(R.id.delete_customer);
        delete_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeleteCustomerActivity.class));
            }
        });

        update_customer_discount = findViewById(R.id.update_discount);
        update_customer_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateDiscountActivity.class));

            }
        });

        update_password = findViewById(R.id.change_password);
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdatePasswordActivity.class));

            }
        });
        update_customer = findViewById(R.id.update_customer);
        update_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UpdateCustomerActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}