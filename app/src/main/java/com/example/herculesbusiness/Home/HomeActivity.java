package com.example.herculesbusiness.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.herculesbusiness.Admin.AddAdminActivity;
import com.example.herculesbusiness.Admin.AdminOperationsActivity;
import com.example.herculesbusiness.Admin.AdminUpdate;
import com.example.herculesbusiness.Admin.UpdateAdminPassword;
import com.example.herculesbusiness.Admin.ViewAdminsActivity;
import com.example.herculesbusiness.Customers.AddCustomerActivity;
import com.example.herculesbusiness.Customers.CustomerOperationsActivity;
import com.example.herculesbusiness.Customers.UpdateCustomerActivity;
import com.example.herculesbusiness.Customers.UpdateDiscountActivity;
import com.example.herculesbusiness.Customers.UpdatePasswordActivity;
import com.example.herculesbusiness.LeisureRequests.LeisureRequests;
import com.example.herculesbusiness.Login.LoginActivity;
import com.example.herculesbusiness.CompletedOrders.CompletedOrdersActivity;
import com.example.herculesbusiness.Models.Token;
import com.example.herculesbusiness.NewOrders.NewOrdersActivity;
import com.example.herculesbusiness.Orders.OrdersOperationActivity;
import com.example.herculesbusiness.PendingOrders.PendingOrdersActivity;
import com.example.herculesbusiness.R;
import com.example.herculesbusiness.RequestsActivity;
import com.example.herculesbusiness.SOLRequests.SOLRequestActivity;
import com.example.herculesbusiness.ViewCustomer.CustomerView;
import com.example.herculesbusiness.ViewCustomer.ViewCustomerActivity;
import com.example.herculesbusiness.utils.LoadImagesInBitmap;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.hawk.Hawk;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ImageView menu;
    DrawerLayout drawer;
    NavigationView navigationView;
    private SwitchCompat switcher;
    CardView customers, orders, admmin, trading, requests;
    ImageView customers_image_view, admin_image_view, trading_image_view, orders_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        customers_image_view = (ImageView) findViewById(R.id.customers_image);
        admin_image_view = (ImageView) findViewById(R.id.admin_image);
        trading_image_view = (ImageView) findViewById(R.id.trading_image);
        orders_image_view = (ImageView) findViewById(R.id.orders_image);

        trading = findViewById(R.id.trading);
        trading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerView.class));
            }
        });

        requests = findViewById(R.id.requests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RequestsActivity.class));
            }
        });

        try {

            Glide.with(getApplicationContext()).load(ContextCompat.getDrawable(getApplicationContext(), R.drawable.customers)).into(customers_image_view);
        } catch (OutOfMemoryError error) {
            customers_image_view.setImageBitmap(
                    LoadImagesInBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.customers, 100, 100));
        }


        try {

            Glide.with(getApplicationContext()).load(ContextCompat.getDrawable(getApplicationContext(), R.drawable.admin)).into(admin_image_view);
        } catch (OutOfMemoryError error) {
            admin_image_view.setImageBitmap(
                    LoadImagesInBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.admin, 100, 100));
        }

        try {

            Glide.with(getApplicationContext()).load(ContextCompat.getDrawable(getApplicationContext(), R.drawable.order)).into(orders_image_view);
        } catch (OutOfMemoryError error) {
            orders_image_view.setImageBitmap(
                    LoadImagesInBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.order, 100, 100));
        }

        try {

            Glide.with(getApplicationContext()).load(ContextCompat.getDrawable(getApplicationContext(), R.drawable.payments)).into(trading_image_view);
        } catch (OutOfMemoryError error) {
            trading_image_view.setImageBitmap(
                    LoadImagesInBitmap.decodeSampledBitmapFromResource(getResources(), R.drawable.payments, 100, 100));
        }
        drawer = findViewById(R.id.drawer_layout);
        customers = findViewById(R.id.customers);
        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerOperationsActivity.class));
            }
        });

        orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrdersOperationActivity.class));
            }
        });

        admmin = findViewById(R.id.admin);
        admmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminOperationsActivity.class));
            }
        });

        menu=findViewById(R.id.experiment);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        Menu menu1 = navigationView.getMenu();
        MenuItem menuItem = menu1.findItem(R.id.dark_mode_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);
        SharedPreferences appSettingPrefs = getSharedPreferences("AppSetting", 0);
        final SharedPreferences.Editor sharedPrefsEdit = appSettingPrefs.edit();
        boolean isNightModeOn = appSettingPrefs.getBoolean("NightMode", false);


        switcher = (SwitchCompat) actionView.findViewById(R.id.switcher);

        if (isNightModeOn) {
            switcher.setChecked(true);
        } else {
            switcher.setChecked(false);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                    sharedPrefsEdit.putBoolean("NightMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPrefsEdit.putBoolean("NightMode", false);
                }
                sharedPrefsEdit.apply();
            }
        });

        setupHeaderView();

        //send Token
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference("Tokens");
        Token data = new Token(token, true);
        Hawk.init(getApplicationContext()).build();
        databaseReference.child(Hawk.get("phone")).setValue(data);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {






        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.MyAlertDialogStyle);
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Hawk.deleteAll();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }




        if (id == R.id.new_orders) {
            startActivity(new Intent(HomeActivity.this, NewOrdersActivity.class));
        }
        if (id == R.id.pending_orders) {
            startActivity(new Intent(HomeActivity.this, PendingOrdersActivity.class));
        }
        if (id == R.id.completed_orders) {
            startActivity(new Intent(HomeActivity.this, CompletedOrdersActivity.class));
        }
        if (id == R.id.trading) {
            startActivity(new Intent(HomeActivity.this, CustomerView.class));
        }
        if (id == R.id.sol_requests) {
            startActivity(new Intent(HomeActivity.this, SOLRequestActivity.class));
        }
        if (id == R.id.leisure_requests) {
            startActivity(new Intent(HomeActivity.this, LeisureRequests.class));
        }
        if (id == R.id.view_admin) {
            startActivity(new Intent(HomeActivity.this, ViewAdminsActivity.class));
        }
        if (id == R.id.add_admin) {
            startActivity(new Intent(HomeActivity.this, AddAdminActivity.class));
        }
        if (id == R.id.change_admin_password) {
            startActivity(new Intent(HomeActivity.this, UpdateAdminPassword.class));
        }
        if (id == R.id.update_admin) {
            startActivity(new Intent(HomeActivity.this, AdminUpdate.class));
        }
        if (id == R.id.view_customers) {
            startActivity(new Intent(HomeActivity.this, ViewCustomerActivity.class));
        }
        if (id == R.id.add_customer) {
            startActivity(new Intent(HomeActivity.this, AddCustomerActivity.class));
        }
        if (id == R.id.change_customer_password) {
            startActivity(new Intent(HomeActivity.this, UpdatePasswordActivity.class));
        }
        if (id == R.id.update_customer) {
            startActivity(new Intent(HomeActivity.this, UpdateCustomerActivity.class));
        }
        if (id == R.id.update_customer_discount) {
            startActivity(new Intent(HomeActivity.this, UpdateDiscountActivity.class));
        }




        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    void setupHeaderView() {
        Hawk.init(HomeActivity.this).build();
        View hView = navigationView.getHeaderView(0);
        TextView header_name = (TextView) hView.findViewById(R.id.textview_header_name);
        String name = Hawk.get("name");
        header_name.setText(name);

        TextView header_email = (TextView) hView.findViewById(R.id.textView_header_email_address);
        String email = Hawk.get("email");
        header_email.setText(email);

        TextView header_phone = (TextView) hView.findViewById(R.id.textView_header_phone_no);
        String phone = Hawk.get("phone");
        header_phone.setText(phone);
    }


}
