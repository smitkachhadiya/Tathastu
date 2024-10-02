package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Reset_PWD.Reset_Pwd_Screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class B_Details_of_person extends AppCompatActivity {

    FloatingActionButton but_back;
    Button but_call;
    TextView bgruop;
            AppCompatTextView name, age1, weight, address, note, phone;
String example;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdetails_of_person);

        bgruop = findViewById(R.id.display_group);
        name = findViewById(R.id.display_name);
        age1 = findViewById(R.id.display_age);
        weight = findViewById(R.id.display_weight);
        address = findViewById(R.id.display_address);
        note = findViewById(R.id.display_note);
        phone = findViewById(R.id.display_phone);


        but_back = findViewById(R.id.BTN_back);
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(B_Details_of_person.this);
            }
        });

//        example = phone.getText().toString();
        but_call = findViewById(R.id.BTN_call);

        Intent intent = getIntent();
        String bgroup = intent.getStringExtra("bgroup");
        bgruop.setText(bgroup);
        String title = intent.getStringExtra("title");
        name.setText(title);
        String age = intent.getStringExtra("age");
        age1.setText(age);
        String mno = intent.getStringExtra("mno");
        phone.setText(mno);
        String loc = intent.getStringExtra("loc");
        address.setText(loc);

        but_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(B_Details_of_person.this);
        finish();
    }
}