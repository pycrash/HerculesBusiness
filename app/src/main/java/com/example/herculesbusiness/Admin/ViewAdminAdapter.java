package com.example.herculesbusiness.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.R;

import java.util.List;

public class ViewAdminAdapter extends RecyclerView.Adapter<ViewAdminAdapter.ProductViewHolder> {
    private List<Admin> adminList;
    ViewAdminAdapter(List<Admin> adminList) {
        this.adminList = adminList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_admin_view, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Admin admin = adminList.get(position);
        holder.bindProduct(admin);
    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder  {
        TextView name, phone, email;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customer_name);
            phone = itemView.findViewById(R.id.customer_phone);
            email = itemView.findViewById(R.id.customer_email);

        }

        void bindProduct(Admin admin) {
            name.setText(admin.getName());
            phone.setText(admin.getPhone());
            email.setText(admin.getEmail());
        }
    }
}