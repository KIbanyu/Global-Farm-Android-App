package com.kibzdev.globalfarm.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.models.ImagesModel;
import com.kibzdev.globalfarm.ui.adsPost.AttachPhotosActivity;

import java.util.List;

/**
 * Created by Itotia Kibanyu on 8/26/2020.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesRecyclerViewHolder> {


    private Context context;
    private Activity activity;



    private List<ImagesModel> imagesModelList;

    public ImagesAdapter(Activity fromActivity, Context context, List<ImagesModel> imagesModels) {
        this.context = context;
        this.imagesModelList = imagesModels;
        this.activity = fromActivity;

    }

    @Override
    public ImagesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_layout, parent, false);
        ImagesRecyclerViewHolder productHolder = new ImagesRecyclerViewHolder(layoutView);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final ImagesRecyclerViewHolder holder, int position) {
        ImagesModel singleImage = imagesModelList.get(position);

        if (null == singleImage.getImage()) {
            holder.lyt_title.setVisibility(View.VISIBLE);
            holder.img_close.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.title.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(singleImage.getImage())
                    .override(600, 200)
                    .fitCenter()
                    .into(holder.imageView);
            holder.lyt_title.setVisibility(View.GONE);
            holder.img_close.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.title.setVisibility(View.VISIBLE);
        }

        holder.title.setText(singleImage.getTitle());

        holder.header_three.setText(singleImage.getHeaderThree());
        holder.header_two.setText(singleImage.getHeaderTwo());
        holder.header_one.setText(singleImage.getHeaderOne());

        holder.ryt_profile_photo.setOnClickListener(v -> {

            ((AttachPhotosActivity) activity).openBottomSheet(position, singleImage);

        });


        holder.img_close.setOnClickListener(v -> {

            ((AttachPhotosActivity) activity).removePhoto(position, singleImage);

        });


    }

    @Override
    public int getItemCount() {
        return imagesModelList.size();
    }

}

