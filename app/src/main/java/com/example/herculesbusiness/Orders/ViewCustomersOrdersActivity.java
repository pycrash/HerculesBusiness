package com.example.herculesbusiness.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.NewOrders.NewOrderCustomerAdapter;
import com.example.herculesbusiness.PendingOrders.PendingOrderCustomerAdapter;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.ViewCustomer.ViewCustomerActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomersOrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<OrderModel> list;
    ViewCustomerOrderAdapter adapter;
    LinearLayout noOrders;
    ProgressBar progressBar;
    String category, name, email;
    TextView category_text;
    public static final String TAG = "CustomersOrdersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customers);


        category = getIntent().getStringExtra("category");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        Log.d(TAG, "onCreate: category : " + category);
        Log.d(TAG, "onCreate: name : " + name);
        Log.d(TAG, "onCreate: email : " + email);

        category_text = findViewById(R.id.category);
        category_text.setText(category);
        progressBar = findViewById(R.id.progressBar);
        noOrders = findViewById(R.id.new_orders_no_orders);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.new_orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewCustomersOrdersActivity.this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        loadOrders();
    }

    private void loadOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(name).child(category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    OrderModel newOrderModel = ds.getValue(OrderModel.class);
                    list.add(newOrderModel);
                    adapter = new ViewCustomerOrderAdapter(ViewCustomersOrdersActivity.this, list, category);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
                if (list.size() == 0) {
                    noOrders.setVisibility(View.VISIBLE);
                } else {
                    noOrders.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });
    }
}