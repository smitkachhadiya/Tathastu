package com.example.tathastu.NGO_Package.NGO_Food_Camp.Donor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NGO_food_user_request_inDetails extends AppCompatActivity {
    FloatingActionButton but_back;
    Button but_call;
    AppCompatTextView name, number, email, loc, note;
    String example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_food_user_request_in_details);

        name = findViewById(R.id.display_food_donor_name);
        number = findViewById(R.id.display_food_donor_phone);
        email = findViewById(R.id.display_food_donor_email);
        loc = findViewById(R.id.display_food_donor_address);
        note = findViewById(R.id.display_food_donor_note);


        but_back = findViewById(R.id.BTN_back);
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_food_user_request_inDetails.this);
            }
        });

//        example = number.getText().toString();
        but_call = findViewById(R.id.BTN_food_donor_call);

        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name");
        name.setText(name1);
        String number1 = intent.getStringExtra("number");
        number.setText(number1);
        String email1 = intent.getStringExtra("email");
        email.setText(email1);
        String loc1 = intent.getStringExtra("loc");
        loc.setText(loc1);
        String note1 = intent.getStringExtra("note");
        note.setText(note1);
        but_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number1));
                startActivity(callIntent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

}