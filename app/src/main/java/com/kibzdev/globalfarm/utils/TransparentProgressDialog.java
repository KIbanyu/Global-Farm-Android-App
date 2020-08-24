package com.kibzdev.globalfarm.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kibzdev.globalfarm.R;


public class TransparentProgressDialog extends Dialog {

    private AppCompatImageView iv;

    public TransparentProgressDialog(Context context, String dialogText, int resourceIdOfImage) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        AppCompatTextView txtInst = new AppCompatTextView(context);
        txtInst.setText(dialogText);
        txtInst.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        txtInst.setTextColor(Color.WHITE);
        txtInst.setTextSize(16);
        txtInst.setGravity(Gravity.CENTER_HORIZONTAL);
        txtInst.setBackgroundColor(Color.TRANSPARENT);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        iv = new AppCompatImageView(context);
        iv.setImageResource(resourceIdOfImage);

        layout.addView(iv, params);
        layout.addView(txtInst, params);
        addContentView(layout, params);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);
        iv.setAnimation(anim);
        iv.startAnimation(anim);
    }
}