package com.kibzdev.globalfarm.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.ProductAdapter;
import com.kibzdev.globalfarm.models.ProductModel;
import com.kibzdev.globalfarm.models.response.models.response.GetPostResponse;
import com.kibzdev.globalfarm.models.response.rest.ApiInterface;
import com.kibzdev.globalfarm.models.response.rest.RestAdapter;
import com.kibzdev.globalfarm.utils.Constants;
import com.kibzdev.globalfarm.utils.SpacesItemDecoration;
import com.kibzdev.globalfarm.utils.TransparentProgressDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FarmProductsFragment extends Fragment {

    private ArrayList<ProductModel> productModels = new ArrayList<>();
    private static String TAG = "Fragment Beer";
    private RecyclerView agrovet_recycler_view;
    Context appContext;
    private SharedPreferences prefs;
    private TransparentProgressDialog transparentProgressDialog;
    private ProductAdapter adapter;
    private List<GetPostResponse.PostResponse> postResponses;

    public FarmProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_farm_products_adapter, container, false);

        appContext = getActivity();

        agrovet_recycler_view = rootView.findViewById(R.id.agrovet_recycler_view);
        GridLayoutManager mGrid = new GridLayoutManager(appContext, 2);
        agrovet_recycler_view.setLayoutManager(mGrid);
        agrovet_recycler_view.setHasFixedSize(true);
        agrovet_recycler_view.addItemDecoration(new SpacesItemDecoration(2, 12, false));

        postResponses = new ArrayList<>();
        adapter = new ProductAdapter(productModels, getActivity());
        agrovet_recycler_view.setAdapter(adapter);
        transparentProgressDialog = new TransparentProgressDialog(appContext, "", R.drawable.ic_processing);


        getFarmProducts();

        return rootView;
    }
    public void getFarmProducts()
    {

        ApiInterface apiInterface = RestAdapter.createAPI();
        Call<GetPostResponse> call = apiInterface.getPosts(Constants.FARM_PRODUCTS);

        transparentProgressDialog.show();
        call.enqueue(new Callback<GetPostResponse>() {
            @Override
            public void onResponse(Call<GetPostResponse> call, Response<GetPostResponse> response) {

                transparentProgressDialog.dismiss();

                if (null == response.body()) {
                    Toast.makeText(appContext, "Error occurred while processing your request", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!response.body().getStatus().equalsIgnoreCase("00")) {
                    Toast.makeText(appContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                productModels.clear();
                postResponses.addAll(response.body().getData());

                for (GetPostResponse.PostResponse post: postResponses)
                {
                    ProductModel productModel = new ProductModel();

                    String images = post.getPhotosList();
                    if (images.contains(","))
                    {
                        String [] parts = images.split(",");

                        productModel.setImagePosts(parts);

                    }else
                    {
                        productModel.setImagePosts(new String[]{images});
                    }
                    productModel.setName(post.getName());
                    productModel.setPrice(post.getPrice());
                    productModel.setDescription(post.getDescription());
                    productModel.setProduct_quantity(post.getQuantity());
                    productModels.add(productModel);

                }

                adapter.notifyDataSetChanged();

                Log.e(TAG, "Response is: " + new Gson().toJson(productModels));



            }

            @Override
            public void onFailure(Call<GetPostResponse> call, Throwable t) {
                transparentProgressDialog.dismiss();


            }
        });


    }
}