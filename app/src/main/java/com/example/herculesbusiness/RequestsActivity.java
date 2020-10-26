package com.example.herculesbusiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.herculesbusiness.Customers.AddCustomerActivity;
import com.example.herculesbusiness.Customers.DeleteCustomerActivity;
import com.example.herculesbusiness.Customers.UpdateCustomerActivity;
import com.example.herculesbusiness.Customers.UpdateDiscountActivity;
import com.example.herculesbusiness.Customers.UpdatePasswordActivity;
import com.example.herculesbusiness.LeisureRequests.LeisureRequests;
import com.example.herculesbusiness.SOLRequests.SOLRequestActivity;
import com.example.herculesbusiness.ViewCustomer.ViewCustomerActivity;
import com.example.herculesbusiness.utils.LoadImagesInBitmap;

public class RequestsActivity extends AppCompatActivity {
    CardView leisure_requests, sol_requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


        leisure_requests= findViewById(R.id.leisure_requests);
        leisure_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LeisureRequests.class));
            }
        });

        sol_requests = findViewById(R.id.sol_requests);
        sol_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SOLRequestActivity.class));
            }
        });

    }
}