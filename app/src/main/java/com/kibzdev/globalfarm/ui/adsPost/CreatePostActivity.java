package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.models.Photos;
import com.kibzdev.globalfarm.models.requests.PostDateRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.utils.AppUtils;
import com.kibzdev.globalfarm.utils.MySingleton;
import com.kibzdev.globalfarm.utils.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.VolleyLog.TAG;
import static com.kibzdev.globalfarm.utils.AppUtils.generateRandomChars;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private RelativeLayout relImage1;
    private RelativeLayout relImage2;
    private RelativeLayout relImage3;
    private int gallery = 1;
    private int camera = 2;
    private Uri imageUri;
    private RequestBody requestFile;
    private List<Photos> photosList = new ArrayList<>();
    private TextView addImageDescription;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText description;
    private EditText phoneNumber;
    private Button add;
    private TransparentProgressDialog transparentProgressDialog;
    private String responseUrl;
    private List<String> imageLinks = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();
    private int position = 0;
    private PostDateRequest request;
    private Spinner category_spinner;
    private String adCategory;
    private TextView error_category;
    private MultipartBody.Part fileToUpload;
    private List<MultipartBody.Part> filesToUpload = new ArrayList<>();
    private List<MultipartBody.Part> fileAdded = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        context = CreatePostActivity.this;
        getSupportActionBar().setTitle("Add a post");

        LinearLayout addImage = findViewById(R.id.addImage);
        transparentProgressDialog = new TransparentProgressDialog(CreatePostActivity.this, "", R.drawable.ic_processing);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        ImageView clearImage1 = findViewById(R.id.clearImage1);
        ImageView clearImage2 = findViewById(R.id.clearImage2);
        ImageView clearImage3 = findViewById(R.id.clearImage3);
        addImageDescription = findViewById(R.id.addImageDescription);
        add = findViewById(R.id.add);
        productName = findViewById(R.id.product);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        description = findViewById(R.id.description);
        phoneNumber = findViewById(R.id.contact);
        error_category = findViewById(R.id.error_category);

        addImage.setOnClickListener(this);
        clearImage1.setOnClickListener(this);
        clearImage2.setOnClickListener(this);
        clearImage3.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        add.setOnClickListener(this);
        image3.setOnClickListener(this);

        relImage1 = findViewById(R.id.relImage1);
        relImage2 = findViewById(R.id.relImage2);
        relImage3 = findViewById(R.id.relImage3);
        category_spinner = findViewById(R.id.category_spinner);

        photosList.clear();
        List<String> adsCategory = new ArrayList<>();
        adsCategory.add("");
        adsCategory.add("Farm products");
        adsCategory.add("Livestock");
        adsCategory.add("Agrovet");
        adsCategory.add("Pets");

        category_spinner.setOnTouchListener((v, event) -> {

            error_category.setVisibility(View.GONE);
            return false;
        });


        addCategories(adsCategory);


    }

    public void addCategories(List<String> categories) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(dataAdapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adCategory = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // do something

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.clearImage1:
                relImage1.setVisibility(View.GONE);
                image1.setImageDrawable(null);
                photosList.remove(0);

                if (photosList.isEmpty()) {
                    addImageDescription.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.add:
                validateInputs();
                break;

            case R.id.clearImage2:
                relImage2.setVisibility(View.GONE);
                image2.setImageDrawable(null);
                if (photosList.size() == 3) {
                    photosList.remove(1);
                } else if (photosList.size() == 2) {
                    photosList.remove(1);
                } else {
                    photosList.remove(0);
                }

                if (photosList.isEmpty()) {
                    addImageDescription.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.category_spinner:
                error_category.setVisibility(View.GONE);
                break;

            case R.id.clearImage3:
                relImage3.setVisibility(View.GONE);
                image3.setImageDrawable(null);
                if (photosList.size() == 3) {
                    photosList.remove(2);
                } else if (photosList.size() == 2) {
                    photosList.remove(1);
                } else {
                    photosList.remove(0);
                }


                if (photosList.isEmpty()) {
                    addImageDescription.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.image1:
                Bitmap bitmap = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                viewImage(bitmap);
                break;

            case R.id.image2:
                Bitmap bitmap2 = ((BitmapDrawable) image2.getDrawable()).getBitmap();
                viewImage(bitmap2);
                break;
            case R.id.image3:
                Bitmap bitmap3 = ((BitmapDrawable) image3.getDrawable()).getBitmap();
                viewImage(bitmap3);
                break;

            case R.id.addImage:
                if (photosList.size() == 3) {
                    Toast.makeText(context, "Only three photos are needed", Toast.LENGTH_LONG).show();

                } else {

                    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    for (int i = 0; i < perms.length; i++) {
                        if (ContextCompat.checkSelfPermission(this, perms[i]) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, perms, 100);
                            break;
                        } else {
                            showPictureDialog();
                        }
                    }


                }

                break;

            default:
        }

    }

    private void showPictureDialog() {


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.pick_photo_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView cameraTxtV = dialoglayout.findViewById(R.id.fromCamera);
        TextView galleryTxtV = dialoglayout.findViewById(R.id.fromgallery);
        TextView close = dialoglayout.findViewById(R.id.close);

        builder.setCancelable(false);
        builder.setView(dialoglayout);
        final AlertDialog ad = builder.show();

        cameraTxtV.setOnClickListener(v -> {
            takePhotoFromCamera();
            ad.dismiss();
        });

        galleryTxtV.setOnClickListener(v -> {
            choosePhotoFromGallary();
            ad.dismiss();

        });

        close.setOnClickListener(v -> ad.dismiss());


    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, gallery);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }

        Photos photos = new Photos();

        if (requestCode == gallery) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    Matrix matrix = new Matrix();

                    matrix.postRotate(0);

                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    String fileName = generateRandomChars("abcdefghijklmnopqrstuvwxyz", 4);
                    //creating a file
                    File file = new File(getRealPathFromURI(contentURI));
                    //creating request body for file
                    requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(contentURI)), file);

                    // Parsing any Media type file    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    fileToUpload = MultipartBody.Part.createFormData(fileName, file.getName(), requestFile);

                    filesToUpload.add(fileToUpload);

                    photos.setPhoto(imageToString(rotatedBitmap));
                    photosList.add(photos);

                    if (!photosList.isEmpty()) {
                        addImageDescription.setVisibility(View.GONE);
                        if (image1.getDrawable() == null) {
                            relImage1.setVisibility(View.VISIBLE);
                            image1.setImageBitmap(rotatedBitmap);
                        } else if (image2.getDrawable() == null) {
                            relImage2.setVisibility(View.VISIBLE);
                            image2.setImageBitmap(rotatedBitmap);
                        } else if (image3.getDrawable() == null) {
                            relImage3.setVisibility(View.VISIBLE);
                            image3.setImageBitmap(rotatedBitmap);
                        }
                    }

                } catch (IOException e) {
                    Logger.getLogger(e.getMessage());
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == camera) {
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                Logger.getLogger(e.getMessage());
            }

            Matrix matrix = new Matrix();

            matrix.postRotate(0);


            Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

            String fileName = generateRandomChars("abcdefghijklmnopqrstuvwxyz", 4);
            //creating a file
            File file = new File(getRealPathFromURI(imageUri));
            //creating request body for file
            requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            fileToUpload = MultipartBody.Part.createFormData(fileName, file.getName(), requestFile);

            filesToUpload.add(fileToUpload);
            photos.setPhoto(imageToString(rotatedBitmap));
            photosList.add(photos);

            if (!photosList.isEmpty()) {

                addImageDescription.setVisibility(View.GONE);
                if (image1.getDrawable() == null) {
                    relImage1.setVisibility(View.VISIBLE);
                    image1.setImageBitmap(rotatedBitmap);
                } else if (image2.getDrawable() == null) {
                    relImage2.setVisibility(View.VISIBLE);
                    image2.setImageBitmap(rotatedBitmap);
                } else if (image3.getDrawable() == null) {
                    relImage3.setVisibility(View.VISIBLE);
                    image3.setImageBitmap(rotatedBitmap);
                }

            }

        }
    }


    private void takePhotoFromCamera() {
        ContentValues values = new ContentValues();
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, camera);
    }

    public void viewImage(Bitmap bitmap) {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.imageviewer_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView imageView = dialoglayout.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
        builder.setCancelable(true);
        builder.setView(dialoglayout);

    }


    public void validateInputs() {


        String name = productName.getText().toString().trim();
        String productPrice = price.getText().toString().trim();
        String productQuantity = quantity.getText().toString().trim();
        String productDescription = description.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            productName.setError("Name is required");
            productName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(adCategory)) {
            error_category.setVisibility(View.VISIBLE);
            return;
        }


        if (TextUtils.isEmpty(productPrice)) {
            price.setError("Price is required");
            price.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(productQuantity)) {
            quantity.setError("Quantity is required");
            quantity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(productDescription)) {
            description.setError("Description is required");
            description.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return;
        }

        if (photosList.isEmpty()) {
            Toast.makeText(context, "Attach photos", Toast.LENGTH_LONG).show();
            return;
        }

        request = new PostDateRequest();
        request.setName(name);
        request.setCategory(adCategory);
        request.setPrice(new BigDecimal(Integer.valueOf(productPrice)));
        request.setQuantity(productQuantity);
        request.setDescription(productDescription);
        request.setPhone(phone);

