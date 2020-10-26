package com.example.herculesbusiness.NewOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DetailedNewOrderActivity extends AppCompatActivity {
    public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
            pincode, discount, amount, new_amount, gstin, email;
    public CardView done_order, approve;
    RecyclerView recyclerView;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_new_order);

        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        OrderModel pendingOrderModel = (OrderModel) getIntent().getSerializableExtra("orderDetails");
        orderid = findViewById(R.id.new_order_id);
        date = findViewById(R.id.new_order_date);
        name = findViewById(R.id.new_order_name);
        contact_name = findViewById(R.id.new_order_contact_name);
        mailing_name = findViewById(R.id.new_order_mailing_name);
        phone = findViewById(R.id.new_order_number);
        contact_phone = findViewById(R.id.new_order_contact_number);
        address = findViewById(R.id.new_order_address);
        state = findViewById(R.id.new_order_state);
        pincode = findViewById(R.id.new_order_pincode);
        discount = findViewById(R.id.new_order_discount);
        amount = findViewById(R.id.new_order_price);
        new_amount = findViewById(R.id.new_order_new_price);
        status = findViewById(R.id.pending_order_status);
        gstin = findViewById(R.id.new_order_gstin);
        done_order = findViewById(R.id.pending_order_done);
        recyclerView = findViewById(R.id.new_order_items_recycler_view);
        email = findViewById(R.id.new_order_email);
        status = findViewById(R.id.pending_order_status);
        approve = findViewById(R.id.approve);


        orderid.setText(pendingOrderModel.getOrderID());
        date.setText(pendingOrderModel.getDate());
        name.setText(pendingOrderModel.getName());
        contact_name.setText(pendingOrderModel.getContactName());
        mailing_name.setText(pendingOrderModel.getMailingName());
        phone.setText(pendingOrderModel.getPhone());
        email.setText(pendingOrderModel.getEmail());
        contact_phone.setText(pendingOrderModel.getContactNumber());
        address.setText(pendingOrderModel.getAddress());
        state.setText(pendingOrderModel.getState());
        pincode.setText(pendingOrderModel.getPincode());
        discount.setText(pendingOrderModel.getDiscount());
        amount.setText(pendingOrderModel.getTotal());
        amount.setPaintFlags(amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        new_amount.setText(pendingOrderModel.getNewTotal());
        gstin.setText(pendingOrderModel.getGstin());
        status.setText(pendingOrderModel.getStatus());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedNewOrderActivity.this));
        ProductAdapter cartAdapter = new ProductAdapter(pendingOrderModel.getCart(), DetailedNewOrderActivity.this);
        recyclerView.setAdapter(cartAdapter);
        done_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternet = CheckInternetConnection.checkInternet(getApplicationContext());
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                ProgressDialog dialog = new ProgressDialog(DetailedNewOrderActivity.this, R.style.ProgressDialog);
                dialog.setMessage("Working on it !!");
                dialog.setCancelable(false);
                dialog.show();
                OrderModel pendingOrderModel = (OrderModel) getIntent().getSerializableExtra("orderDetails");
                FirebaseDatabase db = FirebaseDatabase.getInstance();


                DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders");
                databaseReference.child(orderid.getText().toString()).setValue(pendingOrderModel);

                Map<String, Object> user = new HashMap<>();
                user.put("status", "Approved");
                databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
                databaseReference.updateChildren(user);

                 databaseReference = db.getReference("Requests").child("Pending Orders").child(orderid.getText().toString());
                databaseReference.updateChildren(user);

                databaseReference = db.getReference("Requests").child("New Orders").child(orderid.getText().toString());
                databaseReference.removeValue();

                databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("New Orders").child(orderid.getText().toString());
                databaseReference.removeValue();

                databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Pending Orders");
                databaseReference.child(orderid.getText().toString()).setValue(pendingOrderModel);

                databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Pending Orders").child(orderid.getText().toString());
                databaseReference.updateChildren(user);

                Toast.makeText(getApplicationContext(), "Order Approved", Toast.LENGTH_SHORT).show();
                dialog.hide();
                onBackPressed();
                finish();
            }
        });
    }
}
