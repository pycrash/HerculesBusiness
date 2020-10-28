package com.example.herculesbusiness.NewOrders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrderOrderViewAdapter extends RecyclerView.Adapter<NewOrderOrderViewAdapter.MyOrderHolder> {

    List<OrderModel> listData;
    Context context;
    String TAG = "My Orders";

    public NewOrderOrderViewAdapter(Context context, List<OrderModel> listData) {
        this.listData = listData;
        this.context = context;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder {
        public TextView orderid, date, name, contact_name, mailing_name, phone, contact_phone, address, state,
                pincode, discount, amount, new_amount, status, gstin, email, cancel_text;
        public CardView approve_order, cancel_order;
        RecyclerView recyclerView;


        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.new_order_id);
            date = itemView.findViewById(R.id.new_order_date);
            name = itemView.findViewById(R.id.new_order_name);
            contact_name = itemView.findViewById(R.id.new_order_contact_name);
            mailing_name = itemView.findViewById(R.id.new_order_mailing_name);
            phone = itemView.findViewById(R.id.new_order_number);
            contact_phone = itemView.findViewById(R.id.new_order_contact_number);
            address = itemView.findViewById(R.id.new_order_address);
            state = itemView.findViewById(R.id.new_order_state);
            pincode = itemView.findViewById(R.id.new_order_pincode);
            discount = itemView.findViewById(R.id.new_order_discount);
            amount = itemView.findViewById(R.id.new_order_price);
            new_amount = itemView.findViewById(R.id.new_order_new_price);
            status = itemView.findViewById(R.id.new_order_status);
            gstin = itemView.findViewById(R.id.new_order_gstin);
            approve_order = itemView.findViewById(R.id.new_order_approve);
            recyclerView = itemView.findViewById(R.id.new_order_items_recycler_view);
            email = itemView.findViewById(R.id.new_order_email);
            cancel_order = itemView.findViewById(R.id.new_order_cancel);
            cancel_text = itemView.findViewById(R.id.new_order_cancel_text);


        }

    }


    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_orders_new, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, final int position) {
        //getting data


        holder.orderid.setText(listData.get(position).getOrderID());
        holder.date.setText(listData.get(position).getDate());
        holder.name.setText(listData.get(position).getName());
        holder.contact_name.setText(listData.get(position).getContactName());
        holder.mailing_name.setText(listData.get(position).getMailingName());
        holder.phone.setText(listData.get(position).getPhone());
        holder.email.setText(listData.get(position).getEmail());
        holder.contact_phone.setText(listData.get(position).getContactNumber());
        holder.address.setText(listData.get(position).getAddress());
        holder.state.setText(listData.get(position).getState());
        holder.status.setText(listData.get(position).getStatus());
        holder.pincode.setText(listData.get(position).getPincode());
        holder.discount.setText(listData.get(position).getDiscount());
        holder.amount.setText(listData.get(position).getTotal());
        holder.amount.setPaintFlags(holder.amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());
        holder.gstin.setText(listData.get(position).getGstin());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ProductAdapter cartAdapter = new ProductAdapter(listData.get(position).getCart(), context);
        holder.recyclerView.setAdapter(cartAdapter);

        boolean cancel = listData.get(position).isCancelled();
        if (cancel) {
            holder.cancel_order.setVisibility(View.VISIBLE);
            holder.cancel_text.setVisibility(View.VISIBLE);
        } else {
            holder.cancel_order.setVisibility(View.GONE);
            holder.cancel_text.setVisibility(View.GONE);
        }
        holder.approve_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternet = CheckInternetConnection.checkInternet(context);
                if (!isInternet) {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cancel) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setCancelable(false);
                    builder.setMessage("The user has requested for cancellation. Do you still want to approve this order ?");
                    builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            approveOrder(position);
                        }
                    }).setNeutralButton("Cancel Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cancelOrder(position);
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
                    approveOrder(position);
                }

            }
        });
        holder.cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void approveOrder(int position) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.ProgressDialog);
        dialog.setMessage("Working on it !!");
        dialog.setCancelable(false);
        dialog.show();
        OrderModel model = listData.get(position);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders");
        databaseReference.child(listData.get(position).getOrderID()).setValue(model);

        Map<String, Object> user = new HashMap<>();
        user.put("status", "Approved");
        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("Pending Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("New Orders").child(listData.get(position).getOrderID());
        databaseReference.removeValue();

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("New Orders").child(listData.get(position).getOrderID());
        databaseReference.removeValue();

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Pending Orders");
        databaseReference.child(listData.get(position).getOrderID()).setValue(model);

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Pending Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);
        Toast.makeText(context, "Order Approved", Toast.LENGTH_SHORT).show();
        dialog.hide();
    }

    public void cancelOrder(int position) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.ProgressDialog);
        dialog.setMessage("Cancelling Order");
        dialog.setCancelable(false);
        dialog.show();
        OrderModel model = listData.get(position);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = db.getReference("Requests").child("Completed Orders");
        databaseReference.child(listData.get(position).getOrderID()).setValue(model);

        Map<String, Object> user = new HashMap<>();
        user.put("status", "Cancelled");
        user.put("notes", "The order has been cancelled !!");


        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("Completed Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);

        databaseReference = db.getReference("Requests").child("New Orders").child(listData.get(position).getOrderID());
        databaseReference.removeValue();

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("New Orders").child(listData.get(position).getOrderID());
        databaseReference.removeValue();

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Completed Orders");
        databaseReference.child(listData.get(position).getOrderID()).setValue(model);

        databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Completed Orders").child(listData.get(position).getOrderID());
        databaseReference.updateChildren(user);
        Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
        dialog.hide();
    }
}
