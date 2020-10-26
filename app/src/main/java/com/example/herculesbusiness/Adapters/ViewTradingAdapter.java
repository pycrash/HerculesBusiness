package com.example.herculesbusiness.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.R;
import com.example.herculesbusiness.Trading.UploadPDF;

import java.util.List;

public class ViewTradingAdapter extends RecyclerView.Adapter<ViewTradingAdapter.SOLViewHolder> {
    public List<UploadPDF> uploadPDFList;
    private Context mContext;

    public ViewTradingAdapter(List<UploadPDF> uploadPDFList, Context context) {
        this.uploadPDFList = uploadPDFList;
        mContext = context;
    }

    @NonNull
    @Override
    public SOLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_sol_view, parent, false);
        return new SOLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SOLViewHolder holder, int position) {
        UploadPDF uploadPDF = uploadPDFList.get(position);
        holder.bindProduct(uploadPDF);
    }

    @Override
    public int getItemCount() {
        return uploadPDFList.size();
    }

    public class SOLViewHolder extends RecyclerView.ViewHolder{
        TextView name, date;
        CardView view;

        SOLViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sol_name_of_file);
            date = itemView.findViewById(R.id.sol_date);
            view = itemView.findViewById(R.id.view);

        }

        void bindProduct(UploadPDF uploadPDF) {
            name.setText(uploadPDF.getName());
            date.setText(uploadPDF.getDate());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Opening the upload file in browser using the upload url
                    UploadPDF upload = uploadPDFList.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(upload.getUrl()));
                    mContext.startActivity(intent);
                }
            });
        }


    }

}