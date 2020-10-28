package com.example.herculesbusiness.NewOrders;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.Common;
import com.example.herculesbusiness.Models.DataMessage;
import com.example.herculesbusiness.Models.MyResponse;
import com.example.herculesbusiness.Models.Notification;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.Models.Sender;
import com.example.herculesbusiness.Models.Token;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.Remote.APIService;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedNewOrderActivity extends AppCompatActivity {
    public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
            pincode, discount, amount, new_amount, gstin, email, text_view_cancel;
    public CardView done_order, approve, cancel;
    RecyclerView recyclerView;
    TextView status;
    APIService apiService;

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

        apiService = Common.getFCMService();

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
        cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        text_view_cancel = findViewById(R.id.text_cancellation);


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
        boolean cancel_order = pendingOrderModel.isCancelled();
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
        if (cancel_order) {
            cancel.setVisibility(View.VISIBLE);
            text_view_cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
            text_view_cancel.setVisibility(View.GONE);
        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternet = CheckInternetConnection.checkInternet(getApplicationContext());
                if (!isInternet) {
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cancel_order) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailedNewOrderActivity.this, R.style.MyAlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setMessage("The user has requested for cancellation. Do you still want to approve this order ?");
                    builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            approveOrder();                        }
                    }).setNeutralButton("Cancel Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancelOrder();
                        }
                    });
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    approveOrder();
                }
            }
        });
    }
    public void approveOrder() {
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

        dialog.hide();
        sendOrderStatusToUser(pendingOrderModel);
        onBackPressed();
        finish();
    }

    private void sendOrderStatusToUser(OrderModel pendingOrderModel) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        databaseReference.orderByKey().equalTo(pendingOrderModel.getPhone())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    Notification notification = new Notification("Hercules", "You have new order : "+ pendingOrderModel.getOrderID());
                    Sender content = new Sender(serverToken.getToken(), notification);
                    apiService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.body().success == 1) {
                                        Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed !!", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void cancelOrder() {
        ProgressDialog dialog = new ProgressDialog(DetailedNewOrderActivity.this, R.style.ProgressDialog);
        dialog.setMessage("Cancelling Order !!");
        dialog.setCancelable(false);
        dialog.show();
        OrderModel pendingOrderModel = (OrderModel) getIntent().getSerializableExtra("orderDetails");
        FirebaseDatabase db = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = db.getReference("Requests").child("Completed Orders");
        databaseReference.child(orderid.getText().toString()).setValue(pendingOrderModel);

        Map<String, Object> user = new HashMap<>();
        user.put("status", "Cancelled");
        user.put("notes", "The order has been cancelled");

        databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("Completed Orders").child(orderid.getText().toString());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("New Orders").child(orderid.getText().toString());
        databaseReference.removeValue();

        databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("New Orders").child(orderid.getText().toString());
        databaseReference.removeValue();

        databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Completed Orders");
        databaseReference.child(orderid.getText().toString()).setValue(pendingOrderModel);

        databaseReference = db.getReference(mailing_name.getText().toString().replaceAll(" ", "")).child("Completed Orders").child(orderid.getText().toString());
        databaseReference.updateChildren(user);

        Toast.makeText(getApplicationContext(), "Order Cancelled", Toast.LENGTH_SHORT).show();
        dialog.hide();
        onBackPressed();
        finish();
    }
}
