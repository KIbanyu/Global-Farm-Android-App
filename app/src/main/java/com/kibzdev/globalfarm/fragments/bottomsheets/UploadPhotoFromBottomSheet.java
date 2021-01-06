package com.kibzdev.globalfarm.fragments.bottomsheets;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.ui.adsPost.AttachPhotosActivity;
import com.kibzdev.globalfarm.utils.Constants;

/**
 * Created by Itotia Kibanyu on 8/26/2020.
 */
public
class UploadPhotoFromBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private Button from_camera;
    private Button from_gallery;
    private Activity activity;
    private int pickerNumber;


    public UploadPhotoFromBottomSheet(Activity activity, int pickerNumber) {

        this.activity = activity;
        this.pickerNumber = pickerNumber;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.attach_photo_options, container, false);
        from_camera = rootView.findViewById(R.id.camera);
        from_gallery = rootView.findViewById(R.id.gallery);
        from_camera.setOnClickListener(this);
        from_gallery.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camera:

                ((AttachPhotosActivity) activity).selectPhoto(pickerNumber, Constants.CAMERA);

                dismiss();
                break;

            case R.id.gallery:

                ((AttachPhotosActivity) activity).selectPhoto(pickerNumber, Constants.GALLERY);


                dismiss();
                break;
        }


    }


}