package com.example.herculesbusiness.Orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.CompletedOrders.DetailedCompleteOrder;
import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.Models.Product;
import com.example.herculesbusiness.NewOrders.DetailedNewOrderActivity;
import com.example.herculesbusiness.PendingOrders.DetailedPendingActivity;
import com.example.herculesbusiness.R;

import java.io.Serializable;
import java.util.List;

public class ViewCustomerOrderAdapter extends RecyclerView.Adapter<ViewCustomerOrderAdapter.MyOrderHolder>  {

    List<OrderModel> listData;
    Context context;
    String TAG = "My Orders";
    String category;

    public ViewCustomerOrderAdapter(Context context, List<OrderModel> listData, String category) {
        this.listData = listData;
        this.context = context;
        this.category = category;
    }

    class MyOrderHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView status, id, date,amount, new_amount, name, email, phone;
        public CardView view_details;



        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.my_order_status);
            id = itemView.findViewById(R.id.my_order_id);
            date = itemView.findViewById(R.id.my_order_date);
            amount = itemView.findViewById(R.id.my_order_price);
            view_details = itemView.findViewById(R.id.my_order_details);
            new_amount = itemView.findViewById(R.id.my_order_new_price);
            name = itemView.findViewById(R.id.new_order_name);
            email = itemView.findViewById(R.id.new_order_email);
            phone = itemView.findViewById(R.id.new_order_phone);

        }

    }



    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.individual_customer_order_item, parent, false);
        return new MyOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int position) {
        //getting data
        String orderid = listData.get(position).getOrderID();
        String date = listData.get(position).getDate();
        String amount = listData.get(position).getTotal();
        String status = listData.get(position).getStatus();
        List<Product> products = listData.get(position).getCart();
        holder.name.setText(listData.get(position).getName());
        holder.phone.setText(listData.get(position).getPhone());
        holder.email.setText(listData.get(position).getEmail());
        holder.id.setText(orderid);
        holder.status.setText(status);
        holder.date.setText(date);
        holder.amount.setText(amount);
        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.new_amount.setText(listData.get(position).getNewTotal());

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if (category.equals("New Orders")) {
                    intent = new Intent(context, DetailedNewOrderActivity.class);
                } else if (category.equals("Pending Orders")) {
                    intent = new Intent(context, DetailedPendingActivity.class);
                } else {
                    intent = new Intent(context, DetailedCompleteOrder.class);
                }
                intent.putExtra("orderDetails",  listData.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

}
