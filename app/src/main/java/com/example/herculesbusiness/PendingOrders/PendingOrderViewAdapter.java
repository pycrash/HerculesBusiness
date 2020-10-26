package com.example.herculesbusiness.PendingOrders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingOrderViewAdapter extends RecyclerView.Adapter<PendingOrderViewAdapter.MyOrderHolder> {

    List<OrderModel> listData;
    Context context;
    String TAG = "My Orders";

    public PendingOrderViewAdapter(Context context, List<OrderModel> listData) {
        this.listData = listData;
        this.context = context;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder {
        public TextView orderid, date,name, contact_name,mailing_name, phone, contact_phone, address,state,
                pincode,discount, amount, new_amount, gstin, email, status, addNotes;
        public CardView done_order, status_button, add_notes_butoon;
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
            done_order = itemView.findViewById(R.id.pending_order_done);
            addNotes = itemView.findViewById(R.id.pending_order_add_notes);
            status_button = itemView.findViewById(R.id.status);
            recyclerView = itemView.findViewById(R.id.new_order_items_recycler_view);
            email =  itemView.findViewById(R.id.new_order_email);
            status = itemView.findViewById(R.id.pending_order_status);
            add_notes_butoon = itemView.findViewById(R.id.notes);


        }

    }



    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_orders_pending, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderHolder holder, final int position) {
        //getting data
        CharSequence[] values;
        final int[] pos = {10};

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
        holder.pincode.setText(listData.get(position).getPincode());
        holder.discount.setText(listData.get(position).getDiscount());
        holder.amount.setText(listData.get(position).getTotal());
        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());
        holder.gstin.setText(listData.get(position).getGstin());
        holder.addNotes.setText(listData.get(position).getNotes());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.status.setText(listData.get(position).getStatus());
        ProductAdapter cartAdapter  = new ProductAdapter(listData.get(position).getCart(), context);
        holder.recyclerView.setAdapter(cartAdapter);
        values = new CharSequence[]{"Approved", "Packed", "Shipped", "Delivered"};

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

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
                if (pos[0] == 10) {
                    Toast.makeText(context, "Nothing Selected", Toast.LENGTH_LONG).show();
                } else {
                    dialogInterface.dismiss();
                    ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressDialog);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();

                    Map<String, Object> user = new HashMap<>();
                    user.put("status", values[pos[0]]);
                    DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Pending Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    progressDialog.hide();
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                    holder.status.setText(values[pos[0]]);
                }
            }
        });
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        pos[0] = 0;
                        break;
                    case 1:
                        pos[0] = 1;
                        break;
                    case 2:
                        pos[0] = 2;
                        break;
                    case 3:
                        pos[0] = 3;
                        break;
                    default:
                        pos[0] = 10;
                }

            }
        });
        dialog = builder.create();

        holder.done_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInternet = CheckInternetConnection.checkInternet(context);
                if (!isInternet) {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressDialog);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Marking the order as complete...");
                progressDialog.show();

                Map<String, Object> user = new HashMap<>();
                user.put("status", "Completed");
                OrderModel model = listData.get(position);
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference = db.getReference("Requests").child("Completed Orders");
                databaseReference.child(listData.get(position).getOrderID()).setValue(model);

                databaseReference = db.getReference("Requests").child("Pending Orders").child(listData.get(position).getOrderID());
                databaseReference.removeValue();


                databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Pending Orders").child(listData.get(position).getOrderID());
                databaseReference.removeValue();

                databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Completed Orders");
                databaseReference.child(listData.get(position).getOrderID()).setValue(model);

                databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Completed Orders").child(listData.get(position).getOrderID());
                databaseReference.updateChildren(user);

                databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Orders").child(listData.get(position).getOrderID());
                databaseReference.updateChildren(user);



                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        holder.status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.dialog_add_notes, null);
        EditText mPassword = (EditText) mView.findViewById(R.id.etPassword);
        Button mAuthenticate = (Button) mView.findViewById(R.id.btnLogin);
        Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
        mPassword.setText(listData.get(position).getNotes());

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
                    Toast.makeText(context, "Nothing To add", Toast.LENGTH_SHORT).show();

                } else {
                    mdialog.dismiss();
                    ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressDialog);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Updating Notes...");
                    progressDialog.show();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("notes", mPassword.getText().toString());
                    DatabaseReference databaseReference = db.getReference("Requests").child("Pending Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    databaseReference = db.getReference(listData.get(position).getMailingName().replaceAll(" ", "")).child("Pending Orders").child(listData.get(position).getOrderID());
                    databaseReference.updateChildren(user);
                    progressDialog.hide();
                    holder.addNotes.setText(mPassword.getText().toString());
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.add_notes_butoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
