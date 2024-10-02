package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class O_Edit_detail_page extends AppCompatActivity {

    FloatingActionButton btn_back;
    TextInputEditText txt_name1,txt_sdate,txt_edate,txt_location,txt_note,txt_mno;
    Button btn_save;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oedit_detail_page);

        btn_back = findViewById(R.id.BTN_back);
        btn_save = findViewById(R.id.BTN_add);
        txt_name1 = findViewById(R.id.txt_name);
        txt_sdate = findViewById(R.id.txt_sdate);
        txt_edate = findViewById(R.id.txt_edate);
        txt_location = findViewById(R.id.txt_location);
        txt_note = findViewById(R.id.txt_note);
        txt_mno = findViewById(R.id.txt_mno);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(O_Edit_detail_page.this);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(O_Edit_detail_page.this, "Request Saved.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(O_Edit_detail_page.this, O_my_request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(O_Edit_detail_page.this);
            }
        });

        Intent intent = getIntent();
        String title1 = intent.getStringExtra("title");
        txt_name1.setText(title1);
        String title2 = intent.getStringExtra("sdate");
        txt_sdate.setText(title2);
        String title3 = intent.getStringExtra("edate");
        txt_edate.setText(title3);
        String title4 = intent.getStringExtra("mno");
        txt_mno.setText(title4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(O_Edit_detail_page.this);
    }
}