//        submitImages();
        submitPost(stringBuilder);

    }

    public void submitImages() {


        for (Photos imageUrl : photosList) {
            uploadImages(imageUrl.getPhoto());
        }


    }


    public String uploadImages(String imageUrl) {


        transparentProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUtils.UPLOAD_IMAGE,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        position = position + 1;
                        responseUrl = jsonObject.getString("response");
                        stringBuilder.append(responseUrl).append(",");


                        if (position == photosList.size()) {
                            submitPost(stringBuilder);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            transparentProgressDialog.dismiss();

            Toast.makeText(context, "Failed to upload your post, try again later", Toast.LENGTH_LONG).show();

            System.out.println(">>>>>>>>>>>>> Error is " + new Gson().toJson(error));


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("imageurl", generateRandomChars("abcdefghijklmnopqrstuvwxyz", 8));
                params.put("image", imageUrl);

                System.out.println(">>>>>>>>>>>>>>> " + params);

                return params;

            }
        };

        MySingleton.getInstance(CreatePostActivity.this).addRoRequestQue(stringRequest);

        return responseUrl;
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }


    public void submitPost(StringBuilder input) {



        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<BaseResponse> call = apiInterface.addPost(fileToUpload, request.getName(), request.getCategory(), request.getPrice()
                , request.getQuantity(), request.getDescription(), "Juja", request.getPhone());


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

                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                finish();

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                transparentProgressDialog.dismiss();


            }
        });


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}