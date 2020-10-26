package com.example.herculesbusiness.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.herculesbusiness.CompletedOrders.CompletedOrdersActivity;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.NewOrders.NewOrderCustomerAdapter;
import com.example.herculesbusiness.NewOrders.NewOrdersActivity;
import com.example.herculesbusiness.PendingOrders.PendingOrdersActivity;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.ViewCustomer.OrdersViewCustomerActivity;

import java.util.List;

public class OrdersOperationActivity extends AppCompatActivity {
    CardView newOrders, pendingOrders, completedOrders, customerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_operations);

        newOrders = findViewById(R.id.new_orders);
        pendingOrders= findViewById(R.id.pending_orders);
        completedOrders = findViewById(R.id.completed_orders);

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        newOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewOrdersActivity.class));
            }
        });
        pendingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingOrdersActivity.class));
            }
        });
        completedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CompletedOrdersActivity.class));
            }
        });
        customerView = findViewById(R.id.view_customer_orders);
        customerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrdersViewCustomerActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}