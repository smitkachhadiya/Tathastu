package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class O_add_details extends AppCompatActivity {

    FloatingActionButton btn_back;
    TextView txt_name,txt_sdate,txt_edate,txt_location,txt_note,txt_mno;
    Button btn_submit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oadd_details);

        btn_back = findViewById(R.id.BTN_back);
        btn_submit = findViewById(R.id.BTN_add);
        txt_name = findViewById(R.id.txt_name);
        txt_sdate = findViewById(R.id.txt_sdate);
        txt_edate = findViewById(R.id.txt_edate);
        txt_location = findViewById(R.id.txt_location);
        txt_note = findViewById(R.id.txt_note);
        txt_mno = findViewById(R.id.txt_mno);

        String cname = txt_name.getText().toString();
        String csdate = txt_name.getText().toString();
        String cedate = txt_name.getText().toString();
        String address = txt_name.getText().toString();
        String adetails = txt_name.getText().toString();
        String mno = txt_name.getText().toString();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(O_add_details.this);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
//                String userId = sharedPreferences1.getString("userId","");
//
//                String userIdd = UUID.randomUUID().toString();
//
//                DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("user");
//
//                Map<String, Object> map = new HashMap<>();
//                map.put("camp_name", cname);
//                map.put("camp_start_dte", csdate);
//                map.put("camp_end_date", cedate);
//                map.put("address", address);
//                map.put("additional_detils", adetails);
//                map.put("mobile",mno);
//
//                userdata.child(userId).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        if (snapshot.exists()) {
//
//                            userdata.child(userId).child("ngo").child("blood").child(userIdd).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//
//                                    Toast.makeText(O_add_details.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
//
//                                }
//                            });
//
//                        } else {
//
//                            Toast.makeText(O_add_details.this, "Data is Not Updated.", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                Toast.makeText(O_add_details.this, "Request Created Successfuly", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(O_add_details.this, B_Request_page.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(O_add_details.this);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(O_add_details.this);
        finish();
    }
}