package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.SliderAdapter;
import com.kibzdev.globalfarm.models.ProductModel;


public class SingleProductView extends AppCompatActivity {

    private LinearLayout slider_layout;
    private Context context;
    private SliderAdapter sliderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        context = SingleProductView.this;
        ProductModel productModel = (ProductModel) getIntent().getSerializableExtra("data");

        getSupportActionBar().setTitle(productModel.getName());

        TextView itemName = findViewById(R.id.product_name);
        TextView price = findViewById(R.id.price);
        TextView quantity = findViewById(R.id.quantity);
        TextView description = findViewById(R.id.description);

        ViewPager viewPager = findViewById(R.id.view_pager);

        slider_layout = findViewById(R.id.slider_layout);
        sliderAdapter = new SliderAdapter(context, productModel.getImagePosts());
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);


        itemName.setText(productModel.getName());
        price.setText(String.format("KES %s", productModel.getPrice().intValue()));
        quantity.setText(productModel.getProduct_quantity());
        description.setText(productModel.getDescription());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    public void addDotsIndicator(int position) {

        TextView[] mDots = new TextView[sliderAdapter.getCount()];
        slider_layout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(55);
            mDots[i].setTextColor(context.getResources().getColor(R.color.white));

            slider_layout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(context.getResources().getColor(R.color.profit));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            // Do something
        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do something
        }
    };
}