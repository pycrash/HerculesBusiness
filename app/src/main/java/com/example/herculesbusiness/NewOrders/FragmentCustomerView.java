package com.example.herculesbusiness.NewOrders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herculesbusiness.Models.OrderModel;
import com.example.herculesbusiness.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCustomerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCustomerView extends Fragment {
    RecyclerView recyclerView;
    List<OrderModel> list;
    NewOrderCustomerAdapter adapter;
    LinearLayout noOrders;
    ProgressBar progressBar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCustomerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrderHorizontal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCustomerView newInstance(String param1, String param2) {
        FragmentCustomerView fragment = new FragmentCustomerView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_new_order_horizontal, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        noOrders = view.findViewById(R.id.new_orders_no_orders);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.new_orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        loadOrders();
        return view;
    }
    private void loadOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child("New Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    OrderModel newOrderModel = ds.getValue(OrderModel.class);
                    list.add(newOrderModel);
                    adapter = new NewOrderCustomerAdapter(getContext(), list);
                    recyclerView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
                if (list.size() == 0) {
                    noOrders.setVisibility(View.VISIBLE);
                } else {
                    noOrders.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });
    }
}