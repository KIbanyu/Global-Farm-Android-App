package com.kibzdev.globalfarm.adapters.adapterViews;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;

/**
 * Created by Itotia Kibanyu on 8/2/2020.
 */
public class ProductListingView  extends RecyclerView.ViewHolder {
    public TextView product_name;
    public TextView product_quantity;
    public TextView product_price;
    public ImageView product_image;
    public LinearLayout item_view;
    public Button make_call;


    public ProductListingView(View v) {
        super(v);
        product_name = v.findViewById(R.id.product_name);
        product_image = v.findViewById(R.id.product_image);
        product_price = v.findViewById(R.id.product_price);
        product_quantity = v.findViewById(R.id.quantity);
        item_view = v.findViewById(R.id.item_view);
        make_call = v.findViewById(R.id.make_call);


    }
}