package com.kibzdev.globalfarm.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.DealsProductAdapter;
import com.kibzdev.globalfarm.adapters.TopProductsAdapter;
import com.kibzdev.globalfarm.models.ProductModel;
import com.kibzdev.globalfarm.ui.adsPost.AdsPostPageViewer;
import com.kibzdev.globalfarm.ui.adsPost.CreatePostActivity;
import com.kibzdev.globalfarm.ui.user.ProfileActivity;
import com.kibzdev.globalfarm.utils.Constants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private Context context;
    private SharedPreferences userPreference;
    private ArrayList<ProductModel> dealsProduct = new ArrayList<>();
    private ArrayList<ProductModel> topProducts = new ArrayList<>();
    private RecyclerView dealsRecyclerView;
    private RecyclerView topProductRecyclerView;
    private LinearLayoutManager dealsLinearLayout;
    private LinearLayoutManager topProductLayout;
    private TopProductsAdapter topProductsAdapter;
    private DealsProductAdapter dealsProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        context = MainActivity.this;

        userPreference = context.getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        ImageView home_btn = findViewById(R.id.home);
        ImageView more_btn = findViewById(R.id.more);
        ImageView add = findViewById(R.id.settings);
        ImageView profile = findViewById(R.id.profile);

        add.setOnClickListener(view -> startActivity(new Intent(context, CreatePostActivity.class)));

        profile.setOnClickListener(view -> startActivity(new Intent(context, ProfileActivity.class)));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));

        View hView = navigationView.getHeaderView(0);
        TextView name = hView.findViewById(R.id.name);
        Menu nav_Menu = navigationView.getMenu();

        dealsRecyclerView = findViewById(R.id.deals_recycler_view);
        topProductRecyclerView = findViewById(R.id.top_products_recycler_view);


        topProductsAdapter = new TopProductsAdapter(topProducts, this);
        topProductLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        topProductRecyclerView.setLayoutManager(topProductLayout);
        topProductRecyclerView.setAdapter(topProductsAdapter);


        dealsProductAdapter = new DealsProductAdapter(dealsProduct, this);
        dealsLinearLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        dealsRecyclerView.setLayoutManager(dealsLinearLayout);
        dealsRecyclerView.setAdapter(dealsProductAdapter);

        name.setText(userPreference.getString(Constants.NAME, null));

        RelativeLayout farm_products = findViewById(R.id.farm_products);
        RelativeLayout live_Stock = findViewById(R.id.live_stock);
        RelativeLayout agrovets = findViewById(R.id.agrovet);
        RelativeLayout pets = findViewById(R.id.pet);

        farm_products.setOnClickListener(v ->startTabActivity(0));
        live_Stock.setOnClickListener(v ->startTabActivity(1));
        agrovets.setOnClickListener(v ->startTabActivity(2));
        pets.setOnClickListener(v ->startTabActivity(3));


        getDealsProducts();
        getTopProducts();
    }


    public void startTabActivity(int position) {
        Intent nextScreen = new Intent(getApplicationContext(), AdsPostPageViewer.class);
        nextScreen.putExtra("position", position);
        startActivity(nextScreen);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(context, ProfileActivity.class));
                break;

            case R.id.add_post:
                startActivity(new Intent(context, CreatePostActivity.class));
                break;
        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<ProductModel> getDealsProducts() {

        for (int i = 0; i < 7; i++) {
            ProductModel productModel = new ProductModel();
            productModel.setImage(R.drawable.pets);
            productModel.setName("Dog Dog");
            dealsProduct.add(productModel);
        }

        return dealsProduct;
    }

    public List<ProductModel> getTopProducts() {

        for (int i = 0; i < 7; i++) {
            ProductModel productModel = new ProductModel();
            productModel.setImage(R.drawable.agrovet);
            productModel.setName("Agrovet");
            topProducts.add(productModel);
        }

        return topProducts;
    }
}