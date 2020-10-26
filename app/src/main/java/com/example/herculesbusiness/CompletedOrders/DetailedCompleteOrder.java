package com.example.herculesbusiness.CompletedOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.PendingOrders.DetailedPendingActivity;
import com.example.herculesbusiness.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DetailedCompleteOrder extends AppCompatActivity {
    public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
            pincode, discount, amount, new_amount, gstin, email;
    public CardView done_order;
    RecyclerView recyclerView;
    TextView addNotes;
    TextView status;
    OrderModel pendingOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_complete_order);


        ImageView back;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        pendingOrderModel = (OrderModel) getIntent().getSerializableExtra("orderDetails");
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
        done_order = findViewById(R.id.done);
        addNotes = findViewById(R.id.pending_order_add_notes);
        recyclerView = findViewById(R.id.new_order_items_recycler_view);
        email = findViewById(R.id.new_order_email);
        status = findViewById(R.id.pending_order_status);


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
        addNotes.setText(pendingOrderModel.getNotes());
        status.setText(pendingOrderModel.getStatus());
        addNotes.setText(pendingOrderModel.getNotes());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedCompleteOrder.this));
        ProductAdapter cartAdapter = new ProductAdapter(pendingOrderModel.getCart(), DetailedCompleteOrder.this);
        recyclerView.setAdapter(cartAdapter);
        done_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });









    }
}
