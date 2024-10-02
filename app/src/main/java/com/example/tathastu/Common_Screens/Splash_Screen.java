package com.example.tathastu.Common_Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tathastu.Admin_Package.Admin_DashBoard.Admin_DashBoard_Screen;
import com.example.tathastu.Common_Screens.Intro.Intro_Starting_Screen;
import com.example.tathastu.NGO_Package.NGO_DashBoard.NGO_Dashboard_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.DashBoard_Screen;
import com.google.android.material.imageview.ShapeableImageView;

public class Splash_Screen extends AppCompatActivity {
    ShapeableImageView app_word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ShapeableImageView app_word=findViewById(R.id.app_word);

        YoYo.with(Techniques.FadeIn)
                .duration(3000)
                .repeat(5)
                .playOn(findViewById(R.id.app_word));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animatoo.INSTANCE.animateZoom(Splash_Screen.this);

                if (isFirstTimeLogin().equals("intro") ) {

                    Intent intent1 = new Intent(Splash_Screen.this, Intro_Starting_Screen.class);
                    startActivity(intent1);
                    finish();

                } else if(isFirstTimeLogin().equals("user")) {

                    Intent i=new Intent(Splash_Screen.this, DashBoard_Screen.class);
                    startActivity(i);
                    finish();
                }else if(isFirstTimeLogin().equals("admin")) {

                    Intent i=new Intent(Splash_Screen.this, Admin_DashBoard_Screen.class);
                    startActivity(i);
                    finish();
                } else{
                    Intent i=new Intent(Splash_Screen.this, NGO_Dashboard_Screen.class);
                    startActivity(i);
                    finish();
                }

//
            }
        },3000);
    }

    private String isFirstTimeLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("NGOLogin",MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("AdminLogin",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("hasLoggedIn",false))
        {
            return "user";
        }else if(sharedPreferences1.getBoolean("hasLoggedIn",false)){
            return "ngo";
        }else if(sharedPreferences2.getBoolean("hasLoggedIn",false)){
            return "admin";
        }else{
            return "intro";
        }

    }


}