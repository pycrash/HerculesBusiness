package com.example.herculesbusiness.CompletedOrders;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.Adapters.ProductAdapter;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.R;

import java.util.List;

public class CompletedOrderOrderViewAdapter extends RecyclerView.Adapter<CompletedOrderOrderViewAdapter.MyOrderHolder> {

    List<OrderModel> listData;
    Context context;
    String TAG = "My Orders";

    public CompletedOrderOrderViewAdapter(Context context, List<OrderModel> listData) {
        this.listData = listData;
        this.context = context;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder {
        public TextView orderid, date,name, contact_name,mailing_name, phone, contact_phone, address,state,
                pincode,discount, amount, new_amount, gstin, email;
        RecyclerView recyclerView;
        TextView addNotes;
        TextView status;


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
            status = itemView.findViewById(R.id.completed_order_status);
            gstin = itemView.findViewById(R.id.new_order_gstin);
            addNotes = itemView.findViewById(R.id.pending_order_add_notes);
            recyclerView = itemView.findViewById(R.id.new_order_items_recycler_view);
            email =  itemView.findViewById(R.id.new_order_email);
            status = itemView.findViewById(R.id.pending_order_status);

        }

    }



    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_orders_completed, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderHolder holder, final int position) {
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
        holder.pincode.setText(listData.get(position).getPincode());
        holder.discount.setText(listData.get(position).getDiscount());
        holder.amount.setText(listData.get(position).getTotal());
        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());
        holder.gstin.setText(listData.get(position).getGstin());
        holder.addNotes.setText(listData.get(position).getNotes());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.state.setText(listData.get(position).getStatus());

        ProductAdapter cartAdapter  = new ProductAdapter(listData.get(position).getCart(), context);
        holder.recyclerView.setAdapter(cartAdapter);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
