package com.example.herculesbusiness.LeisureRequests;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.R;
import com.example.herculesbusiness.ViewCustomer.Customer;

import java.util.List;

public class ViewLeisureTradingAdapter extends RecyclerView.Adapter<ViewLeisureTradingAdapter.ProductViewHolder> {
    private List<Customer> customerList;
    Context context;


    ViewLeisureTradingAdapter(List<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_trading_sol, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bindProduct(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder  {
        TextView name, phone, email, address, contactName, contactNumber, gstin;
        Button send_sol;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customer_name);
            phone = itemView.findViewById(R.id.customer_phone);
            email = itemView.findViewById(R.id.customer_email);
            address = itemView.findViewById(R.id.customer_address);
            contactName = itemView.findViewById(R.id.customer_contact_name);
            contactNumber = itemView.findViewById(R.id.customer_contact_number);
            gstin = itemView.findViewById(R.id.customer_gstin);

            send_sol = itemView.findViewById(R.id.send_SOL);


        }

        void bindProduct(Customer customer) {
            name.setText(customer.getName());
            phone.setText(customer.getPhone());
            email.setText(customer.getEmail());
            address.setText("Address : " + customer.getAddress());
            contactName.setText("Contact Name : " + customer.getContactName());
            contactNumber.setText("Contact Number : " + customer.getContactNumber());
            gstin.setText("GSTIN : " + customer.getGstin());
            send_sol.setText("Send Leisure");

            send_sol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UploadLeisureActivity.class);
                    intent.putExtra("name", customer.getMailingName().replaceAll(" ", ""));
                    intent.putExtra("email", customer.getEmail());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });

        }
    }
}