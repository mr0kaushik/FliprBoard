package com.board.flipr.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.board.flipr.LoginActivity;
import com.board.flipr.MainActivity;
import com.board.flipr.R;
import com.board.flipr.Utils.SharedPrefHelper;
import com.board.flipr.WalkThroughActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(() -> {


            if (SharedPrefHelper.getInstance(SplashActivity.this).hasVisitedWalkThrough()) {


                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (mUser != null) {
                    /*---------USER DASHBOARD--------*/
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    /*---------LOGIN--------*/
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                }
            } else {
                /*---------WALK THROUGH--------*/
                Intent i = new Intent(SplashActivity.this, WalkThroughActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
