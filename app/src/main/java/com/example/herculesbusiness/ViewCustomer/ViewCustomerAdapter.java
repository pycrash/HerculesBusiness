package com.example.herculesbusiness.ViewCustomer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.R;

import java.util.List;

public class ViewCustomerAdapter extends RecyclerView.Adapter<ViewCustomerAdapter.ProductViewHolder> {
    private List<Customer> customerList;
    private ItemClickListener clickListener;
    ViewCustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_customer_view, parent, false);
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

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView name, phone, email, address, contactName, contactNumber, gstin;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customer_name);
            phone = itemView.findViewById(R.id.customer_phone);
            email = itemView.findViewById(R.id.customer_email);
            address = itemView.findViewById(R.id.customer_address);
            contactName = itemView.findViewById(R.id.customer_contact_name);
            contactNumber = itemView.findViewById(R.id.customer_contact_number);
            gstin = itemView.findViewById(R.id.customer_gstin);
            itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        void bindProduct(Customer customer) {
            name.setText(customer.getName());
            phone.setText(customer.getPhone());
            email.setText(customer.getEmail());
            address.setText("Address : " + customer.getAddress());
            contactName.setText("Contact Name : " + customer.getContactName());
            contactNumber.setText("Contact Number : " + customer.getContactNumber());
            gstin.setText("GSTIN : " + customer.getGstin());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getPosition());

        }
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;

    }
}