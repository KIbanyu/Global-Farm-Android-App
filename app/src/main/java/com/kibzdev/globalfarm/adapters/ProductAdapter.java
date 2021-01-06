package com.kibzdev.globalfarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.adapterViews.ProductListingView;
import com.kibzdev.globalfarm.models.ProductModel;
import com.kibzdev.globalfarm.ui.adsPost.SingleProductView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Itotia Kibanyu on 8/2/2020.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductListingView> {
    private ArrayList<ProductModel> products;

    private Context context;


    public ProductAdapter(ArrayList<ProductModel> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductListingView onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_view_layout, parent, false);
        ProductListingView holder = new ProductListingView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductListingView holder, int position) {

        holder.product_name.setText(products.get(position).getName());
        holder.product_quantity.setText(products.get(position).getProduct_quantity());
        holder.product_price.setText(String.format("KES %s", products.get(position).getPrice().intValue()));
        holder.make_call.setOnClickListener(view -> makeCall());

        Picasso.get().load(products.get(position).getImagePosts()[0])
                .placeholder(R.drawable.empty_grlobal)
                .fit().centerInside()
                .into(holder.product_image);


        holder.item_view.setOnClickListener(v -> {

            Intent nextScreen = new Intent(context, SingleProductView.class);
            nextScreen.putExtra("data", products.get(position));
            context.startActivity(nextScreen);


        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "0723995657"));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}