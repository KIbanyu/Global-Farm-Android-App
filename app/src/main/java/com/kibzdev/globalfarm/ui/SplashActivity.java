package com.kibzdev.globalfarm.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.ui.user.LoginActivity;
import com.kibzdev.globalfarm.utils.Constants;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {
    private SharedPreferences userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        userPreference = getApplicationContext().getSharedPreferences(Constants.USER_PREFERENCE, MODE_PRIVATE);



        ImageView myImageView = (ImageView) findViewById(R.id.image_icon);
        TextView title = (TextView) findViewById(R.id.title);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView
        title.startAnimation(myFadeInAnimation);

        final Timer RunSplash = new Timer();
        TimerTask ShowSplash = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {

                if (userPreference.getBoolean(Constants.IS_LOGGED_IN, false))
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                finish();

            }
        };

        long delay = 4000;
        RunSplash.schedule(ShowSplash, delay);
    }
}