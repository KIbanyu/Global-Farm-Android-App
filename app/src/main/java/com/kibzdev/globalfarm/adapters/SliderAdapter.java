package com.kibzdev.globalfarm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.kibzdev.globalfarm.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Itotia Kibanyu on 6/18/2020.
 */
public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public String [] slide_images;


    public SliderAdapter(Context ctx, String [] slide_images) {
        this.context = ctx;
        this.slide_images = slide_images;
    }


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.onboarding_image);
        Picasso.get().load(slide_images[position])
                .placeholder(R.drawable.empty_grlobal)
                .fit().centerInside()
                .into(slideImageView);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
