package com.kibzdev.globalfarm.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.utils.Constants;

import java.util.prefs.Preferences;

public class ProfileActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences userPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Profile");
        userPreference =  context.getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE);

        TextView name = findViewById(R.id.userName);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView logout = findViewById(R.id.logout);

        name.setText(userPreference.getString(Constants.NAME, null));
        phone.setText(userPreference.getString(Constants.PHONE_NUMBER, null));
        email.setText(userPreference.getString(Constants.EMAIL, null));

        logout.setOnClickListener(v -> showLogOutDialog());


    }

    private void logoutUser() {

        SharedPreferences.Editor editor = userPreference.edit();
        editor.clear();
        editor.putBoolean(Constants.IS_LOGGED_IN, false);
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void showLogOutDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Log out")
                .setMessage("Are you sure you want to logout?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        logoutUser();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}