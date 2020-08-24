package com.kibzdev.globalfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.adapterViews.ProductViewHolder;
import com.kibzdev.globalfarm.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Itotia Kibanyu on 8/2/2020.
 */
public class TopProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {
   private ArrayList<ProductModel> products;

    private Context context;


    public TopProductsAdapter(ArrayList<ProductModel> products, Context context) {
        this.products=products;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_layout, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {

        holder.product_name.setText(products.get(position).getName());
        Picasso.get().load(products.get(position).getImage())
                .placeholder(R.drawable.pets)
                .fit().centerInside()
                .into(holder.product_image);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}