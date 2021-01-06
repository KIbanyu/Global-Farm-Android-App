package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.TabAdapter;
import com.kibzdev.globalfarm.fragments.AgrovetFragment;
import com.kibzdev.globalfarm.fragments.FarmProductsFragment;
import com.kibzdev.globalfarm.fragments.LiveStockFragment;
import com.kibzdev.globalfarm.fragments.PetsFragment;
import com.kibzdev.globalfarm.ui.MainActivity;
import com.kibzdev.globalfarm.ui.user.ProfileActivity;

public class AdsPostPageViewer extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_post_page_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Global farm");
        getSupportActionBar().setElevation(0);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        adapter.addFragment(new FarmProductsFragment(), "Farm Products");
        adapter.addFragment(new LiveStockFragment(), "Livestock");
        adapter.addFragment(new AgrovetFragment(), "Agrovet");
        adapter.addFragment(new PetsFragment(), "Pets");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageView home_btn = findViewById(R.id.home);
        ImageView more_btn = findViewById(R.id.more);
        ImageView profile = findViewById(R.id.profile);
        ImageView add = findViewById(R.id.settings);

        home_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        add.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreatePostActivity.class)));
        profile.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));


        int pos = viewPager.getCurrentItem();
        activeFragment = adapter.getItem(pos);
        viewPager.setCurrentItem(getIntent().getIntExtra("position",0));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}