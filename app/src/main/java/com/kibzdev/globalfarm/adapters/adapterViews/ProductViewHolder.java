package com.kibzdev.globalfarm.adapters.adapterViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;

/**
 * Created by Itotia Kibanyu on 8/2/2020.
 */
public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView product_name;
    public ImageView product_image;


    public ProductViewHolder(View v) {
        super(v);
        product_name = v.findViewById(R.id.product_name);
        product_image = v.findViewById(R.id.product_image);


    }
}