package com.kibzdev.globalfarm.ui.adsPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.kibzdev.globalfarm.BuildConfig;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.AutoCompleteAdapter;
import com.kibzdev.globalfarm.models.Photos;
import com.kibzdev.globalfarm.models.requests.PostDateRequest;
import com.kibzdev.globalfarm.models.response.BaseResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.ui.MainActivity;
import com.kibzdev.globalfarm.ui.user.ProfileActivity;
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
import java.util.Arrays;
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
import static com.kibzdev.globalfarm.utils.AppUtils.imageToString;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText productName;
    private EditText price;
    private EditText quantity;
    private EditText description;
    private EditText phoneNumber;
    private String locationName = "";
    private Spinner category_spinner;
    private String adCategory;
    private TextView error_category;
    private PlacesClient placesClient;
    private AutoCompleteAdapter adapter;
    private AutoCompleteTextView businessLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        context = CreatePostActivity.this;
        getSupportActionBar().setTitle("Add a post");

        Button add = findViewById(R.id.add);
        productName = findViewById(R.id.product);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        description = findViewById(R.id.description);
        phoneNumber = findViewById(R.id.contact);
        error_category = findViewById(R.id.error_category);
        businessLocation = findViewById(R.id.shopLocation);

        ImageView home_btn = findViewById(R.id.home);
        ImageView more_btn = findViewById(R.id.more);
        ImageView profile = findViewById(R.id.profile);



        home_btn.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        profile.setOnClickListener(view -> startActivity(new Intent(context, ProfileActivity.class)));

        add.setOnClickListener(this);

        category_spinner = findViewById(R.id.category_spinner);

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

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCt-dvBHcbsqt75VMtgr297tB1LqxzBjN4");
        }
        placesClient = Places.createClient(this);
        initAutoCompleteTextView();


        addCategories(adsCategory);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void initAutoCompleteTextView() {

        businessLocation.setThreshold(1);
        businessLocation.setOnItemClickListener(businessLocationListner);
        adapter = new AutoCompleteAdapter(this, placesClient);
        businessLocation.setAdapter(adapter);

    }

    private AdapterView.OnItemClickListener businessLocationListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(task -> {
                        keyboardEstimateManipulation(true);
                        businessLocation.clearFocus();
                        businessLocation.setText(task.getPlace().getName());
                        locationName = task.getPlace().getName();
//                        location = task.getPlace().getLatLng();


                    }).addOnFailureListener(e -> Logger.getLogger(e.getMessage()));
                }
            } catch (Exception e) {
                Logger.getLogger(e.getMessage());
            }

        }
    };

    // a method to manipulate the keyboard and hide/show fare estimate view
    private void keyboardEstimateManipulation(boolean showView) {
        if (showView) {
            //hide keyboard
            try {
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (Exception e) {
                Logger.getLogger(e.getMessage());
            }

        }
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

            case R.id.add:
                validateInputs();
                break;


            case R.id.category_spinner:
                error_category.setVisibility(View.GONE);
                break;

            default:
        }

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

        if (TextUtils.isEmpty(locationName)) {
            businessLocation.setError("Enter location");
            businessLocation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return;
        }


        PostDateRequest request = new PostDateRequest();
        request.setName(name);
        request.setCategory(adCategory);
        request.setPrice(new BigDecimal(Integer.valueOf(productPrice)));
        request.setQuantity(productQuantity);
        request.setDescription(productDescription);
        request.setPhone(phone);
        request.setLocationName(locationName);

        startActivity(new Intent(context, AttachPhotosActivity.class).putExtra("data", request));

    }


}