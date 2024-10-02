package com.example.tathastu.Common_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tathastu.Common_Screens.Intro.Intro_Starting_Screen;
import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class After_Intro_Start extends AppCompatActivity {
    CardView card_start_anim1, card_start_anim2, card_start_anim3, card_start_anim4, card_start_anim5, card_start_anim6, card_start_anim7, card_start_anim8;
    ExtendedFloatingActionButton start;
    public com.example.tathastu.Common_Screens.Intro.intropref intropref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_intro_start);

        card_start_anim1 = findViewById(R.id.card_start_anim1);
        card_start_anim2 = findViewById(R.id.card_start_anim2);
        card_start_anim3 = findViewById(R.id.card_start_anim3);
        card_start_anim4 = findViewById(R.id.card_start_anim4);
        card_start_anim5 = findViewById(R.id.card_start_anim5);
        card_start_anim6 = findViewById(R.id.card_start_anim6);
        card_start_anim7 = findViewById(R.id.card_start_anim7);
        card_start_anim8 = findViewById(R.id.card_start_anim8);
        start = findViewById(R.id.BTN_intro_lets);

        // Animate after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateScreen();
            }
        }, 100);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(After_Intro_Start.this, Selection_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateFade(After_Intro_Start.this);
                finish();
            }
        });
    }

    private void animateScreen() {
        YoYo.with(Techniques.SlideInUp)
                .duration(2000)
                .repeat(0)
                .playOn(card_start_anim1);
        YoYo.with(Techniques.SlideInUp)
                .duration(2100)
                .repeat(0)
                .playOn(card_start_anim2);
        YoYo.with(Techniques.SlideInUp)
                .duration(2300)
                .repeat(0)
                .playOn(card_start_anim3);
        YoYo.with(Techniques.SlideInUp)
                .duration(2500)
                .repeat(0)
                .playOn(card_start_anim4);
        YoYo.with(Techniques.SlideInUp)
                .duration(2800)
                .repeat(0)
                .playOn(card_start_anim5);
        YoYo.with(Techniques.SlideInUp)
                .duration(3000)
                .repeat(0)
                .playOn(card_start_anim6);
        YoYo.with(Techniques.SlideInUp)
                .duration(3100)
                .repeat(0)
                .playOn(card_start_anim7);
        YoYo.with(Techniques.SlideInUp)
                .duration(3300)
                .repeat(0)
                .playOn(card_start_anim8);
    }
}
