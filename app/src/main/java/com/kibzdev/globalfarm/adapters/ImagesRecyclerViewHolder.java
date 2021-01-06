package com.kibzdev.globalfarm.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;


/**
 * Created by Itotia Kibanyu on 8/26/2020.
 */
public class ImagesRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView header_one;
    public TextView header_two;
    public TextView header_three;
    public ImageView imageView;
    public AppCompatImageView img_close;
    public LinearLayout lyt_title;
    public RelativeLayout ryt_profile_photo;

    public ImagesRecyclerViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.image);
        img_close = itemView.findViewById(R.id.img_close);
        header_one = itemView.findViewById(R.id.header_one);
        header_two = itemView.findViewById(R.id.header_two);
        header_three = itemView.findViewById(R.id.header_three);
        lyt_title = itemView.findViewById(R.id.lyt_title);
        ryt_profile_photo = itemView.findViewById(R.id.ryt_profile_photo);


    }
}