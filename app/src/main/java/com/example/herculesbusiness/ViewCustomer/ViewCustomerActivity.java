package com.example.herculesbusiness.ViewCustomer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.herculesbusiness.R;
import com.example.herculesbusiness.utils.CheckInternetConnection;

import java.util.ArrayList;
import java.util.List;

public class ViewCustomerActivity extends AppCompatActivity {

    RelativeLayout root;
    Handler handler;
    AlertDialog dialog;

    ImageView back;
    private List<Customer> customerList = new ArrayList<>();
    private RecyclerView productsRecyclerView;
    private ViewCustomerAdapter viewCustomerAdapter;
    private CustomerListViewModel productListViewModel;
    private boolean isScrolling;
    ProgressBar progressBar, mainProgressBar;
    LinearLayout no_trading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_view);

        progressBar = findViewById(R.id.progressBar);
        mainProgressBar = findViewById(R.id.progressBarMain);
        mainProgressBar.setVisibility(View.VISIBLE);
        no_trading = findViewById(R.id.no_trading);
        root = findViewById(R.id.root);
        checkInternet();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                handler.removeCallbacksAndMessages(null);
            }
        });

        initProductsRecyclerView();
        initProductsAdapter();
        initProductListViewModel();
        getProducts();
        initRecyclerViewOnScrollListener();
    }
    private void initProductsRecyclerView(){
        productsRecyclerView = findViewById(R.id.products_recycler_view);
    }

    private void initProductsAdapter() {
        viewCustomerAdapter = new ViewCustomerAdapter(customerList);
        productsRecyclerView.setAdapter(viewCustomerAdapter);
    }

    private void initProductListViewModel() {
        productListViewModel = new ViewModelProvider(this).get(CustomerListViewModel.class);
    }

    private void getProducts() {
        CustomerListLiveData customerListLiveData = productListViewModel.getProductListLiveData();
        if (customerListLiveData != null) {
            mainProgressBar.setVisibility(View.GONE);
            customerListLiveData.observe(this, new Observer<Operation>() {
                @Override
                public void onChanged(Operation operation) {
                    switch (operation.type) {
                        case R.string.added:
                            Customer addedCustomer = operation.customer;
                            ViewCustomerActivity.this.addProduct(addedCustomer);
                            break;

                        case R.string.modified:
                            Customer modifiedCustomer = operation.customer;
                            ViewCustomerActivity.this.modifyProduct(modifiedCustomer);
                            break;

                        case R.string.removed:
                            Customer removedCustomer = operation.customer;
                            ViewCustomerActivity.this.removeProduct(removedCustomer);
                    }
                    viewCustomerAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        if (customerList.size() == 0) {
            no_trading.setVisibility(View.VISIBLE);
        } else {
            no_trading.setVisibility(View.GONE);
        }
    }

    private void addProduct(Customer addedCustomer) {

        customerList.add(addedCustomer);
        if (customerList.size() == 0) {
            no_trading.setVisibility(View.VISIBLE);
        } else {
            no_trading.setVisibility(View.GONE);
        }
    }

    private void modifyProduct(Customer modifiedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            Customer currentCustomer = customerList.get(i);
            if (currentCustomer.name.equals(modifiedCustomer.name)) {
                customerList.remove(currentCustomer);
                customerList.add(i, modifiedCustomer);
            }
        }
    }

    private void removeProduct(Customer removedCustomer) {
        for (int i = 0; i < customerList.size(); i++) {
            Customer currentCustomer = customerList.get(i);
            if (currentCustomer.name.equals(removedCustomer.name)) {
                customerList.remove(currentCustomer);
            }
        }
    }

    private void initRecyclerViewOnScrollListener() {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (layoutManager != null) {
                    int firstVisibleProductPosition = layoutManager.findFirstVisibleItemPosition();
                    int visibleProductCount = layoutManager.getChildCount();
                    int totalProductCount = layoutManager.getItemCount();

                    if (isScrolling && (firstVisibleProductPosition + visibleProductCount == totalProductCount)) {
                        isScrolling = false;
                        progressBar.setVisibility(View.VISIBLE);
                        getProducts();
                    }
                }
            }
        };
        productsRecyclerView.addOnScrollListener(onScrollListener);
    }
    void checkInternet() {
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(ViewCustomerActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_no_internet, null);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        dialog = mBuilder.create();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 10);
                boolean isInternet = CheckInternetConnection.checkInternet(ViewCustomerActivity.this);
                if (!isInternet) {
                    dialog.show();
                } else {
                    dialog.hide();
                }
            }
        }, 20);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        handler.removeCallbacksAndMessages(null);
    }
}