package com.example.tathastu.NGO_Package.NGO_Education_Camp.History;

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

public class NGO_edu_camp_historyIndetails extends AppCompatActivity {

    FloatingActionButton but_back;
    Button but_call;
    AppCompatTextView name, sdate, edate, address, note, phone;
    String example;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_edu_camp_history_indetails);

        name = findViewById(R.id.display_edu_name);
        sdate = findViewById(R.id.display_edu_sdate);
        edate = findViewById(R.id.display_edu_edate);
        address = findViewById(R.id.display_edu_address);
        note = findViewById(R.id.display_edu_note);
        phone = findViewById(R.id.display_edu_phone);

        but_back = findViewById(R.id.BTN_back);
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_edu_camp_historyIndetails.this);
            }
        });
        example = phone.getText().toString();
        but_call = findViewById(R.id.BTN_edu_call);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        name.setText(title);
        String sdate1 = intent.getStringExtra("sdate");
        sdate.setText(sdate1);
        String edate1 = intent.getStringExtra("edate");
        edate.setText(edate1);
        String loc = intent.getStringExtra("loc");
        address.setText(loc);
        String mno = intent.getStringExtra("mno");
        phone.setText(mno);
        String des = intent.getStringExtra("description");
        note.setText(des);
        but_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mno));
                startActivity(callIntent);
            }
        });
    }
}