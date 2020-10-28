package com.example.herculesbusiness.PendingOrders;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DetailedPendingActivity extends AppCompatActivity {
    public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
            pincode, discount, amount, new_amount, gstin, email;
    public CardView done_order, mark_complete, change_status, add_notes;
    RecyclerView recyclerView;
    TextView addNotes;
    TextView status;
    OrderModel pendingOrderModel;
    AlertDialog alertDialog = null;
    int position = 10;
    CharSequence[] values;
    APIService apiService;

    private static final String TAG = "DetailedPendingOrder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_pending);


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
        mark_complete = findViewById(R.id.mark);
        change_status = findViewById(R.id.status);
        add_notes = findViewById(R.id.notes);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedPendingActivity.this));
        ProductAdapter cartAdapter = new ProductAdapter(pendingOrderModel.getCart(), DetailedPendingActivity.this);
        recyclerView.setAdapter(cartAdapter);
        done_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        values = new CharSequence[]{"Approved", "Packed", "Shipped", "Delivered"};

        mark_complete = findViewById(R.id.mark);
        change_status = findViewById(R.id.status);
        add_notes = findViewById(R.id.notes);

        mark_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(DetailedPendingActivity.this, R.style.ProgressDialog);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Marking the order as complete...");
                progressDialog.show();

                Map<String, Object> user = new HashMap<>();
                user.put("status", "Completed");
                OrderModel model = pendingOrderModel;
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference = db.getReference("Requests").child("Completed Orders");
                databaseReference.child(orderid.getText().toString().trim()).setValue(model);

                databaseReference = db.getReference("Requests").child("Completed Orders").child((orderid.getText().toString().trim()));
                databaseReference.updateChildren(user);

                databaseReference = db.getReference("Requests").child("Pending Orders").child(orderid.getText().toString().trim());
                databaseReference.removeValue();


                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString().trim());
                databaseReference.updateChildren(user);

                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Pending Orders").child(orderid.getText().toString().trim());
                databaseReference.removeValue();

                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Completed Orders").child(orderid.getText().toString().trim());
                databaseReference.setValue(model);

                databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Completed Orders").child(orderid.getText().toString().trim());
                databaseReference.updateChildren(user);

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                sendOrderStatusToUser(pendingOrderModel,"Your order " + pendingOrderModel.getOrderID() + " was completed");
                progressDialog.hide();
                onBackPressed();
                finish();
            }
        });





        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedPendingActivity.this, R.style.MyAlertDialogStyle);

        builder.setTitle("Status Options: ");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (position == 10) {
                    Toast.makeText(DetailedPendingActivity.this, "Nothing Selected", Toast.LENGTH_LONG).show();
                } else {
                    dialogInterface.dismiss();
                    ProgressDialog progressDialog = new ProgressDialog(DetailedPendingActivity.this, R.style.ProgressDialog);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();

                    Map<String, Object> user = new HashMap<>();
                    user.put("status", values[position]);
                    DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);

                    databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Pending Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);

                    progressDialog.hide();
                    sendOrderStatusToUser(pendingOrderModel,"Your order " + pendingOrderModel.getOrderID() + " was "+ values[position]);
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    status.setText(values[position]);
                }
            }
        });
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        position = 0;
                        break;
                    case 1:
                        position = 1;
                        break;
                    case 2:
                        position = 2;
                        break;
                    case 3:
                        position = 3;
                        break;
                    default:
                        position = 10;
                }

            }
        });
        alertDialog = builder.create();

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });



       AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailedPendingActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_notes, null);
        EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);
        Button mAuthenticate = (Button) mView.findViewById(R.id.btnLogin);
        Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
        mPassword.setText(addNotes.getText().toString());

        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        AlertDialog mdialog = mBuilder.create();
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdialog.dismiss();
                mdialog.hide();

            }
        });

        mAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPassword.getText().toString().length() == 0) {
                    mdialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Nothing To add", Toast.LENGTH_SHORT).show();

                } else {
                    mdialog.dismiss();
                    ProgressDialog progressDialog = new ProgressDialog(DetailedPendingActivity.this, R.style.ProgressDialog);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Updating Notes...");
                    progressDialog.show();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("notes", mPassword.getText().toString());
                    DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference("Requests").child("Pending Orders").child((orderid.getText().toString().trim()));
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(mailing_name.getText().toString().trim().replaceAll(" ", "")).child("Pending Orders").child(orderid.getText().toString());
                    databaseReference.updateChildren(user);

                    progressDialog.hide();
                    addNotes.setText(mPassword.getText().toString());
                    sendOrderStatusToUser(pendingOrderModel, "Seller has added notes to your order " + pendingOrderModel.getOrderID());
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.show();
            }
        });
    }
    private void sendOrderStatusToUser(OrderModel pendingOrderModel, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        databaseReference.orderByKey().equalTo(pendingOrderModel.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            Notification notification = new Notification("Hercules",  message);
//                            Sender content = new Sender(serverToken.getToken(), notification);

//                            Map<String , String> dataSend = new HashMap<>();
//                            dataSend.put("title", "Hercules");
//                            dataSend.put("message", message);
//                            DataMessage dataMessage = new DataMessage(serverToken.getToken(), dataSend);
                            Token serverToken = postSnapshot.getValue(Token.class);
                            Notification notification = new Notification("Hercules", "You order with "+pendingOrderModel.getOrderID() + " is approved");
                            Sender content = new Sender(serverToken.getToken(), notification);

                            apiService.sendNotification(content)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.body().success == 1) {
                                                Toast.makeText(getApplicationContext(), "Order Updated", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Order was updated but failed to send Notification !!", Toast.LENGTH_LONG).show();

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.d(TAG, "onFailure: " + t);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
