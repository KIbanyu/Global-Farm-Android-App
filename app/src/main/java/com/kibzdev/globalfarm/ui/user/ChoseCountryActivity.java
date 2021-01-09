package com.kibzdev.globalfarm.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.adapters.CountriesListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChoseCountryActivity extends AppCompatActivity {

    private Context context;
    private ListView countries_list_view;
    private CountriesListAdapter adapter;
    private EditText inputSearch;
    private boolean isSearch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_country);
        context = ChoseCountryActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Select country");

        countries_list_view = findViewById(R.id.countries);

        String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);
        adapter = new CountriesListAdapter(this, recourseList);
        countries_list_view.setAdapter(adapter);

        inputSearch = findViewById(R.id.inputSearch);


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count >= 2) {
//                    searchFromList(s.length(), s, count);
                } else {
                    adapter = new CountriesListAdapter(ChoseCountryActivity.this, recourseList);
                    countries_list_view.setAdapter(adapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        countries_list_view.setOnItemClickListener((parent, view, position, id) -> {

            String[] country = recourseList[position].split(",");
            Intent intent = new Intent();
            intent.putExtra("country", adapter.getCountryDisplayName(country[1]));
            intent.putExtra("countryCode", adapter.getCountryCode(country[1]));
            setResult(250, intent);
            finish();

        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}