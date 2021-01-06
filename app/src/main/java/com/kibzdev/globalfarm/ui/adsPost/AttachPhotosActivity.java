package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.ImagesAdapter;
import com.kibzdev.globalfarm.fragments.bottomsheets.UploadPhotoFromBottomSheet;
import com.kibzdev.globalfarm.models.ImagesModel;
import com.kibzdev.globalfarm.models.requests.PostDateRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.utils.Constants;
import com.kibzdev.globalfarm.utils.SpacesItemDecoration;
import com.kibzdev.globalfarm.utils.TransparentProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kibzdev.globalfarm.utils.AppUtils.convertBitmapToBase64;
import static com.kibzdev.globalfarm.utils.AppUtils.decodeSampledBitmapFromFile;
import static com.kibzdev.globalfarm.utils.AppUtils.getImageUri;
import static com.kibzdev.globalfarm.utils.AppUtils.getPathFromURI;
import static com.kibzdev.globalfarm.utils.AppUtils.getRealPathFromURI;

public class AttachPhotosActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private String photo_one;
    private String photo_two;
    private String photo_three;
    private String photo_four;
    private static final int PHOTO_ONE = 1;
    private static final int PHOTO_TWO = 2;
    private static final int PHOTO_THREE = 3;
    private static final int PHOTO_FOUR = 4;
    private int photoToPick;
    private int gallery = 1;
    private int camera = 2;
    private SharedPreferences userPreference;
    private RecyclerView images_recycler_view;
    private ImagesAdapter imagesAdapter;
    private List<ImagesModel> imagesModelList = new ArrayList<>();
    private String statusString = "";
    private int photoPosition;
    private ImagesModel singleImageModel;
    private List<MultipartBody.Part> filesToUpload = new ArrayList<>();
    private RequestBody requestFile;
    private MultipartBody.Part fileToUpload;
    private TransparentProgressDialog transparentProgressDialog;
    private PostDateRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_photos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attach Photos");
        getSupportActionBar().setElevation(0);

        context = AttachPhotosActivity.this;
        userPreference = context.getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE);
        transparentProgressDialog = new TransparentProgressDialog(AttachPhotosActivity.this, "", R.drawable.ic_processing);

        request = (PostDateRequest) getIntent().getSerializableExtra("data");

        Button previous = findViewById(R.id.previous);
        Button submit_btn = findViewById(R.id.submit_btn);


        images_recycler_view = findViewById(R.id.images_recycler_view);
        GridLayoutManager mGrid = new GridLayoutManager(context, 2);
        images_recycler_view.setLayoutManager(mGrid);
        images_recycler_view.setHasFixedSize(true);
        images_recycler_view.addItemDecoration(new SpacesItemDecoration(2, 12, false));

        imagesAdapter = new ImagesAdapter(AttachPhotosActivity.this, context, imagesModelList);
        images_recycler_view.setAdapter(imagesAdapter);
        submit_btn.setOnClickListener(this);
        previous.setOnClickListener(this);

        createImagesField();


    }

    public void openBottomSheet(int position, ImagesModel singleImageModelReceived) {
        photoPosition = position;
        singleImageModel = singleImageModelReceived;

        if (checkPermission()) {
            UploadPhotoFromBottomSheet uploadPhotoFromBottomSheet = new UploadPhotoFromBottomSheet(this, singleImageModel.getId());
            uploadPhotoFromBottomSheet.show(getSupportFragmentManager(), uploadPhotoFromBottomSheet.getTag());
        } else {
            Toast.makeText(context, "Enable permissions to continue", Toast.LENGTH_LONG).show();
        }


    }

    public void selectPhoto(int type, String pickFrom) {
        photoToPick = type;

        if (pickFrom.equalsIgnoreCase(Constants.GALLERY))
            choosePhotoFromGallery();
        else
            takePhotoFromCamera();

    }

    public void removePhoto(int position, ImagesModel singleImageModel) {

        singleImageModel.setImage(null);
        imagesModelList.set(position, singleImageModel);
        imagesAdapter.notifyDataSetChanged();

        if (singleImageModel.getImageType().equalsIgnoreCase("photo1")) {
            photo_one = "";
            return;
        }

        if (singleImageModel.getImageType().equalsIgnoreCase("photo2")) {
            photo_two = "";
            return;
        }


        if (singleImageModel.getImageType().equalsIgnoreCase("photo3")) {
            photo_three = "";
            return;
        }


        if (singleImageModel.getImageType().equalsIgnoreCase("photo4")) {
            photo_four = "";
            return;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit_btn:
                validatePhotos();
                break;

            case R.id.previous:
                finish();
                break;


            default:
                break;
        }

    }

    public void validatePhotos() {

        if (imagesModelList.size() < 2) {
            Toast.makeText(context, "Upload at least 2 photos", Toast.LENGTH_LONG).show();
            return;
        }


        submitPost();

    }

    public void createImagesField() {
        ImagesModel imagesModel1 = new ImagesModel();
        ImagesModel imagesModel2 = new ImagesModel();
        ImagesModel imagesModel3 = new ImagesModel();
        ImagesModel imagesModel4 = new ImagesModel();

        imagesModel1.setTitle("Photo 1");
        imagesModel1.setImageType("photo1");
        imagesModel1.setHeaderOne("Upload");
        imagesModel1.setHeaderTwo("Photo");
        imagesModel1.setHeaderThree("1");
        imagesModel1.setId(PHOTO_ONE);

        imagesModel2.setTitle("Photo 2");
        imagesModel2.setImageType("photo2");
        imagesModel2.setHeaderOne("Upload");
        imagesModel2.setHeaderTwo("Photo");
        imagesModel2.setHeaderThree("2");
        imagesModel2.setId(PHOTO_TWO);

        imagesModel3.setTitle("Photo 3");
        imagesModel3.setImageType("photo3");
        imagesModel3.setHeaderOne("Upload");
        imagesModel3.setHeaderTwo("Photo");
        imagesModel3.setHeaderThree("3");
        imagesModel3.setId(PHOTO_THREE);


        imagesModel4.setTitle("Photo 4");
        imagesModel4.setImageType("photo4");
        imagesModel4.setHeaderOne("Upload");
        imagesModel4.setHeaderTwo("Photo");
        imagesModel4.setHeaderThree("4");
        imagesModel4.setId(PHOTO_FOUR);


        imagesModelList.add(imagesModel1);
        imagesModelList.add(imagesModel2);
        imagesModelList.add(imagesModel3);
        imagesModelList.add(imagesModel4);

        imagesAdapter.notifyDataSetChanged();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }

        if (requestCode == gallery) {
            if (data != null) {
                Uri imageUri = data.getData();
                String imagePath = getPathFromURI(imageUri, context);

                try {
                    Bitmap bitmap = decodeSampledBitmapFromFile(imagePath, 200, 200);
                    runOnUiThread(() -> setImage(bitmap));
                    File file = new File(getRealPathFromURI(imageUri, context));
                    //creating request body for file
                    requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
                    // Parsing any Media type file    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData("file" + filesToUpload.size(), file.getName(), requestFile);
                    filesToUpload.add(fileToUpload);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else if (requestCode == camera) {

            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {

                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    String imagePath = getPathFromURI(tempUri, context);
                    try {
                        Bitmap lastImage = decodeSampledBitmapFromFile(imagePath, 200, 200);
                        runOnUiThread(() -> setImage(lastImage));
                        File file = new File(getRealPathFromURI(tempUri, context));
                        //creating request body for file
                        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(tempUri)), file);
                        // Parsing any Media type file    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                        fileToUpload = MultipartBody.Part.createFormData("file" + filesToUpload.size(), file.getName(), requestFile);
                        filesToUpload.add(fileToUpload);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, "Failed to upload photo", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(context, "Failed to upload photo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void choosePhotoFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, gallery);

    }

    public void takePhotoFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, camera);
        }


    }

    public boolean checkPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (int i = 0; i < perms.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), perms[i]) != PackageManager.PERMISSION_GRANTED)

                try {
                    ActivityCompat.requestPermissions(this, perms, 100);

                } catch (Exception e) {

                    Logger.getLogger(e.getMessage());
                }

            else
                return true;
        }
        return false;
    }


    public void addImage(ImagesModel imagesModel) {

        imagesModelList.set(photoPosition, imagesModel);
        imagesAdapter.notifyDataSetChanged();
    }


    public void setImage(Bitmap rotatedBitmap) {


        if (photoToPick == PHOTO_ONE) {
            singleImageModel.setImage(rotatedBitmap);
            addImage(singleImageModel);
            photo_one = convertBitmapToBase64(rotatedBitmap);

        }

        if (photoToPick == PHOTO_TWO) {
            singleImageModel.setImage(rotatedBitmap);

            addImage(singleImageModel);
            photo_two = convertBitmapToBase64(rotatedBitmap);
        }

        if (photoToPick == PHOTO_THREE) {
            singleImageModel.setImage(rotatedBitmap);
            addImage(singleImageModel);
            photo_three = convertBitmapToBase64(rotatedBitmap);
        }

        if (photoToPick == PHOTO_FOUR) {
            singleImageModel.setImage(rotatedBitmap);
            addImage(singleImageModel);

            photo_four = convertBitmapToBase64(rotatedBitmap);
        }

    }

    public void submitPost() {

        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<BaseResponse> call = apiInterface.addPost(filesToUpload.get(0),
                filesToUpload.get(1), filesToUpload.get(2), filesToUpload.size(),
                request.getName(), request.getCategory(), request.getPrice()
                , request.getQuantity(), request.getDescription(), request.getLocationName(),
                userPreference.getLong(Constants.USER_ID, 0), request.getPhone());


        transparentProgressDialog.show();
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                transparentProgressDialog.dismiss();

                if (null == response.body()) {
                    Toast.makeText(context, "Error occurred while processing your request", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!response.body().getStatus().equalsIgnoreCase("00")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                filesToUpload.clear();

                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                finish();

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                transparentProgressDialog.dismiss();


            }
        });


    }

}