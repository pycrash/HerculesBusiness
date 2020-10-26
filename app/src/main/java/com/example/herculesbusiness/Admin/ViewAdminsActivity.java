package com.example.herculesbusiness.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ViewAdminsActivity extends AppCompatActivity {
    RelativeLayout root;
    Handler handler;
    AlertDialog dialog;

    ImageView back;
    private List<Admin> adminList = new ArrayList<>();
    private RecyclerView productsRecyclerView;
    private ViewAdminAdapter viewAdminAdapter;
    private AdminListViewModel adminListViewModel;
    private boolean isScrolling;
    ProgressBar progressBar, mainProgressBar;
    LinearLayout no_trading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_admins);
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
        viewAdminAdapter = new ViewAdminAdapter(adminList);
        productsRecyclerView.setAdapter(viewAdminAdapter);
    }

    private void initProductListViewModel() {
        adminListViewModel = new ViewModelProvider(this).get(AdminListViewModel.class);
    }

    private void getProducts() {
        AdminListLiveData customerListLiveData = adminListViewModel.getProductListLiveData();
        if (customerListLiveData != null) {
            mainProgressBar.setVisibility(View.GONE);
            customerListLiveData.observe(this, new Observer<Operation>() {
                @Override
                public void onChanged(Operation operation) {
                    switch (operation.type) {
                        case R.string.added:
                            Admin addedCustomer = operation.admin;
                            ViewAdminsActivity.this.addProduct(addedCustomer);
                            break;

                        case R.string.modified:
                            Admin modifiedCustomer = operation.admin;
                            ViewAdminsActivity.this.modifyProduct(modifiedCustomer);
                            break;

                        case R.string.removed:
                            Admin removedCustomer = operation.admin;
                            ViewAdminsActivity.this.removeProduct(removedCustomer);
                    }
                    viewAdminAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        if (adminList.size() == 0) {
            no_trading.setVisibility(View.VISIBLE);
        } else {
            no_trading.setVisibility(View.GONE);
        }
    }

    private void addProduct(Admin addedCustomer) {

        adminList.add(addedCustomer);
        if (adminList.size() == 0) {
            no_trading.setVisibility(View.VISIBLE);
        } else {
            no_trading.setVisibility(View.GONE);
        }
    }

    private void modifyProduct(Admin modifiedCustomer) {
        for (int i = 0; i < adminList.size(); i++) {
            Admin currentCustomer = adminList.get(i);
            if (currentCustomer.name.equals(modifiedCustomer.name)) {
                adminList.remove(currentCustomer);
                adminList.add(i, modifiedCustomer);
            }
        }
    }

    private void removeProduct(Admin removedCustomer) {
        for (int i = 0; i < adminList.size(); i++) {
            Admin currentCustomer = adminList.get(i);
            if (currentCustomer.name.equals(removedCustomer.name)) {
                adminList.remove(currentCustomer);
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
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(ViewAdminsActivity.this);
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
                boolean isInternet = CheckInternetConnection.checkInternet(ViewAdminsActivity.this);
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

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}