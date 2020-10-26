package com.example.herculesbusiness.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.Models.Product;
import com.example.herculesbusiness.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CartViewHolder> {

    private List<Product> listData = new ArrayList<>();
    private Context context;

    class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView price, name, id, quantity;



        public void setName(TextView name) {
            this.name = name;
        }
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_order_product_name);
            price = itemView.findViewById(R.id.my_order_product_price);
            id = itemView.findViewById(R.id.my_order_product_id);
            quantity = itemView.findViewById(R.id.my_order_quantity);


        }


    }

    public ProductAdapter(List<Product> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.indiviual_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        int multiplier = listData.get(position).getMultiplier();
        int[] ij = {(listData.get(position).getQuantity()) / multiplier};
        holder.name.setText(listData.get(position).getProductName());
        holder.id.setText(context.getString(R.string.product_id, listData.get(position).getProductID()));
        holder.price.setText(context.getString(R.string.price, String.valueOf(listData.get(position).getPrice() * listData.get(position).getQuantity())));
        holder.quantity.setText("Quantity : " + listData.get(position).getQuantity());

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


}
